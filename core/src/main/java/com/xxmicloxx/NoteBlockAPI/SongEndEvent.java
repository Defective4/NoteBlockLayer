package com.xxmicloxx.NoteBlockAPI;

import net.minestom.server.event.Event;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.event.SongEndEvent}
 */
@Deprecated
public class SongEndEvent implements Event {


    private final SongPlayer song;

    public SongEndEvent(SongPlayer song) {
        this.song = song;
    }


    public SongPlayer getSongPlayer() {
        return song;
    }


}
