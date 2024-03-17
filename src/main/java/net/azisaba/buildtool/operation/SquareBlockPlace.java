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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SquareBlockPlace implements Operation {

    private Block b;
    private final BlockFace face;
    private final int l;
    private final int i;
    private final Player p;

    public SquareBlockPlace(Block b, BlockFace face, int l, int i, Player p) {
        this.b = b;
        this.face = face;
        this.i = i;
        this.p = p;
        this.l = l;
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
        return i;
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
    public void place() {
        List<BlockFace> faces = Util.getFaces(face);
        Block get = getBlock().getRelative(face);

        if (l >= 1) place(get);
        if (l >= 2) stackPlace(get, faces, 2);
        if (l >= 3) stackPlace(get, faces, 3);
        if (l >= 4) stackPlace(get, faces, 4);

    }

    private void stackPlace(Block get, @NotNull List<BlockFace> faces, int l) {

        for (BlockFace face : faces) {
            Block get2 = getBlockLoop(get, face, l);
            place(get2);
            for (BlockFace face2 : getRightLeftFaces(face)) {
                loopPlace(get2, face2, l);
            }
        }
    }

    private void loopPlace(Block get, BlockFace face, int l) {
        for (int i = 1; i < l; i++) {
            Block get2 = get.getRelative(face);
            place(get2);
            get = get2;
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


    private void place(@NotNull Block get) {

        if (!(hasItem())) return;
        if (get.getType() == Material.AIR) {
            BlockState state = get.getState();
            state.setBlockData(get.getType().createBlockData());
            BlockPlaceEvent event = new BlockPlaceEvent(get, state, getBlock(), p.getInventory().getItemInMainHand(), p, true, EquipmentSlot.HAND);
            if (!event.callEvent()) return;

            get.setType(getBlock().getType());
            subtract();
        }
    }

    @NotNull
    private Set<BlockFace> getRightLeftFaces(@NotNull BlockFace branchFace) {

        Set<BlockFace> set = new HashSet<>();
        if (face.equals(BlockFace.NORTH) || face.equals(BlockFace.SOUTH)) {
            if (branchFace.equals(BlockFace.EAST) || branchFace.equals(BlockFace.WEST)) {
                set.add(BlockFace.DOWN);
                set.add(BlockFace.UP);
            } else {
                set.add(BlockFace.EAST);
                set.add(BlockFace.WEST);
            }
            return set;
        }
        boolean booleanFace = branchFace.equals(BlockFace.NORTH) || branchFace.equals(BlockFace.SOUTH);

        if (face.equals(BlockFace.EAST) || face.equals(BlockFace.WEST)) {
            if (booleanFace) {
                set.add(BlockFace.DOWN);
                set.add(BlockFace.UP);
            } else {
                set.add(BlockFace.NORTH);
                set.add(BlockFace.SOUTH);
            }
        }
        else {
            if (booleanFace) {
                set.add(BlockFace.EAST);
                set.add(BlockFace.WEST);
            } else {
                set.add(BlockFace.NORTH);
                set.add(BlockFace.SOUTH);
            }
        }
        return set;
    }

    private Block getBlockLoop(Block b, BlockFace branchFace, int l) {

        while (l > 1) {
            b = b.getRelative(branchFace);
            l--;
        }
        return b;
    }
}
