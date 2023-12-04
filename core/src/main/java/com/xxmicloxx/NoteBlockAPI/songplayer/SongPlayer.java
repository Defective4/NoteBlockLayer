package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.*;
import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.model.playmode.ChannelMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoMode;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Plays a Song for a list of Players
 */
public abstract class SongPlayer {

    protected final Map<UUID, Boolean> playerList = new ConcurrentHashMap<>();
    protected final Fade fadeIn;
    protected final Fade fadeOut;
    protected final Map<Song, Boolean> songQueue = new ConcurrentHashMap<>(); //True if already played
    protected final NoteBlockAPI plugin;
    private final Lock lock = new ReentrantLock();
    private final Random rng = new Random();
    protected Song song;
    protected Playlist playlist;
    protected int actualSong;
    protected boolean playing;
    protected boolean fading;
    protected short tick = -1;
    protected boolean autoDestroy;
    protected boolean destroyed;
    protected byte volume = 100;
    protected Fade fadeTemp;
    protected RepeatMode repeat = RepeatMode.NO;
    protected boolean random;
    protected Sound.Source soundCategory;
    protected ChannelMode channelMode = new MonoMode();
    protected boolean enable10Octave;

    com.xxmicloxx.NoteBlockAPI.SongPlayer oldSongPlayer;

    public SongPlayer(Song song) {
        this(new Playlist(song), Sound.Source.MASTER);
    }

    public SongPlayer(Song song, net.kyori.adventure.sound.Sound.Source soundCategory) {
        this(new Playlist(song), soundCategory);
    }

    public SongPlayer(Song song, net.kyori.adventure.sound.Sound.Source soundCategory, boolean random) {
        this(new Playlist(song), soundCategory, random);
    }

    public SongPlayer(Playlist playlist) {
        this(playlist, Sound.Source.MASTER);
    }

    public SongPlayer(Playlist playlist, net.kyori.adventure.sound.Sound.Source soundCategory) {
        this(playlist, soundCategory, false);
    }

    public SongPlayer(Playlist playlist, net.kyori.adventure.sound.Sound.Source soundCategory, boolean random) {
        this.playlist = playlist;
        this.random = random;
        this.soundCategory = soundCategory;
        plugin = NoteBlockAPI.getAPI();

        fadeIn = new Fade(FadeType.NONE, 60);
        fadeIn.setFadeStart((byte) 0);
        fadeIn.setFadeTarget(volume);

        fadeOut = new Fade(FadeType.NONE, 60);
        fadeOut.setFadeStart(volume);
        fadeOut.setFadeTarget((byte) 0);

        if (random) {
            checkPlaylistQueue();
            actualSong = rng.nextInt(playlist.getCount());
        }
        this.song = playlist.get(actualSong);

        start();
    }

    /**
     * @deprecated
     */
    SongPlayer(com.xxmicloxx.NoteBlockAPI.SongPlayer songPlayer) {
        oldSongPlayer = songPlayer;
        com.xxmicloxx.NoteBlockAPI.Song s = songPlayer.getSong();
        HashMap<Integer, Layer> layerHashMap = new HashMap<>();
        for (Integer i : s.getLayerHashMap().keySet()) {
            com.xxmicloxx.NoteBlockAPI.Layer l = s.getLayerHashMap().get(i);
            HashMap<Integer, Note> noteHashMap = new HashMap<>();
            for (Integer iL : l.getHashMap().keySet()) {
                com.xxmicloxx.NoteBlockAPI.Note note = l.getHashMap().get(iL);
                noteHashMap.put(iL, new Note(note.getInstrument(), note.getKey()));
            }
            Layer layer = new Layer();
            layer.setNotesAtTicks(noteHashMap);
            layer.setVolume(l.getVolume());
            layerHashMap.put(i, layer);
        }
        CustomInstrument[] instruments = new CustomInstrument[s.getCustomInstruments().length];
        for (int i = 0; i < s.getCustomInstruments().length; i++) {
            com.xxmicloxx.NoteBlockAPI.CustomInstrument ci = s.getCustomInstruments()[i];
            instruments[i] = new CustomInstrument(ci.getIndex(), ci.getName(), ci.getSoundfile());
        }
        song = new Song(s.getSpeed(),
                        layerHashMap,
                        s.getSongHeight(),
                        s.getLength(),
                        s.getTitle(),
                        s.getAuthor(),
                        s.getDescription(),
                        s.getPath(),
                        instruments);
        playlist = new Playlist(song);

        fadeIn = new Fade(FadeType.NONE, 60);
        fadeIn.setFadeStart((byte) 0);
        fadeIn.setFadeTarget(volume);

        fadeOut = new Fade(FadeType.NONE, 60);
        fadeOut.setFadeStart(volume);
        fadeOut.setFadeTarget((byte) 0);

        plugin = NoteBlockAPI.getAPI();
    }

