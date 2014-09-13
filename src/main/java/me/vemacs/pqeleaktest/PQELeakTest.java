package me.vemacs.pqeleaktest;

import com.google.common.base.Joiner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class PQELeakTest extends JavaPlugin implements Listener {
    private Set<String> playersFromEvent = new HashSet<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        new BukkitRunnable() {
            @Override
            public void run() {
                Set<String> online = new HashSet<>();
                for (Player p : getServer().getOnlinePlayers())
                    online.add(p.getName());
                Set<String> diff = new HashSet<>(playersFromEvent);
                diff.removeAll(online);
                if (diff.size() > 0)
                    getLogger().info("Leaked: " + Joiner.on(", ").join(diff));
            }
        }.runTaskTimer(this, 0, 100);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        playersFromEvent.add(e.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        playersFromEvent.remove(e.getPlayer().getName());
    }
}