package love.marblegate.omnicard.card;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import love.marblegate.omnicard.registry.ItemRegistry;

public class CommonCards {
    private static final BiMap<CommonCard,Byte> CARD_MAPPINGS = HashBiMap.create();
    private static final String STANDARD_CATE = "standard";
    private static final String ELEMENTAL = "elemental";

    public static CommonCard BLANK = register(new CommonCard.Builder("white_card",STANDARD_CATE, ItemRegistry.BLANK_CARD).setFlyCardHitHandler(CardFunc.FlyingCard::whiteCard), (byte) 0);
    public static CommonCard RED = register(new CommonCard.Builder("red_card",STANDARD_CATE, ItemRegistry.BLANK_CARD).setFlyCardHitHandler(CardFunc.FlyingCard::redCard), (byte) 1);
    public static CommonCard CORAL = register(new CommonCard.Builder("orange_card",STANDARD_CATE, ItemRegistry.BLANK_CARD).setFlyCardHitHandler(CardFunc.FlyingCard::orangeCard), (byte) 2);
    public static CommonCard GOLD = register(new CommonCard.Builder("orange_card",STANDARD_CATE, ItemRegistry.BLANK_CARD).setFlyCardHitHandler(CardFunc.FlyingCard::goldCard), (byte) 3);
    public static CommonCard SEA_GREEN = register(new CommonCard.Builder("gold_card",STANDARD_CATE, ItemRegistry.BLANK_CARD).setFlyCardHitHandler(CardFunc.FlyingCard::greenCard), (byte) 4);
    public static CommonCard AZURE = register(new CommonCard.Builder("sky_card",STANDARD_CATE, ItemRegistry.BLANK_CARD).setFlyCardHitHandler(CardFunc.FlyingCard::skyCard), (byte) 5);
    public static CommonCard CERULEAN_BLUE = register(new CommonCard.Builder("blue_card",STANDARD_CATE, ItemRegistry.BLANK_CARD).setFlyCardHitHandler(CardFunc.FlyingCard::blueCard), (byte) 6);
    public static CommonCard HELIOTROPE = register(new CommonCard.Builder("violet_card",STANDARD_CATE, ItemRegistry.BLANK_CARD).setFlyCardHitHandler(CardFunc.FlyingCard::violetCard), (byte) 7);
    public static CommonCard INK = register(new CommonCard.Builder("black_card",STANDARD_CATE, ItemRegistry.BLANK_CARD).setFlyCardHitHandler(CardFunc.FlyingCard::blackCard), (byte) 8);
    public static CommonCard FLAME = register(new CommonCard.Builder("flame_card",ELEMENTAL, ItemRegistry.FLAME_CARD)
            .setFlyCardHitHandler(CardFunc.FlyingCard::flameCard).setTrapCardActivateHandler(CardFunc.TrapCard::flameCard), (byte) 9);
    public static CommonCard TORRENT = register(new CommonCard.Builder("torrent_card",ELEMENTAL, ItemRegistry.TORRENT_CARD)
            .setFlyCardHitHandler(CardFunc.FlyingCard::torrentCard).setTrapCardActivateHandler(CardFunc.TrapCard::torrentCard), (byte) 10);
    public static CommonCard THUNDER = register(new CommonCard.Builder("thunder_card",ELEMENTAL, ItemRegistry.THUNDER_CARD)
            .setFlyCardHitHandler(CardFunc.FlyingCard::thunderCard).setTrapCardActivateHandler(CardFunc.TrapCard::thunderCard), (byte) 11);
    public static CommonCard BRAMBLE = register(new CommonCard.Builder("bramble_card",ELEMENTAL, ItemRegistry.BRAMBLE_CARD)
            .setFlyCardHitHandler(CardFunc.FlyingCard::brambleCard).setTrapCardActivateHandler(CardFunc.TrapCard::bramble_card), (byte) 12);
    public static CommonCard EARTH = register(new CommonCard.Builder("earth_card",ELEMENTAL, ItemRegistry.EARTH_CARD)
            .setFlyCardHitHandler(CardFunc.FlyingCard::earthCard).setTrapCardActivateHandler(CardFunc.TrapCard::earthCard), (byte) 13);
    public static CommonCard END = register(new CommonCard.Builder("end_card",ELEMENTAL, ItemRegistry.END_CARD)
            .setFlyCardHitHandler(CardFunc.FlyingCard::endCard).setTrapCardActivateHandler(CardFunc.TrapCard::endCard), (byte) 14);



    public static CommonCard register(CommonCard.Builder builder, byte id){
        CommonCard commonCard = builder.build();
        if(CARD_MAPPINGS.containsValue(id)){
            throw new RuntimeException("CommonCard Registry ID " + id + " has been occupied!");
        } else{
            CARD_MAPPINGS.put(commonCard,id);
        }
        return commonCard;
    }

    public static byte toByte(CommonCard commonCard){
        Byte b = CARD_MAPPINGS.get(commonCard);
        if(b==null){
            throw new RuntimeException("CommonCard " + commonCard.getCardName() + " hasn't been registered yet!");
        }
        return b;
    }

    public static CommonCard fromByte(byte b){
        CommonCard commonCard = CARD_MAPPINGS.inverse().get(b);
        if(commonCard==null){
            throw new RuntimeException("Retrieve CommonCard Info by id "+ b +" wrongly! Card Registry ID might been changed!");
        }
        return commonCard;
    }
}
