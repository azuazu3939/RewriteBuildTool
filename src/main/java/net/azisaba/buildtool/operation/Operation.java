package net.azisaba.buildtool.operation;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public interface Operation {

    Block getBlock();

    BlockFace getFacing();

    int getRepeat();

    int getAmount();

    void place();

    void setBlock(Block b);

    void subtract();

    enum OperationType {
        LONG_LENGTH_PLACE,
        SQUARE_PLACE;

        @SuppressWarnings("unused")
        public OperationType getType(@NotNull String name) {
            return OperationType.valueOf(name.toLowerCase());
        }
    }
}
