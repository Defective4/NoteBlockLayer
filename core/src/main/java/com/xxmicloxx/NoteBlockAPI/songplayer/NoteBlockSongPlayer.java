package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;

/**
 * SongPlayer created at a specified NoteBlock
 */
public class NoteBlockSongPlayer extends RangeSongPlayer {

    private Block noteBlock;

    public NoteBlockSongPlayer(Song song) {
        super(song);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.NoteBlockSongPlayer.class);
    }

    public NoteBlockSongPlayer(Song song, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(song, soundCategory);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.NoteBlockSongPlayer.class);
    }

    public NoteBlockSongPlayer(Playlist playlist, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(playlist, soundCategory);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.NoteBlockSongPlayer.class);
    }

    public NoteBlockSongPlayer(Playlist playlist) {
        super(playlist);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.NoteBlockSongPlayer.class);
    }

    private NoteBlockSongPlayer(SongPlayer songPlayer) {
        super(songPlayer);
    }

    @Override
    void update(String key, Object value) {
        super.update(key, value);

        switch (key) {
            case "noteBlock":
                noteBlock = (Block) value;
                break;
        }
    }

    /**
     * Returns true if the Player is able to hear the current NoteBlockSongPlayer
     *
     * @param player in range
     * @return ability to hear the current NoteBlockSongPlayer
     */
    @Override
    public boolean isInRange(Player player) {
        return false;
    }

    /**
     * Get the Block this SongPlayer is played at
     *
     * @return Block representing a NoteBlock
     */
    public Block getNoteBlock() {
        return noteBlock;
    }

    /**
     * Set the Block this SongPlayer is played at
     */
    public void setNoteBlock(Block noteBlock) {
        this.noteBlock = noteBlock;
        CallUpdate("noteBlock", noteBlock);
    }

    @Override
    public void playTick(Player player, int tick) {
        // no-op :(
    }
}