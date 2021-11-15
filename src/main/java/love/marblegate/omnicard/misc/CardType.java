package love.marblegate.omnicard.misc;

import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public enum CardType {
    BLANK("white_card",CateCategory.STANDARD,ItemRegistry.BLANK_CARD),
    RED("red_card",CateCategory.STANDARD,ItemRegistry.BLANK_CARD),
    CORAL("orange_card",CateCategory.STANDARD,ItemRegistry.BLANK_CARD),
    GOLD("gold_card",CateCategory.STANDARD,ItemRegistry.BLANK_CARD),
    SEA_GREEN("green_card",CateCategory.STANDARD,ItemRegistry.BLANK_CARD),
    AZURE("sky_card",CateCategory.STANDARD,ItemRegistry.BLANK_CARD),
    CERULEAN_BLUE("blue_card",CateCategory.STANDARD,ItemRegistry.BLANK_CARD),
    HELIOTROPE("violet_card",CateCategory.STANDARD,ItemRegistry.BLANK_CARD),
    INK("black_card",CateCategory.STANDARD,ItemRegistry.BLANK_CARD),

    FLAME("flame_card",CateCategory.ELEMENTAL,true, ItemRegistry.FLAME_CARD),
    TORRENT("torrent_card",CateCategory.ELEMENTAL,true, ItemRegistry.TORRENT_CARD),
    THUNDER("thunder_card",CateCategory.ELEMENTAL,true, ItemRegistry.THUNDER_CARD),
    BRAMBLE("bramble_card",CateCategory.ELEMENTAL,true, ItemRegistry.BRAMBLE_CARD),
    EARTH("earth_card",CateCategory.ELEMENTAL,true, ItemRegistry.EARTH_CARD),
    END("end_card",CateCategory.ELEMENTAL,true, ItemRegistry.END_CARD),

    FIELD("field_card",CateCategory.SPECIAL, ItemRegistry.FIELD_CARD),
    PURIFICATION("purification_card",CateCategory.SPECIAL),
    SEAL("seal_card",CateCategory.SPECIAL, ItemRegistry.SEAL_CARD),
    SUNNY("sunny_card",CateCategory.SPECIAL),
    RAINY("rainy_card",CateCategory.SPECIAL),
    THUNDERSTORM("thunderstorm_card",CateCategory.SPECIAL),
    BLOOM("bloom_card",CateCategory.SPECIAL, ItemRegistry.BLOOM_CARD);


    public String name;
    public CateCategory category;
    public Supplier<Item> retrievedItem;
    public boolean canBePickup;

    CardType(String i,CateCategory category) {
        name = i;
        this.category = category;
        canBePickup = false;
    }

    CardType(String i,CateCategory category, Supplier<Item> retrievedItem) {
        name = i;
        this.category = category;
        this.retrievedItem = retrievedItem;
        canBePickup = false;
    }

    CardType(String i, CateCategory category, boolean canBePickup, Supplier<Item> retrievedItem) {
        name = i;
        this.category = category;
        this.canBePickup = canBePickup;
        this.retrievedItem = retrievedItem;
    }



    public String getTexturePath(){
        return category.path+name+".png";
    }

    public enum CateCategory{
        STANDARD("standard/"),
        ELEMENTAL("elemental/"),
        SPECIAL("special/l");

        public String path;

        CateCategory(String i) {
            path = i;
        }
    }
}
