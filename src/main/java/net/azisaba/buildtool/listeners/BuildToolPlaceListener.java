package net.azisaba.buildtool.listeners;

import net.azisaba.buildtool.Item.BuildItem;
import net.azisaba.buildtool.operation.LongLengthPlace;
import net.azisaba.buildtool.operation.Operation;
import net.azisaba.buildtool.operation.SquarePlace;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static net.azisaba.buildtool.util.Util.getOperationType;

public class BuildToolPlaceListener implements Listener {

    @EventHandler
    public void onPlace(@NotNull PlayerInteractEvent e) {

        if (e.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != null && !e.getHand().equals(EquipmentSlot.HAND)) return;

        Block block = e.getClickedBlock();
        if (block == null) return;
        BlockFace face = e.getBlockFace();

        ItemStack item = e.getItem();
        //check item & debug
        debug("1");
        if (!(item instanceof BuildItem)) return;
        debug("2");
        Player p = e.getPlayer();
        ItemStack haveItem = new ItemStack(block.getType());

        int i = 0;
        for (ItemStack getItem : p.getInventory().getContents()) {
            if (getItem.getType() == null) continue;
            if (getItem.getType() != haveItem.getType()) continue;
            if (getItem.hasItemMeta()) continue;
            //count
            i++;
        }

        Operation.OperationType type = getOperationType(InventoryOptionsListener.getSlot(p.getUniqueId()));
        int length = InventoryOptionsListener.optionsCountMap.getOrDefault(p.getUniqueId(), 1);

        if (type == Operation.OperationType.LONG_LENGTH_PLACE) {
            new LongLengthPlace(block, face, length, i, p).place();

        } else if (type == Operation.OperationType.SQUARE_PLACE) {
            new SquarePlace(block, face, length, i, p).place();
        }
    }

    private void debug(String msg) {
        Player p = Bukkit.getPlayerExact("MCLove32");
        if (p == null) return;
        p.sendMessage(msg);
    }
}
