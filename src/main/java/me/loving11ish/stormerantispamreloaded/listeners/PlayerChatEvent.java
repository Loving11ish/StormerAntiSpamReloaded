package me.loving11ish.stormerantispamreloaded.listeners;

import me.loving11ish.stormerantispamreloaded.StormerAntiSpamReloaded;
import me.loving11ish.stormerantispamreloaded.messages.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PlayerChatEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(final AsyncPlayerChatEvent e) {
        if (!e.getPlayer().isOp()) {
            if (StormerAntiSpamReloaded.getPlugin().muted.containsKey(e.getPlayer().getUniqueId())) {
                e.setCancelled(true);
                Message.normal(e.getPlayer(), (String)StormerAntiSpamReloaded.getPlugin().mutedMessagePool.get((new Random()).nextInt(StormerAntiSpamReloaded.getPlugin().mutedMessagePool.size())));
            } else {
                if (!StormerAntiSpamReloaded.getPlugin().messagesMap.containsKey(e.getPlayer().getUniqueId())) {
                    StormerAntiSpamReloaded.getPlugin().messagesMap.put(e.getPlayer().getUniqueId(), 0);
                }

                StormerAntiSpamReloaded.getPlugin().messagesMap.put(e.getPlayer().getUniqueId(), (Integer)StormerAntiSpamReloaded.getPlugin().messagesMap.get(e.getPlayer().getUniqueId()) + 1);
                StormerAntiSpamReloaded.getPlugin().foliaLib.getImpl().runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (StormerAntiSpamReloaded.getPlugin().messagesMap.containsKey(e.getPlayer().getUniqueId())) {
                            StormerAntiSpamReloaded.getPlugin().messagesMap.put(e.getPlayer().getUniqueId(), (Integer)StormerAntiSpamReloaded.getPlugin().messagesMap.get(e.getPlayer().getUniqueId()) - 1);
                        }
                    }
                }, StormerAntiSpamReloaded.getPlugin().messageCooldownCooloff, TimeUnit.SECONDS);
                if ((Integer)StormerAntiSpamReloaded.getPlugin().messagesMap.get(e.getPlayer().getUniqueId()) > StormerAntiSpamReloaded.getPlugin().messageCooldownthreshold) {
                    e.setCancelled(true);
                    Message.normal(e.getPlayer(), (String)StormerAntiSpamReloaded.getPlugin().messagePool.get((new Random()).nextInt(StormerAntiSpamReloaded.getPlugin().messagePool.size())));
                }
            }
        }
    }
}
