package com.xxmicloxx.NoteBlockAPI.utils;

import com.xxmicloxx.NoteBlockAPI.model.Sound;
import org.bukkit.Instrument;

/**
 * Various methods for working with instruments
 */
public final class InstrumentUtils {

    private InstrumentUtils() {
    }

    /**
     * Returns the org.bukkit.Sound enum for the current server version
     *
     * @return Sound enum (for the current server version)
     * @see Sound
     */
    public static org.bukkit.Sound getInstrument(byte instrument) {
        return Sound.getFromBukkitName(getInstrumentName(instrument));
    }

    /**
     * Add suffix to vanilla instrument to use sound outside 2 octave range
     *
     * @param instrument instrument id
     * @param key        sound key
     * @return warped name
     */
    public static String warpNameOutOfRange(byte instrument, byte key, short pitch) {
        return warpNameOutOfRange(getSoundNameByInstrument(instrument), key, pitch);
    }

    /**
     * Add suffix to qualified name to use sound outside 2 octave range
     *
     * @param name qualified name
     * @param key  sound key
     * @return warped name
     */
    public static String warpNameOutOfRange(String name, byte key, short pitch) {
        key = NoteUtils.applyPitchToKey(key, pitch);
        // -15 base_-2
        // 9 base_-1
        // 33 base
        // 57 base_1
        // 81 base_2
        // 105 base_3
        if (key < 9) name += "_-2";
        else if (key < 33) name += "_-1";
        else //noinspection StatementWithEmptyBody
            if (key < 57) ;
            else if (key < 81) name += "_1";
            else if (key < 105) name += "_2";
        return name;
    }

    /**
     * Returns the name of vanilla instrument
     *
     * @param instrument instrument identifier
     * @return Sound name with full qualified name
     */
    public static String getSoundNameByInstrument(byte instrument) {
        //noinspection RedundantSuppression
        return switch (instrument) {
            case 0 ->
                //noinspection DuplicateBranchesInSwitch
                    "minecraft:block.note_block.harp";
            case 1 -> "minecraft:block.note_block.bass";
            case 2 ->
                //noinspection SpellCheckingInspection
                    "minecraft:block.note_block.basedrum";
            case 3 -> "minecraft:block.note_block.snare";
            case 4 -> "minecraft:block.note_block.hat";
            case 5 -> "minecraft:block.note_block.guitar";
            case 6 -> "minecraft:block.note_block.flute";
            case 7 -> "minecraft:block.note_block.bell";
            case 8 -> "minecraft:block.note_block.chime";
            case 9 -> "minecraft:block.note_block.xylophone";
            case 10 -> "minecraft:block.note_block.iron_xylophone";
            case 11 -> "minecraft:block.note_block.cow_bell";
            case 12 -> "minecraft:block.note_block.didgeridoo";
            case 13 -> "minecraft:block.note_block.bit";
            case 14 -> "minecraft:block.note_block.banjo";
            case 15 ->
                //noinspection SpellCheckingInspection
                    "minecraft:block.note_block.pling";
            default -> "minecraft:block.note_block.harp";
        };
    }

    /**
     * Returns the name of the org.bukkit.Sound enum for the current server version
     *
     * @return Sound enum name (for the current server version)
     * @see Sound
     */
    public static String getInstrumentName(byte instrument) {
        return switch (instrument) {
            case 1 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BASS").name();
            case 2 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BASEDRUM").name();
            case 3 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_SNARE").name();
            case 4 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_HAT").name();
            case 5 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_GUITAR").name();
            case 6 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_FLUTE").name();
            case 7 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BELL").name();
            case 8 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_CHIME").name();
            case 9 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_XYLOPHONE").name();
            case 10 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_IRON_XYLOPHONE").name();
            case 11 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_COW_BELL").name();
            case 12 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_DIDGERIDOO").name();
            case 13 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BIT").name();
            case 14 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BANJO").name();
            case 15 -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_PLING").name();
            default -> Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_HARP").name();
        };
    }

    /**
     * Returns the name of the org.bukkit.Instrument enum for the current server version
     *
     * @return Instrument enum (for the current server version)
     */
    public static Instrument getBukkitInstrument(byte instrument) {
        switch (instrument) {
            case 0:
                return Instrument.PIANO;
            case 1:
                return Instrument.BASS_GUITAR;
            case 2:
                return Instrument.BASS_DRUM;
            case 3:
                return Instrument.SNARE_DRUM;
            case 4:
                return Instrument.STICKS;
            default: {
                if (CompatibilityUtils.getServerVersion() >= 0.0112f) {
                    return switch (instrument) {
                        case 5 -> Instrument.valueOf("GUITAR");
                        case 6 -> Instrument.valueOf("FLUTE");
                        case 7 -> Instrument.valueOf("BELL");
                        case 8 -> Instrument.valueOf("CHIME");
                        case 9 -> Instrument.valueOf("XYLOPHONE");
                        default -> {
                            if (CompatibilityUtils.getServerVersion() >= 0.0114f) {
                                switch (instrument) {
                                    case 10:
                                        yield Instrument.valueOf("IRON_XYLOPHONE");
                                    case 11:
                                        yield Instrument.valueOf("COW_BELL");
                                    case 12:
                                        yield Instrument.valueOf("DIDGERIDOO");
                                    case 13:
                                        yield Instrument.valueOf("BIT");
                                    case 14:
                                        yield Instrument.valueOf("BANJO");
                                    case 15:
                                        yield Instrument.valueOf("PLING");
                                }
                            }
                            yield Instrument.PIANO;
                        }
                    };
                }
                return Instrument.PIANO;
            }
        }
    }

    /**
     * If true, the byte given represents a custom instrument
     *
     * @return whether the byte represents a custom instrument
     */
    public static boolean isCustomInstrument(byte instrument) {
        return instrument >= getCustomInstrumentFirstIndex();
    }

    /**
     * Gets the first index in which a custom instrument
     * can be added to the existing list of instruments
     *
     * @return index where an instrument can be added
     */
    public static byte getCustomInstrumentFirstIndex() {
        if (CompatibilityUtils.getServerVersion() >= 0.0114f) {
            return 16;
        }
        if (CompatibilityUtils.getServerVersion() >= 0.0112f) {
            return 10;
        }
        return 5;
    }

}