package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.playmode.ChannelMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoStereoMode;
import net.minestom.server.entity.Player;

/**
 * SongPlayer playing to everyone added to it no matter where he is
 */
public class RadioSongPlayer extends SongPlayer {

    //protected boolean stereo = true;

    public RadioSongPlayer(Song song) {
        super(song);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
    }

    public RadioSongPlayer(Song song, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(song, soundCategory);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
    }

    private RadioSongPlayer(com.xxmicloxx.NoteBlockAPI.SongPlayer songPlayer) {
        super(songPlayer);
    }

    public RadioSongPlayer(Playlist playlist, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(playlist, soundCategory);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
    }

    public RadioSongPlayer(Playlist playlist) {
        super(playlist);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
    }

    @Override
    public void playTick(Player player, int tick) {
        byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

        for (Layer layer : song.getLayerHashMap().values()) {
            Note note = layer.getNote(tick);
            if (note == null) {
                continue;
            }

            float volume = layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVelocity() / 100_00_00_00F;

            channelMode.play(player,
                             player.getPosition().add(0, player.getEyeHeight(), 0),
                             song,
                             layer,
                             note,
                             soundCategory,
                             volume,
                             !enable10Octave);
        }
    }

    /**
     * Returns if the SongPlayer will play Notes from two sources as stereo
     *
     * @return if is played stereo
     * @deprecated
     */
    @Deprecated
    public boolean isStereo() {
        return !(channelMode instanceof MonoMode);
    }

    /**
     * Sets if the SongPlayer will play Notes from two sources as stereo
     *
     * @deprecated
     */
    @Deprecated
    public void setStereo(boolean stereo) {
        channelMode = stereo ? new MonoMode() : new MonoStereoMode();
    }

    /**
     * Sets how will be {@link Note} played to {@link Player} (eg. mono or stereo). Default is {@link MonoMode}.
     */
    public void setChannelMode(ChannelMode mode) {
        channelMode = mode;
    }
}
