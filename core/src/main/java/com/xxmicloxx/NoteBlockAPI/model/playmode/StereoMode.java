package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.CustomInstrument;
import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

/**
 * Uses panning for individual {@link Note} and {@link Layer} based on data from nbs file.
 */
public class StereoMode extends ChannelMode {

    private float maxDistance = 2;
    private ChannelMode fallbackChannelMode;

    @Override
    public void play(Player player, Pos location, Song song, Layer layer, Note note, net.kyori.adventure.sound.Sound.Source soundCategory, float volume, float pitch) {
        if (!song.isStereo() && fallbackChannelMode != null) {
            fallbackChannelMode.play(player, location, song, layer, note, soundCategory, volume, pitch);
            return;
        }

        float distance;
        if (layer.getPanning() == 100) {
            distance = (note.getPanning() - 100) / 100f * maxDistance;
        } else {
            distance = (layer.getPanning() - 100 + note.getPanning() - 100) / 200f * maxDistance;
        }
        if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
            CustomInstrument instrument = song.getCustomInstruments()[note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

            if (instrument.getSound() != null) {
                CompatibilityUtils.playSound(player,
                                             location,
                                             instrument.getSound(),
                                             soundCategory,
                                             volume,
                                             pitch,
                                             distance);
            } else {
                CompatibilityUtils.playSound(player,
                                             location,
                                             instrument.getSoundFileName(),
                                             soundCategory,
                                             volume,
                                             pitch,
                                             distance);
            }
        } else {
            CompatibilityUtils.playSound(player,
                                         location,
                                         InstrumentUtils.getInstrument(note.getInstrument()),
                                         soundCategory,
                                         volume,
                                         pitch,
                                         distance);
        }
    }

    @Override
    public void play(Player player, Pos location, Song song, Layer layer, Note note, net.kyori.adventure.sound.Sound.Source soundCategory, float volume, boolean doTranspose) {
        if (!song.isStereo() && fallbackChannelMode != null) {
            fallbackChannelMode.play(player, location, song, layer, note, soundCategory, volume, doTranspose);
            return;
        }

        float pitch;
        if (doTranspose)
            pitch = NoteUtils.getPitchTransposed(note);
        else
            pitch = NoteUtils.getPitchInOctave(note);

        float distance;
        if (layer.getPanning() == 100) {
            distance = (note.getPanning() - 100) / 100f * maxDistance;
        } else {
            distance = (layer.getPanning() - 100 + note.getPanning() - 100) / 200f * maxDistance;
        }
        if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
            CustomInstrument instrument = song.getCustomInstruments()[note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

            if (!doTranspose) {
                CompatibilityUtils.playSound(player,
                                             location,
                                             InstrumentUtils.warpNameOutOfRange(instrument.getSoundFileName(),
                                                                                note.getKey(),
                                                                                note.getPitch()),
                                             soundCategory,
                                             volume,
                                             pitch,
                                             distance);
            } else {
                if (instrument.getSound() != null) {
                    CompatibilityUtils.playSound(player,
                                                 location,
                                                 instrument.getSound(),
                                                 soundCategory,
                                                 volume,
                                                 pitch,
                                                 distance);
                } else {
                    CompatibilityUtils.playSound(player,
                                                 location,
                                                 instrument.getSoundFileName(),
                                                 soundCategory,
                                                 volume,
                                                 pitch,
                                                 distance);
                }
            }
        } else {
            if (NoteUtils.isOutOfRange(note.getKey(), note.getPitch()) && !doTranspose) {
                CompatibilityUtils.playSound(player,
                                             location,
                                             InstrumentUtils.warpNameOutOfRange(note.getInstrument(),
                                                                                note.getKey(),
                                                                                note.getPitch()),
                                             soundCategory,
                                             volume,
                                             pitch,
                                             distance);
            } else {
                CompatibilityUtils.playSound(player,
                                             location,
                                             InstrumentUtils.getInstrument(note.getInstrument()),
                                             soundCategory,
                                             volume,
                                             pitch,
                                             distance);
            }
        }
    }

    /**
     * Returns scale of panning in blocks. {@link Note} with maximum left panning will be played this distance from {@link Player}'s head on left side.
     */
    public float getMaxDistance() {
        return maxDistance;
    }

    /**
     * Sets scale of panning in blocks. {@link Note} with maximum left panning will be played this distance from {@link Player}'s head on left side.
     */
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    /**
     * Returns fallback {@link ChannelMode} used when song is not stereo.
     *
     * @return ChannelMode or null when fallback ChannelMode is disabled
     */
    public ChannelMode getFallbackChannelMode() {
        return fallbackChannelMode;
    }

    /**
     * Sets fallback {@link ChannelMode} which is used when song is not stereo. Set to null to disable.
     *
     * @throws IllegalArgumentException if parameter is instance of StereoMode
     */
    public void setFallbackChannelMode(ChannelMode fallbackChannelMode) {
        if (fallbackChannelMode instanceof StereoMode)
            throw new IllegalArgumentException("Fallback ChannelMode can't be instance of StereoMode!");

        this.fallbackChannelMode = fallbackChannelMode;
    }
}
