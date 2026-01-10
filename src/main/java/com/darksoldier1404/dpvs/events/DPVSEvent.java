package com.darksoldier1404.dpvs.events;

import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.events.dinventory.DInventoryClickEvent;
import com.darksoldier1404.dppc.events.dinventory.DInventoryCloseEvent;
import com.darksoldier1404.dppc.utils.NBT;
import com.darksoldier1404.dpvs.functions.DPVSFunction;
import com.darksoldier1404.dpvs.obj.VUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static com.darksoldier1404.dpvs.VirtualStorage.plugin;

public class DPVSEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!plugin.udata.containsKey(p.getUniqueId())) {
            VUser data = new VUser();
            data.setUUID(p.getUniqueId());
            DInventory inv = new DInventory(plugin.getLang().get("inventory_storage_title"), 54, true, true, plugin);
            inv.update();
            data.setInventory(inv);
            plugin.udata.put(p.getUniqueId(), data);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        plugin.udata.save(p.getUniqueId());
    }

    @EventHandler
    public void onInventoryClose(DInventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        DInventory inv = e.getDInventory();
        if (inv.isValidHandler(plugin)) {
            if (inv.isValidChannel(0)) { // user storage save
                inv.applyChanges();
                VUser user = plugin.udata.get(p.getUniqueId());
                user.setInventory(inv);
                plugin.udata.put(p.getUniqueId(), user);
                plugin.udata.save(p.getUniqueId());
                return;
            }
            if (inv.isValidChannel(1)) { // admin lookup save
                inv.applyChanges();
                if (inv.getObj() != null) {
                    VUser user = plugin.udata.get((UUID) inv.getObj());
                    user.setInventory(inv);
                    plugin.udata.put((UUID) inv.getObj(), user);
                    plugin.udata.save((UUID) inv.getObj());
                }
                return;
            }
            if (inv.isValidChannel(101)) {
                inv.applyChanges();
                DPVSFunction.saveCouponItem(inv.getItem(13));
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("coupon_item_saved"));
                return;
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        ItemStack item = e.getItem();
        if (item == null || item.getType().isAir()) return;
        Player p = e.getPlayer();
        if (NBT.hasTagKey(item, "dpvs_couponslot")) {
            e.setCancelled(true);
            int slots = NBT.getIntegerTag(item, "dpvs_couponslot");
            if (slots < 1) {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("coupon_invalid_slots"));
                return;
            }
            VUser user = plugin.udata.get(p.getUniqueId());
            boolean success = user.addStorageSlots(slots);
            if (success) {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("coupon_used_increase_slots", String.valueOf(slots)));
                item.setAmount(item.getAmount() - 1);
                plugin.udata.put(p.getUniqueId(), user);
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("coupon_cannot_increase_more"));
            }
        }
    }

    @EventHandler
    public void onInventoryClick(DInventoryClickEvent e) {
        DInventory inv = e.getDInventory();
        if (inv.isValidHandler(plugin)) {
            if (inv.isValidChannel(0) || inv.isValidChannel(1)) {
                if (e.getCurrentItem() != null && NBT.hasTagKey(e.getCurrentItem(), "dpvs_barrier")) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
