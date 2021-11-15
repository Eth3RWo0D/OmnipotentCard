package love.marblegate.omnicard.capability.cardtype;

import love.marblegate.omnicard.misc.CardType;

public interface ICardTypeData {
    CardType get();

    void set(CardType cardType);

    void setSwitchingCard(boolean i);

    boolean isSwitchingCard();
}
