package net.azisaba.buildtool.Item;

import net.azisaba.buildtool.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Build {

    private static final List<String> lore = new ArrayList<>();
    static {
        lore.add(Util.getColor("&fプレイヤー用建築補助ツール"));
        lore.add(Util.getColor("&f手持ちのブロックを消費して、ブロック設置します。"));
        lore.add(Util.getColor("&fシフト左で設定を開きます。"));
    }

    public ItemStack build(Material material) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("admin_item"), PersistentDataType.INTEGER, 1);
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        meta.setCustomModelData(15);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lBuild&f&l-&b&lTool"));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
