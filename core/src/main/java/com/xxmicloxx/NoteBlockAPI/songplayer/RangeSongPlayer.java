package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import net.minestom.server.entity.Player;

/**
 * SongPlayer playing only in specified distance
 */
public abstract class RangeSongPlayer extends SongPlayer {

    private int distance = 16;

    public RangeSongPlayer(Song song, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(song, soundCategory);
    }

    public RangeSongPlayer(Song song) {
        super(song);
    }

    protected RangeSongPlayer(com.xxmicloxx.NoteBlockAPI.SongPlayer songPlayer) {
        super(songPlayer);
    }

    public RangeSongPlayer(Playlist playlist, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(playlist, soundCategory);
    }

    public RangeSongPlayer(Playlist playlist) {
        super(playlist);
    }

    @Override
    void update(String key, Object value) {
        super.update(key, value);

        switch (key) {
            case "distance":
                distance = (int) value;
                break;
        }
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
     * Returns true if the Player is able to hear the current RangeSongPlayer
     *
     * @param player in range
     * @return ability to hear the current RangeSongPlayer
     */
    public abstract boolean isInRange(Player player);

}
