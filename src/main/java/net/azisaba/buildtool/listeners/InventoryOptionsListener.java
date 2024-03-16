package net.azisaba.buildtool.listeners;

import net.azisaba.buildtool.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryOptionsListener implements Listener, InventoryHolder {

    private static final Map<UUID, Integer> optionsMap = new HashMap<>();
    public static final Map<UUID, Integer> optionsCountMap = new HashMap<>();

    private static final Map<UUID, Inventory> getInventoryMap = new HashMap<>();

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {

        Inventory inv = e.getInventory();
        if (!(inv.getHolder() instanceof InventoryOptionsListener)) return;
        e.setCancelled(true);
        //処理
        int slot = e.getSlot();
        UUID uuid = e.getWhoClicked().getUniqueId();
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null) return;

        setAmount(clicked, e, uuid);

        optionsMap.put(uuid, slot);
        optionsCountMap.put(uuid, clicked.getAmount());
    }

    @EventHandler
    public void onDrag(@NotNull InventoryDragEvent e) {

        if (e.getInventory().getHolder() instanceof InventoryOptionsListener) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof InventoryOptionsListener) {
            getInventoryMap.put(e.getPlayer().getUniqueId(), inv);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return Util.setOptions(Bukkit.createInventory(this, 9, "Build Tool Options"));
    }

    public static Inventory getBuildInventory(UUID uuid) {
        if (!getInventoryMap.containsKey(uuid)) {
            getInventoryMap.put(uuid, new InventoryOptionsListener().getInventory());
        }
        return getInventoryMap.get(uuid);
    }

    private void setAmount(ItemStack clicked, @NotNull InventoryClickEvent e, UUID uuid) {
        int edit = 1;
        if (e.isShiftClick()) edit = 5;
        if (e.isRightClick()) edit = -edit;

        if (optionsCountMap.containsKey(uuid)) {
            int check = optionsCountMap.get(uuid) + edit;
            if (check < 1) check = 64 + check;
            if (check > 64) check-= 64;

            clicked.setAmount(check);
        }
    }

    public static int getSlot(UUID uuid) {
        if (!optionsMap.containsKey(uuid)) return 0;
        return optionsMap.get(uuid);
    }
}
