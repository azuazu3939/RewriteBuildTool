package net.azisaba.buildtool.listeners;

import net.azisaba.buildtool.BuildTool;
import net.azisaba.buildtool.operation.LongLengthPlace;
import net.azisaba.buildtool.operation.Operation;
import net.azisaba.buildtool.operation.SquareBlockPlace;
import net.azisaba.buildtool.operation.SquarePlace;
import net.azisaba.buildtool.util.Util;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static net.azisaba.buildtool.util.Util.getOperationType;
import static org.bukkit.Bukkit.getLogger;

public class BuildToolPlaceListener implements Listener {

    private static final Set<UUID> cooldownSet = new HashSet<>();

    @EventHandler
    public void onPlace(@NotNull PlayerInteractEvent e) {

        if (e.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != null && !e.getHand().equals(EquipmentSlot.HAND)) return;
        Player p = e.getPlayer();
        if (!(Util.isWorld(p.getWorld().getName()))) return;

        Block clicked = e.getClickedBlock();
        if (clicked == null) return;
        BlockFace face = e.getBlockFace();

        ItemStack item = e.getItem();
        //check item & debug
        if (!(Util.isBuildItem(item))) return;
        if (cooldownSet.contains(p.getUniqueId())) return;
        ItemStack haveItem = new ItemStack(clicked.getType());

        int i = 0;
        for (ItemStack getItem : p.getInventory().getContents()) {
            if (getItem == null) continue;
            if (getItem.getType() != haveItem.getType()) continue;
            if (getItem.getItemMeta().hasDisplayName()) continue;
            //count
            i+= getItem.getAmount();
        }

        if (i == 0) return;
        Operation.OperationType type = getOperationType(InventoryOptionsListener.getSlot(p.getUniqueId()));
        int length = InventoryOptionsListener.optionsCountMap.getOrDefault(p.getUniqueId(), 1);

        if (type == Operation.OperationType.LONG_LENGTH_PLACE) {
            new LongLengthPlace(clicked, face, length, i, p).place();

        } else if (type == Operation.OperationType.SQUARE_PLACE) {
            new SquarePlace(clicked, face, length, i, p).place();

        } else if (type == Operation.OperationType.SQUARE_BLOCK_PLACE) {
            new SquareBlockPlace(clicked, face, length, i, p).place();
        }
        getLogger().info(p.getName() + "は、BuildToolを使用した。");
        cooldownSet.add(p.getUniqueId());
        BuildTool.inst().runAsyncDelayed(()-> cooldownSet.remove(p.getUniqueId()), 10);
    }
}