    public void update(String key, Object value) {
        switch (key) {
            case "playing":
                playing = (boolean) value;
                break;
            case "fadeType":
                fadeIn.setType(FadeType.valueOf(((String) value).replace("FADE_", "")));
                break;
            case "fadeTarget":
                fadeIn.setFadeTarget((byte) value);
                break;
            case "fadeStart":
                fadeIn.setFadeStart((byte) value);
                break;
            case "fadeDuration":
                fadeIn.setFadeDuration((int) value);
                break;
            case "fadeDone":
                fadeIn.setFadeDone((int) value);
                break;
            case "tick":
                tick = (short) value;
                break;
            case "addplayer":
                addPlayer(((Player) value).getUuid(), false);
                break;
            case "removeplayer":
                removePlayer(((Player) value).getUuid(), false);
                break;
            case "autoDestroy":
                autoDestroy = (boolean) value;
                break;
            case "volume":
                volume = (byte) value;
                break;
            default:
                break;

        }
    }

    /**
     * Gets the FadeType for this SongPlayer (unused)
     *
     * @return FadeType
     * @deprecated returns fadeIn value
     */
    @Deprecated
    public FadeType getFadeType() {
        return fadeIn.getType();
    }

    /**
     * Sets the FadeType for this SongPlayer
     *
     * @deprecated set fadeIn value
     */
    @Deprecated
    public void setFadeType(FadeType fadeType) {
        fadeIn.setType(fadeType);
        CallUpdate("fadetype", "FADE_" + fadeType.name());
    }

    /**
     * Target volume for fade
     *
     * @return byte representing fade target
     * @deprecated returns fadeIn value
     */
    @Deprecated
    public byte getFadeTarget() {
        return fadeIn.getFadeTarget();
    }

    /**
     * Set target volume for fade
     *
     * @deprecated set fadeIn value
     */
    @Deprecated
    public void setFadeTarget(byte fadeTarget) {
        fadeIn.setFadeTarget(fadeTarget);
        CallUpdate("fadeTarget", fadeTarget);
    }

    /**
     * Gets the starting volume for the fade
     *
     * @deprecated returns fadeIn value
     */
    @Deprecated
    public byte getFadeStart() {
        return fadeIn.getFadeStart();
    }

    /**
     * Sets the starting volume for the fade
     *
     * @deprecated set fadeIn value
     */
    @Deprecated
    public void setFadeStart(byte fadeStart) {
        fadeIn.setFadeStart(fadeStart);
        CallUpdate("fadeStart", fadeStart);
    }

    /**
     * Gets the duration of the fade
     *
     * @return duration of the fade
     * @deprecated returns fadeIn value
     */
    @Deprecated
    public int getFadeDuration() {
        return fadeIn.getFadeDuration();
    }

    /**
     * Sets the duration of the fade
     *
     * @deprecated set fadeIn value
     */
    @Deprecated
    public void setFadeDuration(int fadeDuration) {
        fadeIn.setFadeDuration(fadeDuration);
        CallUpdate("fadeDuration", fadeDuration);
    }

