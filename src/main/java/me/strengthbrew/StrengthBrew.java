package me.strengthbrew;

import io.papermc.paper.potion.PotionMix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class StrengthBrew extends JavaPlugin {

    private final List<NamespacedKey> mixKeys = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        int minutes = Math.max(1, getConfig().getInt("duration-minutes", 8));
        int ticks = minutes * 60 * 20;

        register(Material.POTION, "Potion of Strength II (Extended)", "drink", ticks);
        register(Material.SPLASH_POTION, "Splash Potion of Strength II (Extended)", "splash", ticks);

        getLogger().info("Strength II + Redstone now makes an " + minutes + " minute Strength II potion.");
    }

    private void register(Material type, String name, String id, int ticks) {
        // Result: same potion type, Strength II with a custom duration
        ItemStack result = new ItemStack(type);
        PotionMeta meta = (PotionMeta) result.getItemMeta();
        meta.setColor(Color.fromRGB(0x932423));
        meta.displayName(Component.text(name).decoration(TextDecoration.ITALIC, false));
        meta.addCustomEffect(new PotionEffect(PotionEffectType.STRENGTH, ticks, 1), true);
        result.setItemMeta(meta);

        // Input: a normal Strength II potion of the same type
        RecipeChoice input = PotionMix.createPredicateChoice(item ->
                item != null
                        && item.getType() == type
                        && item.getItemMeta() instanceof PotionMeta pm
                        && pm.getBasePotionType() == PotionType.STRONG_STRENGTH);

        RecipeChoice ingredient = new RecipeChoice.MaterialChoice(Material.REDSTONE);

        NamespacedKey key = new NamespacedKey(this, "strength_two_extended_" + id);
        mixKeys.add(key);
        getServer().getPotionBrewer().addPotionMix(new PotionMix(key, result, input, ingredient));
    }

    @Override
    public void onDisable() {
        for (NamespacedKey key : mixKeys) {
            getServer().getPotionBrewer().removePotionMix(key);
        }
    }
}
