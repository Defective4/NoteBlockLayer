package com.xxmicloxx.NoteBlockAPI;

import net.minestom.server.event.trait.CancellableEvent;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.event.SongDestroyingEvent}
 */
@Deprecated
public class SongDestroyingEvent implements CancellableEvent {


    private SongPlayer song;
    private boolean cancelled = false;

    public SongDestroyingEvent(SongPlayer song) {
        this.song = song;
    }


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
