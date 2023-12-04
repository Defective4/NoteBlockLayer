package com.xxmicloxx.NoteBlockAPI.event;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.minestom.server.event.trait.CancellableEvent;

/**
 * Called whenever a SongPlayer is destroyed
 *
 * @see SongPlayer
 */
public class SongDestroyingEvent implements CancellableEvent {


    private final SongPlayer song;
    private boolean cancelled;

    public SongDestroyingEvent(SongPlayer song) {
        this.song = song;
    }


    /**
     * Returns SongPlayer which is being destroyed
     *
     * @return SongPlayer
     */
    public SongPlayer getSongPlayer() {
        return song;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean arg0) {
        cancelled = arg0;
    }


}
