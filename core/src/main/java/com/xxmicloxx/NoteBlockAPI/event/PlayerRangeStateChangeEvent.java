package com.xxmicloxx.NoteBlockAPI.event;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;

/**
 * Called whenever a Player enters or leave the range of a stationary SongPlayer
 */
public class PlayerRangeStateChangeEvent implements Event {


    private SongPlayer song;
    private Player player;
    private boolean state;

    public PlayerRangeStateChangeEvent(SongPlayer song, Player player, boolean state) {
        this.song = song;
        this.player = player;
        this.state = state;
    }


    /**
     * Returns SongPlayer which range Player enters or leaves
     *
     * @return SongPlayer
     */
    public SongPlayer getSongPlayer() {
        return song;
    }

    /**
     * Returns Player which enter/leave SongPlayer range
     *
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns true if Player is actually in SongPlayer range
     *
     * @return boolean determining if is Player in SongPlayer range
     */
    public boolean isInRange() {
        return state;
    }

}
