package com.xxmicloxx.NoteBlockAPI;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.bukkit.Sound;

import java.util.ArrayList;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils}
 */
@Deprecated
public final class CompatibilityUtils {

    private CompatibilityUtils() {
    }

    /**
     * Returns whether the version of Bukkit is or is after 1.12
     *
     * @return version is after 1.12
     */
    public static boolean isPost1_12() {
        return true;
    }

    /**
     * Returns if SoundCategory is able to be used
     *
     * @return can use SoundCategory
     * @see SoundCategory
     */
    private static boolean isSoundCategoryCompatible() {
        return true;
    }

    /**
     * Plays a sound using NMS and reflection
     */
    public static void playSound(Player player, Pos location, String sound,
                                 net.kyori.adventure.sound.Sound.Source category, float volume, float pitch) {
        com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.playSound(player,
                                                                      location,
                                                                      sound,
                                                                      category,
                                                                      volume,
                                                                      pitch);
    }

    /**
     * Plays a sound using NMS and reflection
     */
    public static void playSound(Player player, Pos location, Sound sound,
                                 net.kyori.adventure.sound.Sound.Source category, float volume, float pitch) {
        com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.playSound(player,
                                                                      location,
                                                                      sound,
                                                                      category,
                                                                      volume,
                                                                      pitch);
    }

    /**
     * Gets instruments which were added post-1.12
     *
     * @return ArrayList of instruments
     * @deprecated Use {@link #getVersionCustomInstruments(float)}
     */
    public static ArrayList<CustomInstrument> get1_12Instruments() {
        return getVersionCustomInstruments(0.0112f);
    }

    /**
     * Return list of instuments which were added in specified version
     *
     * @param serverVersion 1.12 = 0.0112f, 1.14 = 0.0114f,...
     * @return list of custom instruments, if no instuments were added in specified version returns empty list
     */
    public static ArrayList<CustomInstrument> getVersionCustomInstruments(float serverVersion) {
        ArrayList<CustomInstrument> instruments = new ArrayList<>();
        if (serverVersion == 0.0112f) {
            instruments.add(new CustomInstrument((byte) 0, "Guitar", "guitar.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Flute", "flute.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Bell", "bell.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Chime", "icechime.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Xylophone", "xylobone.ogg"));
            return instruments;
        }

        if (serverVersion == 0.0114f) {
            instruments.add(new CustomInstrument((byte) 0, "Iron Xylophone", "iron_xylophone.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Cow Bell", "cow_bell.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Didgeridoo", "didgeridoo.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Bit", "bit.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Banjo", "banjo.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Pling", "pling.ogg"));
            return instruments;
        }
        return instruments;
    }

    /**
     * Return list of custom instruments based on song first custom instrument index and server version
     */
    public static ArrayList<CustomInstrument> getVersionCustomInstrumentsForSong(int firstCustomInstrumentIndex) {
        ArrayList<CustomInstrument> instruments = new ArrayList<>();

        if (com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.getServerVersion() < 0.0112f) {
            if (firstCustomInstrumentIndex == 10) {
                instruments.addAll(getVersionCustomInstruments(0.0112f));
            } else if (firstCustomInstrumentIndex == 16) {
                instruments.addAll(getVersionCustomInstruments(0.0112f));
                instruments.addAll(getVersionCustomInstruments(0.0114f));
            }
        } else if (com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.getServerVersion() < 0.0114f) {
            if (firstCustomInstrumentIndex == 16) {
                instruments.addAll(getVersionCustomInstruments(0.0114f));
            }
        }

        return instruments;
    }

}
