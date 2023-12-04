package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

/**
 * Decides how is {@link Note} played to {@link Player}
 */
public abstract class ChannelMode {

    @Deprecated
    public abstract void play(Player player, Pos location, Song song, Layer layer, Note note,
                              net.kyori.adventure.sound.Sound.Source soundCategory, float volume, float pitch);

    public abstract void play(Player player, Pos location, Song song, Layer layer, Note note,
                              net.kyori.adventure.sound.Sound.Source soundCategory, float volume, boolean doTranspose);
}