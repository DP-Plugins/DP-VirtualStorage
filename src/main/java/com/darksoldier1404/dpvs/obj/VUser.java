package com.darksoldier1404.dpvs.obj;

import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.data.DataCargo;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

import static com.darksoldier1404.dpvs.VirtualStorage.plugin;

public class VUser implements DataCargo {
    private UUID uuid;
    private int maxStorageSlot;
    private DInventory inventory;

    public VUser() {
    }

    public VUser(UUID uuid, int maxStorageSlot, DInventory inventory) {
        this.uuid = uuid;
        this.maxStorageSlot = maxStorageSlot;
        this.inventory = inventory;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public int getMaxStorageSlot() {
        return maxStorageSlot;
    }

    public void setMaxStorageSlot(int maxStorageSlot) {
        this.maxStorageSlot = maxStorageSlot;
    }

    public DInventory getInventory() {
        return inventory;
    }

    public void setInventory(DInventory inventory) {
        this.inventory = inventory;
    }

    public void openInventory(Player p, boolean isLookup) {
        int availableSlots = maxStorageSlot + plugin.defaultStorageSlot;
        int pages = (int) Math.ceil((double) availableSlots / 45.0);
        inventory.setPages(pages - 1);
        inventory.setCurrentPage(0);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta im = barrier.getItemMeta();
        im.setDisplayName("§c잠김");
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        barrier.setItemMeta(im);
        barrier = NBT.setStringTag(barrier, "dpvs_barrier", "true");

        inventory.applyAllItemChanges((pi) -> {
            if (NBT.hasTagKey(pi.getItem(), "dpvs_barrier")) {
                inventory.setPageItem(pi.getPage(), pi.getSlot(), null);
            }
        });

        int skipPage = availableSlots / 45;
        int skipSlot = availableSlots % 45;

        Map<Integer, ItemStack[]> pageItems = inventory.getPageItems();
        for (int page = skipPage; page < pages + 1; page++) {
            int startSlot = (page == skipPage) ? skipSlot : 0;
            for (int slot = startSlot; slot < 45; slot++) {
                if (pageItems.containsKey(page)) {
                    inventory.setPageItem(page, slot, barrier);
                }
            }
        }
        inventory.setPageItems(pageItems);
        inventory.update();
        inventory.applyChanges();
        if (isLookup) {
            inventory.setObj(uuid);
            inventory.setChannel(1);
        } else {
            inventory.setObj(null);
            inventory.setChannel(0);
        }
        inventory.openInventory(p);
    }

    @Override
    public YamlConfiguration serialize() {
        YamlConfiguration data = new YamlConfiguration();
        data.set("uuid", uuid.toString());
        data.set("maxStorageSlot", maxStorageSlot);
        inventory.serialize(data);
        return data;
    }

    @Override
    public VUser deserialize(YamlConfiguration data) {
        this.uuid = UUID.fromString(data.getString("uuid"));
        this.maxStorageSlot = data.getInt("maxStorageSlot");
        this.inventory = new DInventory("가상 창고", 54, true, true, plugin).deserialize(data);
        return this;
    }

    public boolean addStorageSlots(int slots) {
        if (slots > 0) {
            this.maxStorageSlot += slots;
            return true;
        }
        return false;
    }
}