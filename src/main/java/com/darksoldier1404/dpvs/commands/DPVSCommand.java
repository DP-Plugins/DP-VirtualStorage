package com.darksoldier1404.dpvs.commands;

import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import com.darksoldier1404.dpvs.functions.DPVSFunction;
import com.darksoldier1404.dpvs.obj.VUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static com.darksoldier1404.dpvs.VirtualStorage.plugin;

public class DPVSCommand {
    private final CommandBuilder builder;

    public DPVSCommand() {
        builder = new CommandBuilder(plugin);

        builder.addSubCommand("defaultslot", "dpvs.admin", plugin.getLang().get("command_usage_defaultslot"), false, (p, args) -> {
            if (args.length == 2) {
                try {
                    int slot = Integer.parseInt(args[1]);
                    if (slot < 1) {
                        p.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_defaultslot_invalid_slot"));
                        return true;
                    }
                    plugin.defaultStorageSlot = slot;
                    plugin.getConfig().set("Settings.defaultStorageSlot", slot);
                    plugin.saveDataContainer();
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("command_defaultslot_set_success", String.valueOf(slot)));
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return false;
        });

        builder.addSubCommand("open", "dpvs.open", plugin.getLang().get("command_usage_open"), true, (p, args) -> {
            if (!(p instanceof Player)) {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_common_player_only"));
                return true;
            }
            if (args.length == 1) {
                Player player = (Player) p;
                VUser user = plugin.udata.get(player.getUniqueId());
                user.openInventory(player, false);
                return true;
            }
            return false;
        });

        builder.addSubCommand("lookup", "dpvs.admin", plugin.getLang().get("command_usage_lookup"), true, (p, args) -> {
            if (!(p instanceof Player)) {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_common_player_only"));
                return true;
            }
            if (args.length == 2) {
                OfflinePlayer target = DPVSFunction.getOfflinePlayer(args[1]);
                if (target == null) {
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_lookup_player_not_found"));
                    return true;
                }
                Player player = (Player) p;
                VUser user = plugin.udata.get(target.getUniqueId());
                if (user == null) {
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_lookup_data_not_found"));
                    return true;
                }
                user.openInventory(player, true);
                return true;
            }
            return false;
        });

        // set coupon item
        builder.addSubCommand("setcoupon", "dpvs.admin", plugin.getLang().get("command_usage_setcoupon"), true, (p, args) -> {
            if (args.length == 1) {
                if (!(p instanceof Player)) {
                    p.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_common_player_only"));
                    return true;
                }
                Player player = (Player) p;
                DPVSFunction.openCouponSettingGUI(player);
                return true;
            }
            return false;
        });

        // give coupon
        builder.addSubCommand("givecoupon", "dpvs.admin", plugin.getLang().get("command_usage_givecoupon"), true, (p, args) -> {
            if (args.length == 2) {
                DPVSFunction.giveCoupon(p, args[1]);
                return true;
            }
            return false;
        });

        builder.addSubCommand("reload", "dpvs.admin", plugin.getLang().get("command_usage_reload"), false, (p, args) -> {
            if (args.length == 1) {
                plugin.reload();
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_reload_success"));
                return true;
            }
            return false;
        });

        for (String c : builder.getSubCommandNames()) {
            builder.addTabCompletion(c, (sender, args) -> {
                if (c.equals("lookup")) {
                    if (args.length == 2) {
                        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                    }
                }
                return null;
            });
        }
    }

    public CommandBuilder getBuilder() {
        return builder;
    }
}
