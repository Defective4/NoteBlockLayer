package com.xxmicloxx.NoteBlockAPI;

import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import net.minestom.server.entity.Player;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer}
 */
@Deprecated
public class RadioSongPlayer extends SongPlayer {

    public RadioSongPlayer(Song song) {
        super(song);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer.class);
    }

    public RadioSongPlayer(Song song, net.kyori.adventure.sound.Sound.Source soundCategory) {
        super(song, soundCategory);
        makeNewClone(com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer.class);
    }

    public RadioSongPlayer(com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer songPlayer) {
        super(songPlayer);
    }

    @Override
    public void playTick(Player player, int tick) {
        byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

        for (Layer layer : song.getLayerHashMap().values()) {
            Note note = layer.getNote(tick);
            if (note == null) {
                continue;
            }

            float volume = layer.getVolume() * (int) this.volume * (int) playerVolume / 1000000F;
            float pitch = NotePitch.getPitch(note.getKey() - 33);

            if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
                CustomInstrument instrument = song.getCustomInstruments()
                        [note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

                if (instrument.getSound() != null) {
                    CompatibilityUtils.playSound(player, player.getPosition().add(0, player.getEyeHeight(), 0),
                                                 instrument.getSound(),
                                                 this.soundCategory, volume, pitch);
                } else {
                    CompatibilityUtils.playSound(player, player.getPosition().add(0, player.getEyeHeight(), 0),
                                                 instrument.getSoundfile(),
                                                 this.soundCategory, volume, pitch);
                }
            } else {
                CompatibilityUtils.playSound(player,
                                             player.getPosition().add(0, player.getEyeHeight(), 0),
                                             InstrumentUtils.getInstrument(note.getInstrument()),
                                             this.soundCategory,
                                             volume,
                                             pitch);
            }
        }
    }

}
