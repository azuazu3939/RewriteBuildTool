package net.azisaba.buildtool.util;

import net.azisaba.buildtool.operation.Operation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Util {

    private static final List<BlockFace> faceList = new ArrayList<>();
    private static final Map<Integer, Operation.OperationType> operations = new HashMap<>();

    static {
        faceList.add(BlockFace.NORTH);
        faceList.add(BlockFace.EAST);
        faceList.add(BlockFace.SOUTH);
        faceList.add(BlockFace.WEST);
        faceList.add(BlockFace.UP);
        faceList.add(BlockFace.DOWN);
    }

    public static Inventory setOptions(Inventory inv) {
        registerOperationAndPlace(inv, 0, createOption(getColor("&aライン型設置"), Material.OAK_LOG), Operation.OperationType.LONG_LENGTH_PLACE);
        registerOperationAndPlace(inv, 1, createOption(getColor("&a平面型設置"), Material.SPRUCE_LOG), Operation.OperationType.SQUARE_PLACE);
        return inv;
    }

    public static @NotNull ItemStack createOption(String name, Material material) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        item.setItemMeta(meta);
        return item;
    }

    public static void registerOperationAndPlace(@NotNull Inventory inv, int slot, ItemStack item, Operation.OperationType type) {
        inv.setItem(slot, item);
        operations.put(slot, type);
    }

    public static @NotNull Set<BlockFace> getFaces(BlockFace face) {

        Set<BlockFace> result = new HashSet<>();
        if (faceList.contains(face)) {
            for (BlockFace f : faceList) {
                if (f.equals(face) || f.equals(face.getOppositeFace())) continue;
                result.add(f);
            }
        }
        return result;
    }

    @Contract("_ -> new")
    public static @NotNull String getColor(String name) {
        return ChatColor.translateAlternateColorCodes('&', name);
    }

    public static Operation.OperationType getOperationType(int slot) {
        return operations.get(slot);
    }
}
