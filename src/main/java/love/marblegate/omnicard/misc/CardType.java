package love.marblegate.omnicard.misc;

import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public enum CardType {
    BLANK("white_card",CateCategory.STANDARD,true,ItemRegistry.BLANK_CARD),
    RED("red_card",CateCategory.STANDARD,false),
    CORAL("orange_card",CateCategory.STANDARD,false),
    GOLD("gold_card",CateCategory.STANDARD,false),
    SEA_GREEN("green_card",CateCategory.STANDARD,false),
    AZURE("sky_card",CateCategory.STANDARD,false),
    CERULEAN_BLUE("blue_card",CateCategory.STANDARD,false),
    HELIOTROPE("violet_card",CateCategory.STANDARD,false),
    INK("black_card",CateCategory.STANDARD,false),

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
    public boolean recyclable;

    CardType(String i,CateCategory category) {
        name = i;
        this.category = category;
    }

    CardType(String i,CateCategory category, Supplier<Item> retrievedItem) {
        name = i;
        this.category = category;
        this.retrievedItem = retrievedItem;
    }

    CardType(String i,CateCategory category,boolean recyclable, Supplier<Item> retrievedItem) {
        name = i;
        this.category = category;
        this.recyclable = recyclable;
        this.retrievedItem = retrievedItem;
    }

    CardType(String i,CateCategory category,boolean recyclable) {
        name = i;
        this.category = category;
        this.recyclable = recyclable;
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
