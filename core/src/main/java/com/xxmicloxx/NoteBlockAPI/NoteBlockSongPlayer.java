package com.xxmicloxx.NoteBlockAPI;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.songplayer.NoteBlockSongPlayer}
 */
@Deprecated
public class NoteBlockSongPlayer extends SongPlayer {

    private Block noteBlock;
    private int distance = 16;

    public NoteBlockSongPlayer(Song song) {
        super(song);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.songplayer.NoteBlockSongPlayer.class);
    }

    public NoteBlockSongPlayer(Song song, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(song, soundCategory);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.songplayer.NoteBlockSongPlayer.class);
    }

    public NoteBlockSongPlayer(com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer songPlayer) {
        super(songPlayer);
    }

    @Override
    void update(String key, Object value) {
        super.update(key, value);

        switch (key) {
            case "distance":
                distance = (int) value;
                break;
            case "noteBlock":
                noteBlock = (Block) value;
                break;
        }
    }

    @Override
    public void playTick(Player player, int tick) {
        // no-op :(
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
     * Returns true if the Player is able to hear the current NoteBlockSongPlayer
     *
     * @param player in range
     * @return ability to hear the current NoteBlockSongPlayer
     */
    @Deprecated
    public boolean isPlayerInRange(Player player) {
        return false;
    }
}
