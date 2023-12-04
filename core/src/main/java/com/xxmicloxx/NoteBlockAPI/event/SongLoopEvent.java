package com.xxmicloxx.NoteBlockAPI.event;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.minestom.server.event.trait.CancellableEvent;

public class SongLoopEvent implements CancellableEvent {


    private final SongPlayer song;
    private boolean cancelled;

    public SongLoopEvent(SongPlayer song) {
        this.song = song;
    }


    /**
     * Returns SongPlayer which {@link Song} ends and is going to start again
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
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
