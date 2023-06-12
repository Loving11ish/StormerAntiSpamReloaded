package me.loving11ish.stormerantispamreloaded.commands;

import com.tcoded.folialib.FoliaLib;
import me.loving11ish.stormerantispamreloaded.StormerAntiSpamReloaded;
import me.loving11ish.stormerantispamreloaded.messages.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AntiSpamReloadCommand implements CommandExecutor {

    Logger logger = StormerAntiSpamReloaded.getPlugin().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player){
            if (player.hasPermission("antispam.command.reload")||player.hasPermission("antispam.command.*")
                    ||player.hasPermission("antispam.*")||player.isOp()){
                Message.normal("&aBeginning plugin reload...");
                FoliaLib foliaLib = StormerAntiSpamReloaded.getPlugin().foliaLib;
                StormerAntiSpamReloaded.getPlugin().onDisable();
                foliaLib.getImpl().runLater(new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getPluginManager().getPlugin("StormerAntiSpamReloaded").onEnable();
                    }
                }, 5L, TimeUnit.SECONDS);
                foliaLib.getImpl().runLater(new Runnable() {
                    @Override
                    public void run() {
                        StormerAntiSpamReloaded.getPlugin().reloadConfig();
                        StormerAntiSpamReloaded.getPlugin().messagesFileManager.reloadMessagesConfig();
                        Message.normal("&aPlugin reload complete!");
                    }
                }, 5L, TimeUnit.SECONDS);
            }
        }else if (sender instanceof ConsoleCommandSender){
            logger.info("&6StormerAntiSpamReloaded: &aBeginning plugin reload...");
            FoliaLib foliaLib = StormerAntiSpamReloaded.getPlugin().foliaLib;
            StormerAntiSpamReloaded.getPlugin().onDisable();
            foliaLib.getImpl().runLater(new Runnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().getPlugin("StormerAntiSpamReloaded").onEnable();
                }
            }, 5L, TimeUnit.SECONDS);
            foliaLib.getImpl().runLater(new Runnable() {
                @Override
                public void run() {
                    StormerAntiSpamReloaded.getPlugin().reloadConfig();
                    StormerAntiSpamReloaded.getPlugin().messagesFileManager.reloadMessagesConfig();
                    logger.info("&6StormerAntiSpamReloaded: &aPlugin reload complete!");
                }
            }, 5L, TimeUnit.SECONDS);
        }
        return true;
    }
}
