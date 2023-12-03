package com.xxmicloxx.NoteBlockAPI;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.ExecutionType;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main class; contains methods for playing and adjusting songs for players
 */
public class NoteBlockAPI {

    private static final NoteBlockAPI plugin = new NoteBlockAPI();

    private Map<UUID, ArrayList<SongPlayer>> playingSongs = new ConcurrentHashMap<UUID, ArrayList<SongPlayer>>();
    private Map<UUID, Byte> playerVolume = new ConcurrentHashMap<UUID, Byte>();

    private boolean disabling;

    /**
     * Returns true if a Player is currently receiving a song
     *
     * @param player
     * @return is receiving a song
     */
    public static boolean isReceivingSong(Player player) {
        return isReceivingSong(player.getUuid());
    }

    /**
     * Returns true if a Player with specified UUID is currently receiving a song
     *
     * @param uuid
     * @return is receiving a song
     */
    public static boolean isReceivingSong(UUID uuid) {
        ArrayList<SongPlayer> songs = plugin.playingSongs.get(uuid);
        return (songs != null && !songs.isEmpty());
    }

    /**
     * Stops the song for a Player
     *
     * @param player
     */
    public static void stopPlaying(Player player) {
        stopPlaying(player.getUuid());
    }

    /**
     * Stops the song for a Player
     *
     * @param uuid
     */
    public static void stopPlaying(UUID uuid) {
        ArrayList<SongPlayer> songs = plugin.playingSongs.get(uuid);
        if (songs == null) {
            return;
        }
        for (SongPlayer songPlayer : songs) {
            songPlayer.removePlayer(uuid);
        }
    }

    /**
     * Sets the volume for a given Player
     *
     * @param player
     * @param volume
     */
    public static void setPlayerVolume(Player player, byte volume) {
        setPlayerVolume(player.getUuid(), volume);
    }

    /**
     * Sets the volume for a given Player
     *
     * @param uuid
     * @param volume
     */
    public static void setPlayerVolume(UUID uuid, byte volume) {
        plugin.playerVolume.put(uuid, volume);
    }

    /**
     * Gets the volume for a given Player
     *
     * @param player
     * @return volume (byte)
     */
    public static byte getPlayerVolume(Player player) {
        return getPlayerVolume(player.getUuid());
    }

    /**
     * Gets the volume for a given Player
     *
     * @param uuid
     * @return volume (byte)
     */
    public static byte getPlayerVolume(UUID uuid) {
        Byte byteObj = plugin.playerVolume.get(uuid);
        if (byteObj == null) {
            byteObj = 100;
            plugin.playerVolume.put(uuid, byteObj);
        }
        return byteObj;
    }

    public static ArrayList<SongPlayer> getSongPlayersByPlayer(Player player) {
        return getSongPlayersByPlayer(player.getUuid());
    }

    public static ArrayList<SongPlayer> getSongPlayersByPlayer(UUID player) {
        return plugin.playingSongs.get(player);
    }

    public static void setSongPlayersByPlayer(Player player, ArrayList<SongPlayer> songs) {
        setSongPlayersByPlayer(player.getUuid(), songs);
    }

    public static void setSongPlayersByPlayer(UUID player, ArrayList<SongPlayer> songs) {
        plugin.playingSongs.put(player, songs);
    }

    public static NoteBlockAPI getAPI() {
        return plugin;
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
