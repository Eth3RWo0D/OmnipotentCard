package love.marblegate.omnicard.data;

import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.data.*;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class ModRecipeGen extends RecipeProvider {
    public ModRecipeGen(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        //Essentials
        ShapedRecipeBuilder.shaped(ItemRegistry.BLANK_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.PAPER)
                .define('B', Items.LAPIS_LAZULI)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.PROTOTYPE_CORE.get())
                .requires(Items.QUARTZ)
                .unlockedBy("has_quartz", has(Items.QUARTZ)).save(consumer);
        // CardTack
        ShapedRecipeBuilder.shaped(ItemRegistry.CARD_STACK.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.NETHER_STAR)
                .unlockedBy("has_blank_card", has(ItemRegistry.BLANK_CARD.get()))
                .save(consumer);
        //Elemental Card
        ShapedRecipeBuilder.shaped(ItemRegistry.FLAME_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.BLAZE_POWDER)
                .unlockedBy("has_blank_card", has(ItemRegistry.BLANK_CARD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.TORRENT_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.PRISMARINE_SHARD)
                .unlockedBy("has_blank_card", has(ItemRegistry.BLANK_CARD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.BRAMBLE_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.VINE)
                .unlockedBy("has_blank_card", has(ItemRegistry.BLANK_CARD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.END_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.ENDER_PEARL)
                .unlockedBy("has_blank_card", has(ItemRegistry.BLANK_CARD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.EARTH_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.NETHER_BRICK)
                .unlockedBy("has_blank_card", has(ItemRegistry.BLANK_CARD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ItemRegistry.THUNDER_CARD.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemRegistry.BLANK_CARD.get())
                .define('B', Items.RABBIT_FOOT)
                .unlockedBy("has_blank_card", has(ItemRegistry.BLANK_CARD.get()))
                .save(consumer);
        //Special Card Core
        ShapelessRecipeBuilder.shapeless(ItemRegistry.FIELD_CORE.get())
                .requires(ItemRegistry.PROTOTYPE_CORE.get())
                .requires(Items.BLAZE_ROD)
                .requires(Items.GUNPOWDER)
                .requires(Items.ORANGE_DYE)
                .unlockedBy("has_prototype_core", has(ItemRegistry.PROTOTYPE_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.PURIFICATION_CORE.get())
                .requires(ItemRegistry.PROTOTYPE_CORE.get())
                .requires(Items.GLOWSTONE_DUST)
                .requires(Items.MAGMA_CREAM)
                .requires(Items.YELLOW_DYE)
                .unlockedBy("has_prototype_core", has(ItemRegistry.PROTOTYPE_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.SEAL_CORE.get())
                .requires(ItemRegistry.PROTOTYPE_CORE.get())
                .requires(Items.SOUL_SAND)
                .requires(Items.PURPLE_DYE)
                .unlockedBy("has_prototype_core", has(ItemRegistry.PROTOTYPE_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.SUNNY_CORE.get())
                .requires(ItemRegistry.PROTOTYPE_CORE.get())
                .requires(Items.LAVA_BUCKET)
                .requires(Items.BLUE_DYE)
                .unlockedBy("has_prototype_core", has(ItemRegistry.PROTOTYPE_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.RAINY_CORE.get())
                .requires(ItemRegistry.PROTOTYPE_CORE.get())
                .requires(Items.WATER_BUCKET)
                .requires(Items.PURPLE_DYE)
                .unlockedBy("has_prototype_core", has(ItemRegistry.PROTOTYPE_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.THUNDERSTORM_CORE.get())
                .requires(ItemRegistry.PROTOTYPE_CORE.get())
                .requires(Items.WATER_BUCKET)
                .requires(Items.BLUE_DYE)
                .unlockedBy("has_prototype_core", has(ItemRegistry.PROTOTYPE_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.BLOOM_CORE.get())
                .requires(ItemRegistry.PROTOTYPE_CORE.get())
                .requires(Items.BONE_MEAL)
                .requires(Items.ROTTEN_FLESH)
                .requires(Items.GREEN_DYE)
                .unlockedBy("has_prototype_core", has(ItemRegistry.PROTOTYPE_CORE.get())).save(consumer);

        //Special Card
        ShapelessRecipeBuilder.shapeless(ItemRegistry.FIELD_CARD.get())
                .requires(ItemRegistry.BLANK_CARD.get())
                .requires(ItemRegistry.FIELD_CORE.get())
                .unlockedBy("field_core", has(ItemRegistry.FIELD_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.PURIFICATION_CARD.get())
                .requires(ItemRegistry.BLANK_CARD.get())
                .requires(ItemRegistry.PURIFICATION_CORE.get())
                .unlockedBy("purification_core", has(ItemRegistry.PURIFICATION_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.SEAL_CARD.get())
                .requires(ItemRegistry.BLANK_CARD.get())
                .requires(ItemRegistry.SEAL_CORE.get())
                .unlockedBy("seal_core", has(ItemRegistry.SEAL_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.SUNNY_CARD.get())
                .requires(ItemRegistry.BLANK_CARD.get())
                .requires(ItemRegistry.SUNNY_CORE.get())
                .unlockedBy("sunny_core", has(ItemRegistry.SUNNY_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.RAINY_CARD.get())
                .requires(ItemRegistry.BLANK_CARD.get())
                .requires(ItemRegistry.RAINY_CORE.get())
                .unlockedBy("rainy_core", has(ItemRegistry.RAINY_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.THUNDERSTORM_CARD.get())
                .requires(ItemRegistry.BLANK_CARD.get())
                .requires(ItemRegistry.THUNDERSTORM_CORE.get())
                .unlockedBy("thunderstorm_core", has(ItemRegistry.THUNDERSTORM_CORE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(ItemRegistry.BLOOM_CARD.get())
                .requires(ItemRegistry.BLANK_CARD.get())
                .requires(ItemRegistry.BLOOM_CORE.get())
                .unlockedBy("bloom_core", has(ItemRegistry.BLOOM_CORE.get())).save(consumer);

    }
}
