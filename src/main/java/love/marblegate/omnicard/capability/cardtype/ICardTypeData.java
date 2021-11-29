package love.marblegate.omnicard.capability.cardtype;

import love.marblegate.omnicard.card.CommonCard;

public interface ICardTypeData {
    CommonCard get();

    void set(CommonCard card);

    void setSwitchingCard(boolean i);

    boolean isSwitchingCard();
}
