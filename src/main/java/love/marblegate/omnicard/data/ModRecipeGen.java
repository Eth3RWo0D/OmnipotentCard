package love.marblegate.omnicard.data;

import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class ModRecipeGen extends RecipeProvider {
    public ModRecipeGen(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ItemRegistry.BLANK_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.PAPER)
                .define('B', Items.LAPIS_LAZULI)
                .unlockedBy("paper", InventoryChangeTrigger.Instance.hasItems(Items.PAPER, Items.LAPIS_LAZULI))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.CARD_STACK.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.NETHER_STAR)
                .unlockedBy("blank_card", InventoryChangeTrigger.Instance.hasItems(ItemRegistry.BLANK_CARD.get(), Items.NETHER_STAR))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.FLAME_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.BLAZE_POWDER)
                .unlockedBy("blank_card", InventoryChangeTrigger.Instance.hasItems(ItemRegistry.BLANK_CARD.get(), Items.BLAZE_POWDER))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.TORRENT_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.PRISMARINE_SHARD)
                .unlockedBy("blank_card", InventoryChangeTrigger.Instance.hasItems(ItemRegistry.BLANK_CARD.get(), Items.PRISMARINE_SHARD))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.BRAMBLE_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.VINE)
                .unlockedBy("blank_card", InventoryChangeTrigger.Instance.hasItems(ItemRegistry.BLANK_CARD.get(), Items.VINE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.END_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.ENDER_PEARL)
                .unlockedBy("blank_card", InventoryChangeTrigger.Instance.hasItems(ItemRegistry.BLANK_CARD.get(), Items.ENDER_PEARL))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.EARTH_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.NETHER_BRICK)
                .unlockedBy("blank_card", InventoryChangeTrigger.Instance.hasItems(ItemRegistry.BLANK_CARD.get(), Items.NETHER_BRICK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.THUNDER_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.RABBIT_FOOT)
                .unlockedBy("blank_card", InventoryChangeTrigger.Instance.hasItems(ItemRegistry.BLANK_CARD.get(), Items.RABBIT_FOOT))
                .save(consumer);
    }
}
