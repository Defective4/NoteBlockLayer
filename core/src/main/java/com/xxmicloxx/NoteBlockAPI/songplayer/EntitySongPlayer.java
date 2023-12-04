package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.PlayerRangeStateChangeEvent;
import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

public class EntitySongPlayer extends RangeSongPlayer {

    private Entity entity;

    public EntitySongPlayer(Song song) {
        super(song);
    }

    public EntitySongPlayer(Song song, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(song, soundCategory);
    }

    public EntitySongPlayer(Playlist playlist, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(playlist, soundCategory);
    }

    public EntitySongPlayer(Playlist playlist) {
        super(playlist);
    }

    /**
     * Returns true if the Player is able to hear the current {@link EntitySongPlayer}
     *
     * @param player in range
     * @return ability to hear the current {@link EntitySongPlayer}
     */
    @Override
    public boolean isInRange(Player player) {
        return player.getPosition().distance(entity.getPosition()) <= getDistance();
    }

    /**
     * Get {@link Entity} associated with this {@link EntitySongPlayer}
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Set entity associated with this {@link EntitySongPlayer}
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void playTick(Player player, int tick) {
        if (entity.isRemoved()) {
            if (autoDestroy) {
                destroy();
            } else {
                setPlaying(false);
            }
        }
        if (player.getInstance() != entity.getInstance()) {
            return; // not in same world
        }

        byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

        for (Layer layer : song.getLayerHashMap().values()) {
            Note note = layer.getNote(tick);
            if (note == null) continue;

            float a = layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVelocity();
            float b = 1F / 16F * getDistance();
            float volume = a / 100_00_00_00F
                           * b;

            channelMode.play(player, entity.getPosition(), song, layer, note, soundCategory, volume, !enable10Octave);

            if (isInRange(player)) {
                if (!playerList.get(player.getUuid())) {
                    playerList.put(player.getUuid(), true);
                    MinecraftServer.getGlobalEventHandler().call(new PlayerRangeStateChangeEvent(this, player, true));
                }
            } else {
                if (playerList.get(player.getUuid())) {
                    playerList.put(player.getUuid(), false);
                    MinecraftServer.getGlobalEventHandler().call(new PlayerRangeStateChangeEvent(this, player, false));
                }
            }
        }
    }
}
