package net.azisaba.buildtool.operation;

import net.azisaba.buildtool.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SquarePlace implements Operation {

    private Block b;

    private final BlockFace face;
    private final int l;
    private final int a;

    private final Player p;

    public SquarePlace(Block b, BlockFace face, int l, int a, Player p) {
        this.b = b;
        this.face = face;
        this.a = a;
        this.p = p;
        this.l = Math.min(a, l);
    }

    @Override
    public Block getBlock() {
        return b;
    }

    @Override
    public BlockFace getFacing() {
        return face;
    }

    @Override
    public int getRepeat() {
        return l;
    }

    @Override
    public int getAmount() {
        return a;
    }

    @Override
    public void setBlock(Block b) {
        this.b = b;
    }

    @Override
    public void subtract() {
        for (ItemStack item : p.getInventory().getContents()) {
            if (item == null) continue;
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) continue;
            if (item.getType().equals(b.getType())) {
                item.setAmount(item.getAmount() - 1);
                return;
            }
        }
    }

    @Override
    public boolean hasItem() {
        for (ItemStack item : p.getInventory().getContents()) {
            if (item == null) continue;
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) continue;
            if (item.getType().equals(b.getType())) return true;
        }
        return false;
    }

    @Override
    public void place() {
        List<BlockFace> faces = Util.getFaces(face);
        Block get = getBlock().getRelative(face);
        List<Block> loop = new ArrayList<>();

        int remaining = place(get, getRepeat(), loop);
        List<Block> loop2 = new ArrayList<>();
        while (remaining > 0) {

            for (Block block : loop) {
                for (BlockFace face : faces) {
                    get = block.getRelative(face);
                    remaining = place(get, remaining, loop2);
                    if (remaining <= 0) return;
                }
            }
            loop.clear();
            loop.addAll(loop2);
            loop2.clear();
        }
    }

    private int place(@NotNull Block get, int remaining, List<Block> loop) {
        BlockState state = get.getState();

        if (state.getType() != Material.AIR) {
            remaining--;
            setBlock(get);
            loop.add(get);
            return remaining;
        }

        if (!(hasItem())) return remaining;

        state.setBlockData(get.getType().createBlockData());
        BlockPlaceEvent event = new BlockPlaceEvent(get, state, getBlock(), p.getInventory().getItemInMainHand(), p, true, EquipmentSlot.HAND);

        if (!event.callEvent()) return remaining;

        subtract();
        get.setType(getBlock().getType());
        setBlock(get);
        loop.add(get);

        remaining--;
        return remaining;
    }
}