    /**
     * Gets the tick when fade will be finished
     *
     * @return tick
     * @deprecated returns fadeIn value
     */
    @Deprecated
    public int getFadeDone() {
        return fadeIn.getFadeDone();
    }

    /**
     * Sets the tick when fade will be finished
     *
     * @deprecated set fadeIn value
     */
    @Deprecated
    public void setFadeDone(int fadeDone) {
        fadeIn.setFadeDone(fadeDone);
        CallUpdate("fadeDone", fadeDone);
    }

    /**
     * Check if 6 octave range is enabled
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnable10Octave() {
        return enable10Octave;
    }

    /**
     * Enable or disable 6 octave range
     * <p>
     * If not enabled, notes will be transposed to 2 octave range
     *
     * @param enable10Octave true if enabled, false otherwise
     */
    public void setEnable10Octave(boolean enable10Octave) {
        this.enable10Octave = enable10Octave;
    }

    /**
     * Starts this SongPlayer
     */
    private void start() {
        plugin.doAsync(() -> {
            while (!destroyed) {
                long startTime = System.currentTimeMillis();
                lock.lock();
                try {
                    if (destroyed || NoteBlockAPI.getAPI().isDisabling()) {
                        break;
                    }

                    if (playing || fading) {
                        if (fadeTemp != null) {
                            if (fadeTemp.isDone()) {
                                fadeTemp = null;
                                fading = false;
                                if (!playing) {
                                    SongStoppedEvent event = new SongStoppedEvent(this);
                                    plugin.doSync(() -> MinecraftServer.getGlobalEventHandler().call(event));
                                    volume = fadeIn.getFadeTarget();
                                    continue;
                                }
                            } else {
                                int fade = fadeTemp.calculateFade();
                                if (fade != -1) {
                                    volume = (byte) fade;
                                }
                            }
                        } else if (tick < fadeIn.getFadeDuration()) {
                            int fade = fadeIn.calculateFade();
                            if (fade != -1) {
                                volume = (byte) fade;
                            }
                            CallUpdate("fadeDone", fadeIn.getFadeDone());
                        } else if (tick >= song.getLength() - fadeOut.getFadeDuration()) {
                            int fade = fadeOut.calculateFade();
                            if (fade != -1) {
                                volume = (byte) fade;
                            }
                        }

                        tick++;
                        if (tick > song.getLength()) {
                            tick = -1;
                            fadeIn.setFadeDone(0);
                            CallUpdate("fadeDone", fadeIn.getFadeDone());
                            fadeOut.setFadeDone(0);
                            volume = fadeIn.getFadeTarget();
                            if (repeat == RepeatMode.ONE) {
                                SongLoopEvent event = new SongLoopEvent(this);
                                plugin.doSync(() -> MinecraftServer.getGlobalEventHandler().call(event));

                                if (!event.isCancelled()) {
                                    continue;
                                }
                            } else {
                                if (random) {
                                    songQueue.put(song, true);
                                    checkPlaylistQueue();
                                    ArrayList<Song> left = new ArrayList<>();
                                    for (Song s : songQueue.keySet()) {
                                        if (!songQueue.get(s)) {
                                            left.add(s);
                                        }
                                    }

                                    if (left.isEmpty()) {
                                        left.addAll(songQueue.keySet());
                                        songQueue.replaceAll((s, v) -> false);
                                        song = left.get(rng.nextInt(left.size()));
                                        actualSong = playlist.getIndex(song);
                                        CallUpdate("song", song);
                                        if (repeat == RepeatMode.ALL) {
                                            SongLoopEvent event = new SongLoopEvent(this);
                                            plugin.doSync(() -> MinecraftServer.getGlobalEventHandler().call(event));

                                            if (!event.isCancelled()) {
                                                continue;
                                            }
                                        }
                                    } else {
                                        song = left.get(rng.nextInt(left.size()));
                                        actualSong = playlist.getIndex(song);

                                        CallUpdate("song", song);
                                        SongNextEvent event = new SongNextEvent(this);
                                        plugin.doSync(() -> MinecraftServer.getGlobalEventHandler().call(event));
                                        continue;
                                    }
                                } else {
                                    if (playlist.hasNext(actualSong)) {
                                        actualSong++;
                                        song = playlist.get(actualSong);
                                        CallUpdate("song", song);
                                        SongNextEvent event = new SongNextEvent(this);
                                        plugin.doSync(() -> MinecraftServer.getGlobalEventHandler().call(event));
                                        continue;
                                    } else {
                                        actualSong = 0;
                                        song = playlist.get(actualSong);
                                        CallUpdate("song", song);
                                        if (repeat == RepeatMode.ALL) {
                                            SongLoopEvent event = new SongLoopEvent(this);
                                            plugin.doSync(() -> MinecraftServer.getGlobalEventHandler().call(event));

                                            if (!event.isCancelled()) {
                                                continue;
                                            }
                                        }
                                    }
                                }
                            }
                            playing = false;
                            SongEndEvent event = new SongEndEvent(this);
                            plugin.doSync(() -> MinecraftServer.getGlobalEventHandler().call(event));
                            if (autoDestroy) {
                                destroy();
                            }
                            continue;
                        }
                        CallUpdate("tick", tick);

                        plugin.doSync(() -> {
                            try {
                                for (UUID uuid : playerList.keySet()) {
                                    Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);
                                    if (player == null) {
                                        // offline...
                                        continue;
                                    }
                                    playTick(player, tick);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

                if (destroyed) {
                    break;
                }

                long duration = System.currentTimeMillis() - startTime;
                float delayMillis = song.getDelay() * 50;
                if (duration < delayMillis) {
                    try {
                        Thread.sleep((long) (delayMillis - duration));
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }
            }
        });
    }

    private void checkPlaylistQueue() {
        for (Song s : songQueue.keySet()) {
            if (!playlist.contains(s)) {
                songQueue.remove(s);
            }
        }

        for (Song s : playlist.getSongList()) {
            if (!songQueue.containsKey(s)) {
                songQueue.put(s, false);
            }
        }
    }

    /**
     * Returns {@link Fade} for Fade in effect
     *
     * @return Fade
     */
    public Fade getFadeIn() {
        return fadeIn;
    }

    /**
     * Returns {@link Fade} for Fade out effect
     *
     * @return Fade
     */
    public Fade getFadeOut() {
        return fadeOut;
    }

    /**
     * Gets list of current Player UUIDs listening to this SongPlayer
     *
     * @return set of Player UUIDs
     */
    public Set<UUID> getPlayerUUIDs() {
        Set<UUID> uuids = new HashSet<>(playerList.keySet());
        return Collections.unmodifiableSet(uuids);
    }

    /**
     * Adds a Player to the list of Players listening to this SongPlayer
     */
    public void addPlayer(Player player) {
        addPlayer(player.getUuid());
    }

    /**
     * Adds a Player to the list of Players listening to this SongPlayer
     *
     * @param player's uuid
     */
    public void addPlayer(UUID player) {
        addPlayer(player, true);
    }

    private void addPlayer(UUID player, boolean notify) {
        lock.lock();
        try {
            if (!playerList.containsKey(player)) {
                playerList.put(player, false);
                ArrayList<SongPlayer> songs = NoteBlockAPI.getSongPlayersByPlayer(player);
                if (songs == null) {
                    songs = new ArrayList<>();
                }
                songs.add(this);
                NoteBlockAPI.setSongPlayersByPlayer(player, songs);
                if (notify) {
                    Player p = MinecraftServer.getConnectionManager().getPlayer(player);
                    if (p != null) {
                        CallUpdate("addplayer", p);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns whether the SongPlayer is set to destroy itself when no one is listening
     * or when the Song ends
     *
     * @return if autoDestroy is enabled
     */
    public boolean getAutoDestroy() {
        lock.lock();
        try {
            return autoDestroy;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets whether the SongPlayer is going to destroy itself when no one is listening
     * or when the Song ends
     *
     * @param autoDestroy if autoDestroy is enabled
     */
    public void setAutoDestroy(boolean autoDestroy) {
        lock.lock();
        try {
            this.autoDestroy = autoDestroy;
            CallUpdate("autoDestroy", autoDestroy);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Plays the Song for the specific player
     *
     * @param player to play this SongPlayer for
     * @param tick   to play at
     */
    public abstract void playTick(Player player, int tick);

    /**
     * SongPlayer will destroy itself
     */
    public void destroy() {
        lock.lock();
        try {
            SongDestroyingEvent event = new SongDestroyingEvent(this);
            plugin.doSync(() -> MinecraftServer.getGlobalEventHandler().call(event));
            //Bukkit.getScheduler().cancelTask(threadId);
            if (event.isCancelled()) {
                return;
            }
            destroyed = true;
            playing = false;
            setTick((short) -1);
            CallUpdate("destroyed", destroyed);
            CallUpdate("playing", playing);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns whether the SongPlayer is actively playing
     *
     * @return is playing
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Sets whether the SongPlayer is playing
     */
    public void setPlaying(boolean playing) {
        setPlaying(playing, null);
    }

    /**
     * Sets whether the SongPlayer is playing and whether it should fade if previous value was different
     */
    public void setPlaying(boolean playing, boolean fade) {
        setPlaying(playing, fade ? playing ? fadeIn : fadeOut : null);
    }

    public void setPlaying(boolean playing, Fade fade) {
        if (this.playing == playing) return;

        this.playing = playing;
        if (fade != null && fade.getType() != FadeType.NONE) {
            fadeTemp = new Fade(fade.getType(), fade.getFadeDuration());
            fadeTemp.setFadeStart(playing ? 0 : volume);
            fadeTemp.setFadeTarget(playing ? volume : 0);
            fading = true;
        } else {
            fading = false;
            fadeTemp = null;
            volume = fadeIn.getFadeTarget();
            if (!playing) {
                SongStoppedEvent event = new SongStoppedEvent(this);
                plugin.doSync(() -> MinecraftServer.getGlobalEventHandler().call(event));
            }
        }

        CallUpdate("playing", playing);
    }

    /**
     * Gets the current tick of this SongPlayer
     */
    public short getTick() {
        return tick;
    }

    /**
     * Sets the current tick of this SongPlayer
     */
    public void setTick(short tick) {
        this.tick = tick;
        CallUpdate("tick", tick);
    }

    /**
     * Removes a player from this SongPlayer
     *
     * @param player to remove
     */
    public void removePlayer(Player player) {
        removePlayer(player.getUuid());
    }

    /**
     * Removes a player from this SongPlayer
     *
     * @param uuid of player to remove
     */
    public void removePlayer(UUID uuid) {
        removePlayer(uuid, true);
    }

    private void removePlayer(UUID player, boolean notify) {
        lock.lock();
        try {
            if (notify) {
                Player p = MinecraftServer.getConnectionManager().getPlayer(player);
                if (p != null) {
                    CallUpdate("removeplayer", p);
                }
            }
            playerList.remove(player);
            if (NoteBlockAPI.getSongPlayersByPlayer(player) == null) {
                return;
            }
            ArrayList<SongPlayer> songs = new ArrayList<>(
                    NoteBlockAPI.getSongPlayersByPlayer(player));
            songs.remove(this);
            NoteBlockAPI.setSongPlayersByPlayer(player, songs);
            if (playerList.isEmpty() && autoDestroy) {
                SongEndEvent event = new SongEndEvent(this);
                plugin.doSync(() -> MinecraftServer.getGlobalEventHandler().call(event));
                destroy();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the current volume of this SongPlayer
     *
     * @return volume (0-100)
     */
    public byte getVolume() {
        return volume;
    }

    /**
     * Sets the current volume of this SongPlayer
     *
     * @param volume (0-100)
     */
    public void setVolume(byte volume) {
        if (volume > 100) {
            volume = 100;
        } else if (volume < 0) {
            volume = 0;
        }
        this.volume = volume;

        fadeIn.setFadeTarget(volume);
        fadeOut.setFadeStart(volume);
        if (fadeTemp != null) {
            if (playing) fadeTemp.setFadeTarget(volume);
            else fadeTemp.setFadeStart(volume);
        }

        CallUpdate("volume", volume);
    }

    /**
     * Gets the Song being played by this SongPlayer
     */
    public Song getSong() {
        return song;
    }

    /**
     * Gets the Playlist being played by this SongPlayer
     */
    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * Sets the Playlist being played by this SongPlayer. Will affect next Song
     */
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * Get index of actually played {@link Song} in {@link Playlist}
     */
    public int getPlayedSongIndex() {
        return actualSong;
    }

    /**
     * Start playing {@link Song} at specified index in {@link Playlist}
     * If there is no {@link Song} at this index, {@link SongPlayer} will continue playing current song
     */
    public void playSong(int index) {
        lock.lock();
        try {
            if (playlist.exist(index)) {
                song = playlist.get(index);
                actualSong = index;
                tick = -1;
                fadeIn.setFadeDone(0);
                fadeOut.setFadeDone(0);
                CallUpdate("song", song);
                CallUpdate("fadeDone", fadeIn.getFadeDone());
                CallUpdate("tick", tick);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Start playing {@link Song} that is next in {@link Playlist} or random {@link Song} from {@link Playlist}
     */
    public void playNextSong() {
        lock.lock();
        try {
            tick = song.getLength();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the SoundCategory of this SongPlayer
     *
     * @return SoundCategory of this SongPlayer
     * @see SoundCategory
     */
    public Sound.Source getCategory() {
        return soundCategory;
    }

    /**
     * Sets the SoundCategory for this SongPlayer
     */
    public void setCategory(net.kyori.adventure.sound.Sound.Source soundCategory) {
        this.soundCategory = soundCategory;
        CallUpdate("soundCategory", soundCategory.name());
    }

    /**
     * Gets whether the SongPlayer will loop
     *
     * @return is loop
     * @deprecated
     */
    public boolean isLoop() {
        return repeat == RepeatMode.ALL;
    }

    /**
     * Sets whether the SongPlayer will loop
     *
     * @deprecated
     */
    public void setLoop(boolean loop) {
        this.repeat = RepeatMode.ALL;
    }

    /**
     * Gets SongPlayer's {@link RepeatMode}
     */
    public RepeatMode getRepeatMode() {
        return repeat;
    }

    /**
     * Sets SongPlayer's {@link RepeatMode}
     */
    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeat = repeatMode;
    }

    /**
     * Gets whether the SongPlayer will choose next song from player randomly
     *
     * @return is random
     */
    public boolean isRandom() {
        return random;
    }

    /**
     * Sets whether the SongPlayer will choose next song from player randomly
     */
    public void setRandom(boolean random) {
        this.random = random;
    }

    public ChannelMode getChannelMode() {
        return channelMode;
    }

    void CallUpdate(String key, Object value) {
        if (oldSongPlayer == null) {
            return;
        }
        try {
            oldSongPlayer.update(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    <T extends com.xxmicloxx.NoteBlockAPI.SongPlayer> void makeNewClone(Class<T> newClass) {
        try {
            Constructor<T> c = newClass.getDeclaredConstructor(SongPlayer.class);
            c.setAccessible(true);
            oldSongPlayer = (T) c.newInstance(new Object[]{this});
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

}
