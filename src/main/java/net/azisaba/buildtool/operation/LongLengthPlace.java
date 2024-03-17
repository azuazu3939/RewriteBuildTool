package net.azisaba.buildtool.operation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class LongLengthPlace implements Operation {

    private Block b;

    private final BlockFace face;
    private final int l;
    private final int a;

    private final Player p;

    public LongLengthPlace(Block b, BlockFace face, int l, int a, Player p) {

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

        for (int i = 0; i < getRepeat(); i++) {

            if (!(hasItem())) return;
            Block get = getBlock().getRelative(face);
            if (get.getType() != Material.AIR) return; // if block is air, return もしかしたら、これもオプションにするかも

            BlockState state = get.getState();
            BlockPlaceEvent event = new BlockPlaceEvent(get, state, getBlock(), p.getInventory().getItemInMainHand(), p, true, EquipmentSlot.HAND);

            if (!event.callEvent()) return;

            subtract();
            get.setType(getBlock().getType());
            setBlock(get);
        }
    }
}
