package net.azisaba.buildtool.listeners;

import net.azisaba.buildtool.Item.BuildItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OpenListener implements Listener {

    @EventHandler
    public void onOpen(@NotNull PlayerInteractEvent e) {
        if (!(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) return;
        if (e.getHand() != null && !e.getHand().equals(EquipmentSlot.HAND)) return;

        Player p = e.getPlayer();
        if (!p.isSneaking()) return;

        ItemStack item = e.getItem();
        if (!(item instanceof BuildItem)) return;

        p.closeInventory();
        p.openInventory(InventoryOptionsListener.getBuildInventory(p.getUniqueId()));
    }
}
