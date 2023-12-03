package com.xxmicloxx.NoteBlockAPI;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.event.PlayerRangeStateChangeEvent}
 */
@Deprecated
public class PlayerRangeStateChangeEvent implements Event {

    
    private SongPlayer song;
    private Player player;
    private boolean state;

    public PlayerRangeStateChangeEvent(SongPlayer song, Player player, boolean state) {
        this.song = song;
        this.player = player;
        this.state = state;
    }

    public SongPlayer getSongPlayer() {
        return song;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isInRange() {
        return state;
    }

}
