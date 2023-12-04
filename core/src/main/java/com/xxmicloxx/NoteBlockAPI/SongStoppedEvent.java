package com.xxmicloxx.NoteBlockAPI;

import net.minestom.server.event.Event;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.event.SongStoppedEvent}
 */
@Deprecated
public class SongStoppedEvent implements Event {


    private final SongPlayer songPlayer;

    public SongStoppedEvent(SongPlayer songPlayer) {
        this.songPlayer = songPlayer;
    }


    public SongPlayer getSongPlayer() {
        return songPlayer;
    }


}
