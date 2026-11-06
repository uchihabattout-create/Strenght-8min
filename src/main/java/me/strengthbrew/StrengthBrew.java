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

public class StrengthBrew extends JavaPlugin {

    private NamespacedKey mixKey;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        int minutes = Math.max(1, getConfig().getInt("duration-minutes", 8));
        int ticks = minutes * 60 * 20;

        // The potion you get out of the brewing stand:
        // Strength II with a custom duration.
        ItemStack result = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) result.getItemMeta();
        meta.setColor(Color.fromRGB(0x932423)); // Strength red
        meta.displayName(Component.text("Potion of Strength II (Extended)")
                .decoration(TextDecoration.ITALIC, false));
        meta.addCustomEffect(new PotionEffect(PotionEffectType.STRENGTH, ticks, 1), true);
        result.setItemMeta(meta);

        // Input: any normal Strength II potion (the 1:30 one made with glowstone)
        RecipeChoice input = PotionMix.createPredicateChoice(item ->
                item != null
                        && item.getType() == Material.POTION
                        && item.getItemMeta() instanceof PotionMeta pm
                        && pm.getBasePotionType() == PotionType.STRONG_STRENGTH);

        // Ingredient: redstone
        RecipeChoice ingredient = new RecipeChoice.MaterialChoice(Material.REDSTONE);

        mixKey = new NamespacedKey(this, "strength_two_extended");
        getServer().getPotionBrewer().addPotionMix(new PotionMix(mixKey, result, input, ingredient));

        getLogger().info("Strength II + Redstone now makes an " + minutes + " minute Strength II potion.");
    }

    @Override
    public void onDisable() {
        if (mixKey != null) {
            getServer().getPotionBrewer().removePotionMix(mixKey);
        }
    }
}
