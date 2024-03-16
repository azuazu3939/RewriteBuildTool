package net.azisaba.buildtool.commands;

import net.azisaba.buildtool.Item.Build;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveBuildToolCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;
        if (!player.hasPermission("BuildTool.command.giveBuildTool")) return false;

        player.getInventory().addItem(new Build(Material.BLAZE_ROD).build());
        return true;
    }
}
