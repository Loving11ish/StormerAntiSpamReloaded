package me.loving11ish.stormerantispamreloaded.messages;

import me.loving11ish.stormerantispamreloaded.StormerAntiSpamReloaded;
import me.loving11ish.stormerantispamreloaded.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class Message {

    private static String PLUGIN_PREFIX = StormerAntiSpamReloaded.getPlugin().messagesFileManager.getMessagesConfig().getString("plugin-prefix");
    private static final String a;
    private static final String b;

    static {
        a = "&0";
        b = "&c";
    }

    public Message() {
    }

    public static void normal(CommandSender p, String strg) {
        String m = PLUGIN_PREFIX + " " + strg;
        p.sendMessage(ColorUtils.translateColorCodes(m));
    }

    public static void normal(String strg) {
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();

        while(var2.hasNext()) {
            Player p = (Player)var2.next();
            normal((CommandSender)p, (String)ColorUtils.translateColorCodes(strg));
        }

    }

    public static void normal(String strg, List<String> p) {
        Iterator var3 = Bukkit.getOnlinePlayers().iterator();

        while(var3.hasNext()) {
            Player pls = (Player)var3.next();
            if (p.contains(pls.getName())) {
                normal((CommandSender)pls, (String)ColorUtils.translateColorCodes(strg));
            }
        }

    }

    public static void error(CommandSender p, String strg) {
        String m = a + "[" + b + "Error" + a + "] " + ChatColor.RED + strg;
        p.sendMessage(ColorUtils.translateColorCodes(m));
    }

    public static void error(String strg) {
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();

        while(var2.hasNext()) {
            Player p = (Player)var2.next();
            error((CommandSender)p, (String)ColorUtils.translateColorCodes(strg));
        }

    }

    public static void error(String strg, List<String> p) {
        Iterator var3 = Bukkit.getOnlinePlayers().iterator();

        while(var3.hasNext()) {
            Player pls = (Player)var3.next();
            if (p.contains(pls.getName())) {
                error((CommandSender)pls, (String)ColorUtils.translateColorCodes(strg));
            }
        }

    }

    public static void systemNormal(String strg) {
        String m = PLUGIN_PREFIX + " " + strg;
        Bukkit.getConsoleSender().sendMessage(ColorUtils.translateColorCodes(m));
    }

    public static void systemError(String strg) {
        String m = PLUGIN_PREFIX + " " + a + "[" + b + "Error" + a + "] " + ChatColor.RED + strg;
        Bukkit.getConsoleSender().sendMessage(ColorUtils.translateColorCodes(m));
    }
}
