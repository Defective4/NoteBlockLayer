package org.bukkit;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum Instrument {
    PIANO(0),
    BASS_DRUM(1),
    SNARE_DRUM(2),
    STICKS(3),
    BASS_GUITAR(4),
    FLUTE(5),
    BELL(6),
    GUITAR(7),
    CHIME(8),
    XYLOPHONE(9),
    IRON_XYLOPHONE(10),
    COW_BELL(11),
    DIDGERIDOO(12),
    BIT(13),
    BANJO(14),
    PLING(15),
    ZOMBIE(16),
    SKELETON(17),
    CREEPER(18),
    DRAGON(19),
    WITHER_SKELETON(20),
    PIGLIN(21),
    CUSTOM_HEAD(22);

    private static final Map<Byte, Instrument> BY_DATA = new HashMap<>();

    static {
        Instrument[] var0 = values();

        for (Instrument instrument : var0) {
            BY_DATA.put(instrument.getType(), instrument);
        }

    }

    private final byte type;

    Instrument(int type) {
        this.type = (byte) type;
    }

    @Internal
    public static @Nullable Instrument getByType(byte type) {
        return (Instrument) BY_DATA.get(type);
    }

    @Internal
    public byte getType() {
        return this.type;
    }
}
