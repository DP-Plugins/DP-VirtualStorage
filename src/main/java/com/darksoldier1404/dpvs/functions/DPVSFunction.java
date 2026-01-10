package com.darksoldier1404.dpvs.functions;

import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import static com.darksoldier1404.dpvs.VirtualStorage.plugin;

public class DPVSFunction {
    public static void setDefaultStorageSlot(CommandSender sender, int slot) {
        plugin.defaultStorageSlot = slot;
        plugin.getConfig().set("Settings.defaultStorageSlot", slot);
        plugin.saveDataContainer();
        sender.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("command_defaultslot_set_success", String.valueOf(slot)));
    }

    @Nullable
    public static OfflinePlayer getOfflinePlayer(String name) {
        for (OfflinePlayer op : plugin.getServer().getOfflinePlayers()) {
            if (op.getName() != null && op.getName().equalsIgnoreCase(name)) {
                return op;
            }
        }
        return null;
    }

    public static void openCouponSettingGUI(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_common_player_only"));
            return;
        }
        Player p = (Player) sender;
        DInventory inv = new DInventory(plugin.getLang().get("gui_coupon_setting_title"), 27, plugin);
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(" ");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pane.setItemMeta(meta);
        pane = NBT.setStringTag(pane, "dppc_clickcancel", "true");
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, pane);
        }
        inv.setItem(13, plugin.config.getItemStack("Settings.couponItem"));
        inv.setChannel(101);
        inv.openInventory(p);
    }

    public static void saveCouponItem(ItemStack item) {
        plugin.getConfig().set("Settings.couponItem", item);
        plugin.saveDataContainer();
    }

    @Nullable
    public static ItemStack getCoupon(int slots) {
        ItemStack coupon = plugin.config.getItemStack("Settings.couponItem");
        if (coupon == null || coupon.getType().isAir()) {
            return null;
        }
        return NBT.setIntTag(coupon, "dpvs_couponslot", slots);
    }

    public static void giveCoupon(CommandSender sender, String sSlots) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_common_player_only"));
            return;
        }
        Player p = (Player) sender;
        int slots;
        try {
            slots = Integer.parseInt(sSlots);
            if (slots < 1) {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_defaultslot_invalid_slot"));
                return;
            }
        } catch (NumberFormatException e) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("command_common_invalid_number"));
            return;
        }
        ItemStack coupon = getCoupon(slots);
        if (coupon == null || coupon.getType().isAir()) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("coupon_not_configured"));
            return;
        }
        coupon = applyPlaceholder(coupon);
        p.getInventory().addItem(coupon);
        p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("coupon_given", String.valueOf(slots)));
    }

    public static ItemStack applyPlaceholder(ItemStack item) {
        if (item == null || item.getType().isAir()) return item;
        if (NBT.hasTagKey(item, "dpvs_couponslot")) {
            int slots = NBT.getIntegerTag(item, "dpvs_couponslot");
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String name = meta.getDisplayName();
                name = name.replace("%slots%", String.valueOf(slots));
                meta.setDisplayName(name);
                if (meta.hasLore()) {
                    for (int i = 0; i < meta.getLore().size(); i++) {
                        String lore = meta.getLore().get(i);
                        lore = lore.replace("%slots%", String.valueOf(slots));
                        meta.getLore().set(i, lore);
                    }
                }
                item.setItemMeta(meta);
            }
        }
        return item;
    }
}
