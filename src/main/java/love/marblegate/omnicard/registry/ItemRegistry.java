package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.card.BlockCards;
import love.marblegate.omnicard.card.CommonCards;
import love.marblegate.omnicard.item.*;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OmniCard.MODID);

    public static final RegistryObject<Item> CARD_STACK = ITEMS.register("card_stack", CardStack::new);
    public static final RegistryObject<Item> BLANK_CARD = ITEMS.register("blank_card", BlankCard::new);
    public static final RegistryObject<Item> PROTOTYPE_CORE = ITEMS.register("prototype_core", Intermediates::new);

    public static final RegistryObject<Item> FLAME_CARD = ITEMS.register("flame_card", () -> new ElementalCard(CommonCards.FLAME));
    public static final RegistryObject<Item> TORRENT_CARD = ITEMS.register("torrent_card", () -> new ElementalCard(CommonCards.TORRENT));
    public static final RegistryObject<Item> THUNDER_CARD = ITEMS.register("thunder_card", () -> new ElementalCard(CommonCards.THUNDER));
    public static final RegistryObject<Item> BRAMBLE_CARD = ITEMS.register("bramble_card", () -> new ElementalCard(CommonCards.BRAMBLE));
    public static final RegistryObject<Item> EARTH_CARD = ITEMS.register("earth_card", () -> new ElementalCard(CommonCards.EARTH));
    public static final RegistryObject<Item> END_CARD = ITEMS.register("end_card", () -> new ElementalCard(CommonCards.END));

    public static final RegistryObject<Item> FIELD_CARD = ITEMS.register("field_card", () -> new PlaceableSpecialCard(BlockCards.FIELD));
    public static final RegistryObject<Item> PURIFICATION_CARD = ITEMS.register("purification_card", () -> new PlaceableSpecialCard(BlockCards.PURIFICATION));
    public static final RegistryObject<Item> SEAL_CARD = ITEMS.register("seal_card", () -> new PlaceableSpecialCard(BlockCards.SEAL));
    public static final RegistryObject<Item> SUNNY_CARD = ITEMS.register("sunny_card", () -> new PlaceableSpecialCard(BlockCards.SUNNY));
    public static final RegistryObject<Item> RAINY_CARD = ITEMS.register("rainy_card", () -> new PlaceableSpecialCard(BlockCards.RAINY));
    public static final RegistryObject<Item> THUNDERSTORM_CARD = ITEMS.register("thunderstorm_card", () -> new PlaceableSpecialCard(BlockCards.THUNDERSTORM));
    public static final RegistryObject<Item> BLOOM_CARD = ITEMS.register("bloom_card", () -> new PlaceableSpecialCard(BlockCards.BLOOM));

    public static final RegistryObject<Item> FIELD_CORE = ITEMS.register("field_core", Intermediates::new);
    public static final RegistryObject<Item> PURIFICATION_CORE = ITEMS.register("purification_core", Intermediates::new);
    public static final RegistryObject<Item> SEAL_CORE = ITEMS.register("seal_core", Intermediates::new);
    public static final RegistryObject<Item> SUNNY_CORE = ITEMS.register("sunny_core", Intermediates::new);
    public static final RegistryObject<Item> RAINY_CORE = ITEMS.register("rainy_core", Intermediates::new);
    public static final RegistryObject<Item> THUNDERSTORM_CORE = ITEMS.register("thunderstorm_core", Intermediates::new);
    public static final RegistryObject<Item> BLOOM_CORE = ITEMS.register("bloom_core", Intermediates::new);



}
