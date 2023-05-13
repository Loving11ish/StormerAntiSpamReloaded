package me.loving11ish.stormerantispamreloaded.commands;

import me.loving11ish.stormerantispamreloaded.StormerAntiSpamReloaded;
import me.loving11ish.stormerantispamreloaded.messages.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AntiSpamCommand implements CommandExecutor, TabCompleter {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !((Player)sender).isOp()) {
            return false;
        } else {
            String sint;
            if (command.getName().equalsIgnoreCase("antispam")) {
                if (args.length == 0) {
                    Message.normal(sender, "Help for Antispam command : ");
                    Message.normal(sender, "Updates the corresponding config parameter");
                    Message.normal(sender, "Usage : Antispam <messageCooldownthreshold/messageCooldownTickCooloff>");
                    return true;
                } else {
                    int value;
                    if (args[0].equalsIgnoreCase("messageCooldownthreshold")) {
                        if (args.length == 1) {
                            Message.normal(sender, "Current calue : " + StormerAntiSpamReloaded.getPlugin().messageCooldownthreshold);
                            return true;
                        } else {
                            sint = args[1];

                            try {
                                value = Integer.parseInt(sint);
                                StormerAntiSpamReloaded.getPlugin().getConfig().set("messageCooldownthreshold", value);
                                StormerAntiSpamReloaded.getPlugin().loadConfig();
                                Message.normal(sender, "New value : " + value);
                                return true;
                            } catch (Exception var9) {
                                Message.error(sender, "Expected a number, was given (" + sint + ")");
                                return false;
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("messageCooldownTickCooloff")) {
                        if (args.length == 1) {
                            Message.normal(sender, "Current calue : " + StormerAntiSpamReloaded.getPlugin().messageCooldownCooloff);
                            return true;
                        } else {
                            sint = args[1];

                            try {
                                value = Integer.parseInt(sint);
                                StormerAntiSpamReloaded.getPlugin().getConfig().set("messageCooldownTickCooloff", value);
                                StormerAntiSpamReloaded.getPlugin().loadConfig();
                                Message.normal(sender, "New value : " + value);
                                return true;
                            } catch (Exception var10) {
                                Message.error(sender, "Expected a number, was given (" + sint + ")");
                                return false;
                            }
                        }
                    } else {
                        Message.error(sender, "Error, invalid subcommand (" + args[0] + ")");
                        Message.normal(sender, "Valid commands : ");
                        Message.normal(sender, "messageCooldownthreshold");
                        Message.normal(sender, "messageCooldownTickCooloff");
                        return false;
                    }
                }
            } else {
                Player p;
                if (command.getName().equalsIgnoreCase("mute")) {
                    if (args.length == 0) {
                        Message.error(sender, "you need to specify a player to mute");
                        return false;
                    } else {
                        sint = args[0];
                        p = Bukkit.getPlayer(sint);
                        if (p == null) {
                            Message.error(sender, "no player with such name");
                            return false;
                        } else if (p.isOp()) {
                            Message.error(sender, "you cannot mute that player");
                            return false;
                        } else if (args.length == 1) {
                            Message.error(sender, "missing argument \"time\", expecting a number");
                            return false;
                        } else {
                            int time;
                            try {
                                time = Integer.parseInt(args[1]);
                            } catch (Exception var11) {
                                Message.error(sender, "invalid argument (" + args[1] + "), expected a number");
                                return false;
                            }

                            StormerAntiSpamReloaded.Unit unit = StormerAntiSpamReloaded.Unit.Seconds;
                            if (args.length != 2) {
                                unit = StormerAntiSpamReloaded.Unit.parse(args[2]);
                                if (unit == StormerAntiSpamReloaded.Unit.Invalid) {
                                    Message.error(sender, "invalid unit (" + args[2] + "), expected a time unit (t,s,m,h,d)");
                                    return false;
                                }
                            }

                            StormerAntiSpamReloaded.getPlugin().muted.put(p.getUniqueId(), time * unit.multiplier);
                            Message.normal(sender, "Muted " + p.getName() + " for " + time + " " + unit.name());
                            Message.normal(p, (String)StormerAntiSpamReloaded.getPlugin().mutedMessagePool.get((new Random()).nextInt(StormerAntiSpamReloaded.getPlugin().mutedMessagePool.size())));
                            return true;
                        }
                    }
                } else if (command.getName().equalsIgnoreCase("unmute")) {
                    if (args.length == 0) {
                        Message.error(sender, "you need to specify a player to unmute");
                        return false;
                    } else {
                        sint = args[0];
                        p = Bukkit.getPlayer(sint);
                        if (p == null) {
                            Message.error(sender, "no player with such name");
                            return false;
                        } else if (!StormerAntiSpamReloaded.getPlugin().muted.containsKey(p.getUniqueId())) {
                            Message.error(sender, "This player is already unmuted");
                            return false;
                        } else {
                            StormerAntiSpamReloaded.getPlugin().muted.remove(p.getUniqueId());
                            Message.error(sender, "Unmuted " + p.getName());
                            Message.normal(p, (String)StormerAntiSpamReloaded.getPlugin().unmutedMessagePool.get((new Random()).nextInt(StormerAntiSpamReloaded.getPlugin().unmutedMessagePool.size())));
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> list = new ArrayList();
        if (sender instanceof Player && !((Player)sender).isOp()) {
            return list;
        } else {
            String s = "";
            int depth = 0;
            if (cmd.getName().equalsIgnoreCase("antispam")) {
                s = "messageCooldownthreshold";
                if (args.length == depth || args.length == depth + 1 && s.toLowerCase().startsWith(args[depth].toLowerCase())) {
                    list.add(s);
                }

                if (args.length > depth + 1 && args[depth].equalsIgnoreCase(s)) {
                    ++depth;
                    s = String.valueOf(StormerAntiSpamReloaded.getPlugin().getConfig().getInt(s));
                    if (args.length == depth || args.length == depth + 1 && s.toLowerCase().startsWith(args[depth].toLowerCase())) {
                        list.add(s);
                    }

                    --depth;
                }

                s = "messageCooldownCooloff";
                if (args.length == depth || args.length == depth + 1 && s.toLowerCase().startsWith(args[depth].toLowerCase())) {
                    list.add(s);
                }

                if (args.length > depth + 1 && args[depth].equalsIgnoreCase(s)) {
                    ++depth;
                    s = String.valueOf(StormerAntiSpamReloaded.getPlugin().getConfig().getInt(s));
                    if (args.length == depth || args.length == depth + 1 && s.toLowerCase().startsWith(args[depth].toLowerCase())) {
                        list.add(s);
                    }

                    --depth;
                }
            }

            if (cmd.getName().equalsIgnoreCase("mute") || cmd.getName().equalsIgnoreCase("unmute")) {
                Player pls;
                for(Iterator var9 = Bukkit.getOnlinePlayers().iterator(); var9.hasNext(); s = pls.getName()) {
                    pls = (Player)var9.next();
                }

                if (args.length == depth || args.length == depth + 1 && s.toLowerCase().startsWith(args[depth].toLowerCase())) {
                    list.add(s);
                }

                if (args.length > depth + 1) {
                    ++depth;
                    s = "time";
                    if (args.length == depth || args.length == depth + 1 && s.toLowerCase().startsWith(args[depth].toLowerCase())) {
                        list.add(s);
                    }

                    if (args.length > depth + 1) {
                        ++depth;
                        StormerAntiSpamReloaded.Unit[] var11;
                        int var10 = (var11 = StormerAntiSpamReloaded.Unit.values()).length;
                        int var13 = 0;

                        while(true) {
                            if (var13 >= var10) {
                                --depth;
                                break;
                            }

                            StormerAntiSpamReloaded.Unit unit = var11[var13];
                            s = unit.name();
                            if (args.length == depth || args.length == depth + 1 && s.toLowerCase().startsWith(args[depth].toLowerCase())) {
                                list.add(s);
                            }

                            ++var13;
                        }
                    }

                    --depth;
                }
            }

            return list;
        }
    }
}
