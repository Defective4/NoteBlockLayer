package com.xxmicloxx.NoteBlockAPI;

import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer}
 */
@Deprecated
public class PositionSongPlayer extends SongPlayer {

    private Pos targetLocation;
    private int distance = 16;

    public PositionSongPlayer(Song song) {
        super(song);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer.class);
    }

    public PositionSongPlayer(Song song, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(song, soundCategory);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer.class);
    }

    public PositionSongPlayer(com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer songPlayer) {
        super(songPlayer);
    }

    @Override
    public void update(String key, Object value) {
        super.update(key, value);

        switch (key) {
            case "distance":
                distance = (int) value;
                break;
            case "targetLocation":
                targetLocation = (Pos) value;
                break;
        }
    }

    @Override
    public void playTick(Player player, int tick) {

        byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

        for (Layer layer : song.getLayerHashMap().values()) {
            Note note = layer.getNote(tick);
            if (note == null) continue;

            float volume = (layer.getVolume() * (int) this.volume * (int) playerVolume) / 1000000F
                           * (1F / 16F * getDistance());
            float pitch = NotePitch.getPitch(note.getKey() - 33);

            if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
                CustomInstrument instrument = song.getCustomInstruments()
                        [note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

                if (instrument.getSound() != null) {
                    CompatibilityUtils.playSound(player, targetLocation, instrument.getSound(),
                                                 this.soundCategory, volume, pitch);
                } else {
                    CompatibilityUtils.playSound(player, targetLocation, instrument.getSoundfile(),
                                                 this.soundCategory, volume, pitch);
                }
            } else {
                CompatibilityUtils.playSound(player, targetLocation,
                                             InstrumentUtils.getInstrument(note.getInstrument()), this.soundCategory,
                                             volume, pitch);
            }

            if (isPlayerInRange(player)) {
                if (!this.playerList.get(player.getUsername())) {
                    playerList.put(player.getUsername(), true);
                    MinecraftServer.getGlobalEventHandler().call(new PlayerRangeStateChangeEvent(this, player, true));
                }
            } else {
                if (this.playerList.get(player.getUsername())) {
                    playerList.put(player.getUsername(), false);
                    MinecraftServer.getGlobalEventHandler().call(new PlayerRangeStateChangeEvent(this, player, false));
                }
            }
        }
    }

    public Pos getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Pos targetLocation) {
        this.targetLocation = targetLocation;
        CallUpdate("targetLocation", targetLocation);
    }

    public int getDistance() {
        return distance;
    }

    /**
     * Sets distance in blocks where would be player able to hear sound.
     *
     * @param distance (Default 16 blocks)
     */
    public void setDistance(int distance) {
        this.distance = distance;
        CallUpdate("distance", distance);
    }

    /**
     * Returns true if the Player is able to hear the current PositionSongPlayer
     *
     * @param player in range
     * @return ability to hear the current PositionSongPlayer
     */
    @Deprecated
    public boolean isPlayerInRange(Player player) {
        return player.getPosition().distance(targetLocation) <= getDistance();
    }

}
