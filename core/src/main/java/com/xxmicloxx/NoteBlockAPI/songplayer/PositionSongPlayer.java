package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.event.PlayerRangeStateChangeEvent;
import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

/**
 * SongPlayer created at a specified Location
 */
public class PositionSongPlayer extends RangeSongPlayer {

    private Pos targetLocation;

    public PositionSongPlayer(Song song) {
        super(song);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.PositionSongPlayer.class);
    }

    public PositionSongPlayer(Song song, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(song, soundCategory);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.PositionSongPlayer.class);
    }

    private PositionSongPlayer(SongPlayer songPlayer) {
        super(songPlayer);
    }

    public PositionSongPlayer(Playlist playlist, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(playlist, soundCategory);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.PositionSongPlayer.class);
    }

    public PositionSongPlayer(Playlist playlist) {
        super(playlist);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.PositionSongPlayer.class);
    }

    @Override
    public void update(String key, Object value) {
        super.update(key, value);

        switch (key) {
            case "targetLocation":
                targetLocation = (Pos) value;
                break;
        }
    }

    /**
     * Returns true if the Player is able to hear the current PositionSongPlayer
     *
     * @param player in range
     * @return ability to hear the current PositionSongPlayer
     */
    @Override
    public boolean isInRange(Player player) {
        return player.getPosition().distance(targetLocation) <= getDistance();
    }

    /**
     * Gets location on which is the PositionSongPlayer playing
     */
    public Pos getTargetLocation() {
        return targetLocation;
    }

    /**
     * Sets location on which is the PositionSongPlayer playing
     */
    public void setTargetLocation(Pos targetLocation) {
        this.targetLocation = targetLocation;
        CallUpdate("targetLocation", targetLocation);
    }

    @Override
    public void playTick(Player player, int tick) {
        byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

        for (Layer layer : song.getLayerHashMap().values()) {
            Note note = layer.getNote(tick);
            if (note == null) continue;

            float a = layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVelocity();
            float b = 1F / 16F * getDistance();

            float volume = a / 100_00_00_00F
                           * b;

            channelMode.play(player, targetLocation, song, layer, note, soundCategory, volume, !enable10Octave);

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
