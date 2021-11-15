package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.item.BlankCard;
import love.marblegate.omnicard.item.CardStack;
import love.marblegate.omnicard.item.ElementalCard;
import love.marblegate.omnicard.item.PlaceableSpecialCard;
import love.marblegate.omnicard.misc.CardType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OmniCard.MODID);

    public static final RegistryObject<Item> CARD_STACK = ITEMS.register("card_stack", CardStack::new);
    public static final RegistryObject<Item> BLANK_CARD = ITEMS.register("blank_card", BlankCard::new);

    public static final RegistryObject<Item> FLAME_CARD = ITEMS.register("flame_card", () -> new ElementalCard(CardType.FLAME));
    public static final RegistryObject<Item> TORRENT_CARD = ITEMS.register("torrent_card", () -> new ElementalCard(CardType.TORRENT));
    public static final RegistryObject<Item> THUNDER_CARD = ITEMS.register("thunder_card", () -> new ElementalCard(CardType.THUNDER));
    public static final RegistryObject<Item> BRAMBLE_CARD = ITEMS.register("bramble_card", () -> new ElementalCard(CardType.BRAMBLE));
    public static final RegistryObject<Item> EARTH_CARD = ITEMS.register("earth_card", () -> new ElementalCard(CardType.EARTH));
    public static final RegistryObject<Item> END_CARD = ITEMS.register("end_card", () -> new ElementalCard(CardType.END));

    public static final RegistryObject<Item> FIELD_CARD = ITEMS.register("field_card", () -> new PlaceableSpecialCard(CardType.FIELD));
    public static final RegistryObject<Item> PURIFICATION_CARD = ITEMS.register("purification_card", () -> new PlaceableSpecialCard(CardType.PURIFICATION));
    public static final RegistryObject<Item> SEAL_CARD = ITEMS.register("seal_card", () -> new PlaceableSpecialCard(CardType.SEAL));
    public static final RegistryObject<Item> SUNNY_CARD = ITEMS.register("sunny_card", () -> new PlaceableSpecialCard(CardType.SUNNY));
    public static final RegistryObject<Item> RAINY_CARD = ITEMS.register("rainy_card", () -> new PlaceableSpecialCard(CardType.RAINY));
    public static final RegistryObject<Item> THUNDERSTORM_CARD = ITEMS.register("thunderstorm_card", () -> new PlaceableSpecialCard(CardType.THUNDERSTORM));
    public static final RegistryObject<Item> BLOOM_CARD = ITEMS.register("bloom_card", () -> new PlaceableSpecialCard(CardType.BLOOM));


}
