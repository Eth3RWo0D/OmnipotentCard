package love.marblegate.omnicard.card;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import love.marblegate.omnicard.card.function.CardFunc;
import love.marblegate.omnicard.registry.ItemRegistry;

public class BlockCards {
    private static final BiMap<BlockCard, Byte> CARD_MAPPINGS = HashBiMap.create();
    private static final String SPECIAL_CATE = "special";

    public static BlockCard FIELD = register(new BlockCard.Builder("field_card", SPECIAL_CATE, 24000).setRetrievedItem(ItemRegistry.FIELD_CARD).setServerTickHandler(CardFunc.RunningField::fieldCard).isRetrievableWhenBreak().setClientTickHandler(CardFunc.RunningField::fieldCardClient), (byte) 0);
    public static BlockCard PURIFICATION = register(new BlockCard.Builder("purification_card", SPECIAL_CATE, 100).setRetrievedItem(ItemRegistry.PURIFICATION_CARD).setServerTickHandler(CardFunc.RunningField::purificationCard).setClientTickHandler(CardFunc.RunningField::purificationCardClient), (byte) 1);
    public static BlockCard SEAL = register(new BlockCard.Builder("seal_card", SPECIAL_CATE, -1).setRetrievedItem(ItemRegistry.SEAL_CARD).isRetrievableWhenBreak().setClientTickHandler(CardFunc.RunningField::sealCardClient).setServerTickHandler(CardFunc.RunningField::sealCard), (byte) 2);
    public static BlockCard SUNNY = register(new BlockCard.Builder("sunny_card", SPECIAL_CATE, 24000).setRetrievedItem(ItemRegistry.SUNNY_CARD).setServerTickHandler(CardFunc.RunningField::sunnyCard), (byte) 3);
    public static BlockCard RAINY = register(new BlockCard.Builder("rainy_card", SPECIAL_CATE, 24000).setRetrievedItem(ItemRegistry.RAINY_CARD).setServerTickHandler(CardFunc.RunningField::rainyCard), (byte) 4);
    public static BlockCard THUNDERSTORM = register(new BlockCard.Builder("thunderstorm_card", SPECIAL_CATE, 24000).setRetrievedItem(ItemRegistry.THUNDERSTORM_CARD).setServerTickHandler(CardFunc.RunningField::thunderstormCard), (byte) 5);
    public static BlockCard BLOOM = register(new BlockCard.Builder("bloom_card", SPECIAL_CATE, -1).setRetrievedItem(ItemRegistry.BLOOM_CARD).setServerTickHandler(CardFunc.RunningField::bloomCard).isRetrievableWhenBreak(), (byte) 6);

    public static BlockCard register(BlockCard.Builder builder, byte id) {
        BlockCard blockCard = builder.build();
        if (CARD_MAPPINGS.containsValue(id)) {
            throw new RuntimeException("BlockCard Registry ID " + id + " has been occupied!");
        } else {
            CARD_MAPPINGS.put(blockCard, id);
        }
        return blockCard;
    }

    public static byte toByte(BlockCard blockCard) {
        Byte b = CARD_MAPPINGS.get(blockCard);
        if (b == null) {
            throw new RuntimeException("BlockCard " + blockCard.getCardName() + " hasn't been registered yet!");
        }
        return b;
    }

    public static BlockCard fromByte(byte b) {
        BlockCard blockCard = CARD_MAPPINGS.inverse().get(b);
        if (blockCard == null) {
            throw new RuntimeException("Retrieve BlockCard Info by id " + b + " wrongly! Card Registry ID might been changed!");
        }
        return blockCard;
    }
}
