package me.loving11ish.stormerantispamreloaded;

import com.rylinaux.plugman.api.PlugManAPI;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.WrappedTask;
import io.papermc.lib.PaperLib;
import me.loving11ish.stormerantispamreloaded.commands.AntiSpamCommand;
import me.loving11ish.stormerantispamreloaded.files.MessagesFileManager;
import me.loving11ish.stormerantispamreloaded.listeners.PlayerChatEvent;
import me.loving11ish.stormerantispamreloaded.listeners.PlayerDisconnectEvent;
import me.loving11ish.stormerantispamreloaded.messages.Message;
import me.loving11ish.stormerantispamreloaded.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class StormerAntiSpamReloaded extends JavaPlugin {

    Logger logger = this.getLogger();

    private final PluginDescriptionFile pluginInfo = getDescription();
    private final String pluginVersion = pluginInfo.getVersion();
    private static StormerAntiSpamReloaded plugin;
    private WrappedTask wrappedTask;

    public HashMap<UUID, Integer> muted = new HashMap();
    public int messageCooldownthreshold;
    public int messageCooldownCooloff;
    public HashMap<UUID, Integer> messagesMap = new HashMap();
    public List<String> messagePool = new ArrayList();
    public List<String> mutedMessagePool = new ArrayList();
    public List<String> unmutedMessagePool = new ArrayList();
    public FoliaLib foliaLib = new FoliaLib(this);
    public MessagesFileManager messagesFileManager;

    @Override
    public void onEnable() {
        //Plugin startup logic
        plugin = this;

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.13")||Bukkit.getServer().getVersion().contains("1.14")||
                Bukkit.getServer().getVersion().contains("1.15")||Bukkit.getServer().getVersion().contains("1.16")||
                Bukkit.getServer().getVersion().contains("1.17")||Bukkit.getServer().getVersion().contains("1.18")||
                Bukkit.getServer().getVersion().contains("1.19"))){
            logger.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &4This plugin is only supported on the Minecraft versions listed below:"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &41.13.x"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &41.14.x"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &41.15.x"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &41.16.x"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &41.17.x"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &41.18.x"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &41.19.x"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &4Is now disabling!"));
            logger.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
            logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &aA supported Minecraft version has been detected"));
            logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &6Continuing plugin startup"));
            logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
        }

        //Suggest PaperMC if not using
        if (foliaLib.isUnsupported()||foliaLib.isSpigot()){
            PaperLib.suggestPaper(this);
        }

        //Check if PlugManX is enabled
        if (isPlugManXEnabled()){
            if (!PlugManAPI.iDoNotWantToBeUnOrReloaded("StormerAntiSpamReloaded")){
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&4WARNING WARNING WARNING WARNING!"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &4You appear to be using an unsupported version of &d&lPlugManX"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &4Please &4&lDO NOT USE PLUGMANX TO LOAD/UNLOAD/RELOAD THIS PLUGIN!"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &4Please &4&lFULLY RESTART YOUR SERVER!"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &4This plugin &4&lHAS NOT &4been validated to use this version of PlugManX!"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &4&lNo official support will be given to you if you use this!"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &4&lUnless Loving11ish has explicitly agreed to help!"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &4Please add StormerAntiSpamReloaded to the ignored-plugins list in PlugManX's config.yml"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &6Continuing plugin startup"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
            }else {
                logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
                logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &aSuccessfully hooked into PlugManX"));
                logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &aSuccessfully added StormerAntiSpamReloaded to ignoredPlugins list."));
                logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &6Continuing plugin startup"));
                logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            }
        }else {
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &cPlugManX not found!"));
            logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &cDisabling PlugManX hook loader"));
            logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &6Continuing plugin startup"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

        //Load plugin config
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
        this.messageCooldownthreshold = this.getConfig().getInt("messageCooldownthreshold");
        this.messageCooldownCooloff = this.getConfig().getInt("messageCooldownCooloff");

        //Load messages.yml
        this.messagesFileManager = new MessagesFileManager();
        messagesFileManager.MessagesFileManager(this);

        //Register commands
        AntiSpamCommand antiSpamCommand = new AntiSpamCommand();
        this.getCommand("antispam").setExecutor(antiSpamCommand);
        this.getCommand("antispam").setTabCompleter(antiSpamCommand);
        this.getCommand("mute").setExecutor(antiSpamCommand);
        this.getCommand("unmute").setExecutor(antiSpamCommand);

        //Register listeners
        this.getServer().getPluginManager().registerEvents(new PlayerChatEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDisconnectEvent(), this);

        //Run delayed startup tasks
        this.getMessages();
        this.startLoop();

        //Final startup message
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &3Plugin by: &b&lLoving11ish & Stormer3428"));
        logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &3has been loaded successfully"));
        logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &3Plugin Version: &d&l" + pluginVersion));
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
    }

    @Override
    public void onDisable() {
        //Plugin shutdown logic

        //Cancel running tasks
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &3Plugin by: &b&lLoving11ish & Stormer3428"));
        try {
            this.wrappedTask.cancel();
            if (foliaLib.isUnsupported()){
                Bukkit.getScheduler().cancelTasks(this);
            }
            logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &3Background tasks have disabled successfully!"));
        }catch (Exception e){
            logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &3Background tasks have disabled successfully!"));
        }

        //Final plugin shutdown message
        logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &3has been shutdown successfully"));
        logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &3Plugin Version: &d&l" + pluginVersion));
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));

        //Clear any plugin remains
        this.messagesMap = null;
        this.messagesFileManager = null;
        this.messagePool = null;
        this.mutedMessagePool = null;
        this.unmutedMessagePool = null;
        this.muted = null;
        this.wrappedTask = null;
        this.foliaLib = null;
        this.logger = null;
        plugin = null;
    }

    public void getMessages() {
        foliaLib.getImpl().runLaterAsync(new Runnable() {
            @Override
            public void run() {
                List<String> configMessagePool = StormerAntiSpamReloaded.this.messagesFileManager.getMessagesConfig().getStringList("message-pool");
                List<String> configMutedMessagePool = StormerAntiSpamReloaded.this.messagesFileManager.getMessagesConfig().getStringList("muted-message-pool");
                List<String> configUnMutedMessagePool = StormerAntiSpamReloaded.this.messagesFileManager.getMessagesConfig().getStringList("unmuted-message-pool");
                StormerAntiSpamReloaded.this.messagePool.addAll(configMessagePool);
                StormerAntiSpamReloaded.this.mutedMessagePool.addAll(configMutedMessagePool);
                StormerAntiSpamReloaded.this.unmutedMessagePool.addAll(configUnMutedMessagePool);
            }
        }, 2L, TimeUnit.SECONDS);
    }

    private void startLoop() {
        wrappedTask = foliaLib.getImpl().runTimerAsync(new Runnable() {
            @Override
            public void run() {
                Iterator var2 = StormerAntiSpamReloaded.this.muted.keySet().iterator();

                while(var2.hasNext()) {
                    UUID p = (UUID)var2.next();
                    StormerAntiSpamReloaded.this.muted.put(p, (Integer)StormerAntiSpamReloaded.this.muted.get(p) - 1);
                    if ((Integer)StormerAntiSpamReloaded.this.muted.get(p) - 1 <= 0) {
                        StormerAntiSpamReloaded.this.muted.remove(p);
                        if (Bukkit.getPlayer(p) != null) {
                            Message.normal(Bukkit.getPlayer(p), (String)StormerAntiSpamReloaded.this.unmutedMessagePool.get((new Random()).nextInt(StormerAntiSpamReloaded.this.unmutedMessagePool.size())));
                        }
                    }
                }
            }
        }, 50L, 50L, TimeUnit.MILLISECONDS);
    }

    public void loadConfig() {
        this.getConfig().options().copyDefaults();
        this.saveConfig();
        this.messageCooldownthreshold = this.getConfig().getInt("messageCooldownthreshold");
        this.messageCooldownCooloff = this.getConfig().getInt("messageCooldownCooloff");
    }

    public enum Unit {
        Invalid(0),
        Ticks(1),
        Seconds(20),
        Minutes(1200),
        Hours(72000),
        Days(1728000);

        public int multiplier;

        Unit(int i) {
            this.multiplier = i;
        }

        public static Unit parse(String toParse) {
            StormerAntiSpamReloaded.Unit[] var4;
            int var3 = (var4 = values()).length;
            StormerAntiSpamReloaded.Unit unit;
            int var2;
            for(var2 = 0; var2 < var3; ++var2) {
                unit = var4[var2];
                if (unit.name().equals(toParse)) {
                    return unit;
                }
            }
            var3 = (var4 = values()).length;
            for(var2 = 0; var2 < var3; ++var2) {
                unit = var4[var2];
                if (unit.name().equalsIgnoreCase(toParse)) {
                    return unit;
                }
            }
            if (toParse.length() != 1) {
                return Invalid;
            } else {
                var3 = (var4 = values()).length;
                for(var2 = 0; var2 < var3; ++var2) {
                    unit = var4[var2];
                    if (unit.name().toCharArray()[0] == toParse.toCharArray()[0]) {
                        return unit;
                    }
                }
                var3 = (var4 = values()).length;
                for(var2 = 0; var2 < var3; ++var2) {
                    unit = var4[var2];
                    if (unit.name().toLowerCase().toCharArray()[0] == toParse.toLowerCase().toCharArray()[0]) {
                        return unit;
                    }
                }
                return Invalid;
            }
        }
    }

    public boolean isPlugManXEnabled() {
        try {
            Class.forName("com.rylinaux.plugman.PlugMan");
            logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &aFound PlugManX main class at:"));
            logger.info(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &dcom.rylinaux.plugman.PlugMan"));
            return true;
        } catch (ClassNotFoundException e) {
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &aCould not find PlugManX main class at:"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerAntiSpamReloaded: &dcom.rylinaux.plugman.PlugMan"));
            return false;
        }
    }

    public static StormerAntiSpamReloaded getPlugin() {
        return plugin;
    }
}
