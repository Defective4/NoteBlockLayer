package com.xxmicloxx.NoteBlockAPI;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.ExecutionType;

import java.util.*;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.NoteBlockAPI}
 */
@Deprecated
public class NoteBlockPlayerMain {

    public static NoteBlockPlayerMain plugin = new NoteBlockPlayerMain();

    public final Map<UUID, ArrayList<SongPlayer>> playingSongs =
            Collections.synchronizedMap(new HashMap<>());
    public final Map<UUID, Byte> playerVolume = Collections.synchronizedMap(new HashMap<>());

    private boolean disabling;

    /**
     * Returns true if a Player is currently receiving a song
     *
     * @return is receiving a song
     */
    public static boolean isReceivingSong(Player player) {
        return plugin.playingSongs.get(player.getUuid()) != null
               && !plugin.playingSongs.get(player.getUuid()).isEmpty();
    }

    /**
     * Stops the song for a Player
     */
    public static void stopPlaying(Player player) {
        if (plugin.playingSongs.get(player.getUuid()) == null) {
            return;
        }
        for (SongPlayer songPlayer : plugin.playingSongs.get(player.getUuid())) {
            songPlayer.removePlayer(player);
        }
    }

    /**
     * Sets the volume for a given Player
     */
    public static void setPlayerVolume(Player player, byte volume) {
        plugin.playerVolume.put(player.getUuid(), volume);
        NoteBlockAPI.setPlayerVolume(player, volume);
    }

    /**
     * Gets the volume for a given Player
     *
     * @return volume (byte)
     */
    public static byte getPlayerVolume(Player player) {
        return plugin.playerVolume.computeIfAbsent(player.getUuid(), k -> (byte) 100);
    }

    public void onEnable() {
        plugin = this;
    }

    public void onDisable() {
        disabling = true;
    }

    public void doSync(Runnable runnable) {
        MinecraftServer.getSchedulerManager().scheduleNextTick(runnable, ExecutionType.SYNC);
    }

    public void doAsync(Runnable runnable) {
        MinecraftServer.getSchedulerManager().scheduleNextTick(runnable, ExecutionType.ASYNC);
    }

    public boolean isDisabling() {
        return disabling;
    }

}
