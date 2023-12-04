package net.defekt.racuszki.noteblocklayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public final class TestServer {
    private TestServer() {
    }

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();

        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setGenerator(mod -> mod.modifier().fillHeight(0, 1, Block.GRASS_BLOCK));

        MinecraftServer.getCommandManager().register(new Command("song") {
            {
                ArgumentString arg = ArgumentType.String("url");
                setDefaultExecutor((sender, ctx) -> sender.sendMessage("Usage: /song <url>"));

                addSyntax((sender, ctx) -> {
                    if (sender instanceof Player player)
                        try (InputStream is = new URL(ctx.get(arg)).openStream()) {
                            List<SongPlayer> players = NoteBlockAPI.getSongPlayersByPlayer(player);
                            if (players != null)
                                players.forEach(SongPlayer::destroy);
                            SongPlayer sp = new RadioSongPlayer(NBSDecoder.parse(is));
                            sp.addPlayer(player);
                            sp.playSong(0);
                            sp.setPlaying(true);
                            System.out.println(sp);
                        } catch (Exception e) {
                            sender.sendMessage(e.toString());
                            sender.sendMessage("Check console for errors");
                        }
                }, arg);
            }
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, e -> {
            e.setSpawningInstance(instance);
            e.getPlayer().setGameMode(GameMode.CREATIVE);
            e.getPlayer().setRespawnPoint(new Pos(0, 5, 0));
            e.getPlayer().sendMessage("§fUse §n/song <url>§r to play a song from an url.");
        });
        server.start("localhost", 25565);
    }
}
