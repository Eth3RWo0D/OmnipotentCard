package love.marblegate.omnicard.misc;

import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public enum SpecialCardType {
    // Special Card
    FIELD("field_card",24000,true, ItemRegistry.FIELD_CARD, entity -> {

    }),
    PURIFICATION("purification_card",100,false, ItemRegistry.PURIFICATION_CARD, entity -> {

    }),
    SEAL("seal_card",-1,true, ItemRegistry.SEAL_CARD, entity -> {

    }),
    SUNNY("sunny_card",24000,false, ItemRegistry.SUNNY_CARD, entity -> {

    }),
    RAINY("rainy_card",24000,false,  ItemRegistry.RAINY_CARD, entity -> {

    }),
    THUNDERSTORM("thunderstorm_card",24000,false,  ItemRegistry.THUNDERSTORM_CARD, entity -> {

    }),
    BLOOM("bloom_card",-1,true, ItemRegistry.BLOOM_CARD, entity -> {

    });

    public String name;
    public int lifetimeAfterPlace;
    public boolean canRetrieveByBreak;
    public Supplier<Item> retrievedItem;
    public ISpecialCardBlockServerTick serverTickLogic;

    SpecialCardType(String i,int lifetimeAfterPlace,boolean canRetrieveByBreak, Supplier<Item> retrievedItem, ISpecialCardBlockServerTick serverTickLogic) {
        name = i;
        this.lifetimeAfterPlace = lifetimeAfterPlace;
        this.canRetrieveByBreak = canRetrieveByBreak;
        this.retrievedItem = retrievedItem;
        this.serverTickLogic = serverTickLogic;
    }

    @FunctionalInterface
    public interface ISpecialCardBlockServerTick{
        void handle(SpecialCardBlockTileEntity entity);
    }

    public String getTexturePath() {
        return "special/" + name + ".png";
    }
}
