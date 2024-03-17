package net.azisaba.buildtool;

import net.azisaba.buildtool.commands.GiveBuildToolCommand;
import net.azisaba.buildtool.listeners.BuildToolPlaceListener;
import net.azisaba.buildtool.listeners.InventoryOptionsListener;
import net.azisaba.buildtool.listeners.OpenListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class BuildTool extends JavaPlugin {

    private static BuildTool instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        //Listeners
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BuildTool inst() {
        return instance;
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InventoryOptionsListener(), this);
        pm.registerEvents(new BuildToolPlaceListener(), this);
        pm.registerEvents(new OpenListener(), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("giveBuildTool")).setExecutor(new GiveBuildToolCommand());
    }

    public void runAsyncDelayed(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, runnable, delay);
    }
}
