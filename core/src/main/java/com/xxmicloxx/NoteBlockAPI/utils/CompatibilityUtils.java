package com.xxmicloxx.NoteBlockAPI.utils;

import com.xxmicloxx.NoteBlockAPI.model.CustomInstrument;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.bukkit.Sound;

import java.util.ArrayList;

/**
 * Fields/methods for reflection &amp; version checking
 */
public class CompatibilityUtils {

    /**
     * Returns whether the version of Bukkit is or is after 1.12
     *
     * @return version is after 1.12
     * @deprecated Compare {@link #getServerVersion()} with 0.0112f
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
    protected static boolean isSoundCategoryCompatible() {
        return true;
    }

    /**
     * Plays a sound using NMS &amp; reflection
     *
     * @param player
     * @param location
     * @param sound
     * @param category
     * @param volume
     * @param pitch
     * @deprecated
     */
    public static void playSound(Player player, Pos location, String sound,
                                 net.kyori.adventure.sound.Sound.Source category, float volume, float pitch) {
        playSound(player, location, sound, category, volume, pitch, 0);
    }

    /**
     * Plays a sound using NMS &amp; reflection
     *
     * @param player
     * @param location
     * @param sound
     * @param category
     * @param volume
     * @param pitch
     * @deprecated
     */
    public static void playSound(Player player, Pos location, String sound,
                                 net.kyori.adventure.sound.Sound.Source category, float volume, float pitch, boolean stereo) {
        playSound(player, location, sound, category, volume, pitch, stereo ? 2 : 0);
    }

    /**
     * Plays a sound using NMS &amp; reflection
     *
     * @param player
     * @param location
     * @param sound
     * @param category
     * @param volume
     * @param pitch
     * @deprecated
     */
    public static void playSound(Player player, Pos location, Sound sound,
                                 net.kyori.adventure.sound.Sound.Source category, float volume, float pitch) {
        playSound(player, location, sound, category, volume, pitch, 0);
    }

    /**
     * Plays a sound using NMS &amp; reflection
     *
     * @param player
     * @param location
     * @param sound
     * @param category
     * @param volume
     * @param pitch
     * @deprecated
     */
    public static void playSound(Player player, Pos location, Sound sound,
                                 net.kyori.adventure.sound.Sound.Source category, float volume, float pitch, boolean stereo) {
        playSound(player, location, sound, category, volume, pitch, stereo ? 2 : 0);
    }

    /**
     * Plays a sound using NMS &amp; reflection
     *
     * @param player
     * @param location
     * @param sound
     * @param category
     * @param volume
     * @param pitch
     * @param distance
     */
    public static void playSound(Player player, Pos location, String sound,
                                 net.kyori.adventure.sound.Sound.Source category, float volume, float pitch, float distance) {
        playSoundUniversal(player, location, sound, category, volume, pitch, distance);
    }

    /**
     * Plays a sound using NMS &amp; reflection
     *
     * @param player
     * @param location
     * @param sound
     * @param category
     * @param volume
     * @param pitch
     * @param distance
     */
    public static void playSound(Player player, Pos location, Sound sound,
                                 net.kyori.adventure.sound.Sound.Source category, float volume, float pitch, float distance) {
        playSoundUniversal(player, location, sound, category, volume, pitch, distance);
    }

    private static void playSoundUniversal(Player player, Pos location, Object sound,
                                           net.kyori.adventure.sound.Sound.Source category, float volume, float pitch, float distance) {
        if(sound instanceof Sound snd) {
            player.playSound(net.kyori.adventure.sound.Sound.sound(
                    snd,
                    category,
                    volume,
                    pitch
            ), location);
        }else if(sound instanceof String str) {
            try {
                player.playSound(net.kyori.adventure.sound.Sound.sound(
                        NamespaceID.from(str),
                        category,
                        volume,
                        pitch
                ), location);
            }catch (Exception ignored) {}
        }
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
            instruments.add(new CustomInstrument((byte) 0, "Guitar", "block.note_block.guitar.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Flute", "block.note_block.flute.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Bell", "block.note_block.bell.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Chime", "block.note_block.icechime.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Xylophone", "block.note_block.xylobone.ogg"));
            return instruments;
        }

        if (serverVersion == 0.0114f) {
            instruments.add(new CustomInstrument((byte) 0, "Iron Xylophone", "block.note_block.iron_xylophone.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Cow Bell", "block.note_block.cow_bell.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Didgeridoo", "block.note_block.didgeridoo.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Bit", "block.note_block.bit.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Banjo", "block.note_block.banjo.ogg"));
            instruments.add(new CustomInstrument((byte) 0, "Pling", "block.note_block.pling.ogg"));
            return instruments;
        }
        return instruments;
    }

    /**
     * Return list of custom instruments based on song first custom instrument index and server version
     *
     * @param firstCustomInstrumentIndex
     * @return
     */
    public static ArrayList<CustomInstrument> getVersionCustomInstrumentsForSong(int firstCustomInstrumentIndex) {
        ArrayList<CustomInstrument> instruments = new ArrayList<>();

        if (getServerVersion() < 0.0112f) {
            if (firstCustomInstrumentIndex == 10) {
                instruments.addAll(getVersionCustomInstruments(0.0112f));
            } else if (firstCustomInstrumentIndex == 16) {
                instruments.addAll(getVersionCustomInstruments(0.0112f));
                instruments.addAll(getVersionCustomInstruments(0.0114f));
            }
        } else if (getServerVersion() < 0.0114f) {
            if (firstCustomInstrumentIndex == 16) {
                instruments.addAll(getVersionCustomInstruments(0.0114f));
            }
        }

        return instruments;
    }

    /**
     * Returns server version as float less than 1 with two digits for each version part
     *
     * @return e.g. 0.011401f for 1.14.1
     */
    public static float getServerVersion() {
        return 0.012001f;
    }

    public static Block getNoteBlockMaterial() {
        return Block.NOTE_BLOCK;
    }

}
