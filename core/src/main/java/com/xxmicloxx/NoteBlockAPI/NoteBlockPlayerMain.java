package com.xxmicloxx.NoteBlockAPI;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.ExecutionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.NoteBlockAPI}
 */
@Deprecated
public class NoteBlockPlayerMain {

    public static NoteBlockPlayerMain plugin = new NoteBlockPlayerMain();

    public Map<String, ArrayList<SongPlayer>> playingSongs =
            Collections.synchronizedMap(new HashMap<String, ArrayList<SongPlayer>>());
    public Map<String, Byte> playerVolume = Collections.synchronizedMap(new HashMap<String, Byte>());

    private boolean disabling;

    /**
     * Returns true if a Player is currently receiving a song
     *
     * @param player
     * @return is receiving a song
     */
    public static boolean isReceivingSong(Player player) {
        return plugin.playingSongs.get(player.getUuid()) != null
               && !plugin.playingSongs.get(player.getUuid()).isEmpty();
    }

    /**
     * Stops the song for a Player
     *
     * @param player
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
     *
     * @param player
     * @param volume
     */
    public static void setPlayerVolume(Player player, byte volume) {
        plugin.playerVolume.put(player.getUsername(), volume);
        NoteBlockAPI.setPlayerVolume(player, volume);
    }

    /**
     * Gets the volume for a given Player
     *
     * @param player
     * @return volume (byte)
     */
    public static byte getPlayerVolume(Player player) {
        Byte byteObj = plugin.playerVolume.get(player.getUsername());
        if (byteObj == null) {
            byteObj = 100;
            plugin.playerVolume.put(player.getUsername(), byteObj);
        }
        return byteObj;
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
