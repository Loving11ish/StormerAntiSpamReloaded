package me.loving11ish.stormerantispamreloaded.listeners;

import me.loving11ish.stormerantispamreloaded.StormerAntiSpamReloaded;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectEvent implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        StormerAntiSpamReloaded.getPlugin().messagesMap.remove(e.getPlayer().getUniqueId());
    }
}
