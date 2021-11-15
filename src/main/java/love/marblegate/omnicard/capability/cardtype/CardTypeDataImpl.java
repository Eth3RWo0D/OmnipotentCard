package love.marblegate.omnicard.capability.cardtype;

import love.marblegate.omnicard.misc.CardType;

public class CardTypeDataImpl implements ICardTypeData {
    private CardType cardType;
    private boolean isSwitchingCard;

    public CardTypeDataImpl() {
        cardType = CardType.INK;
        isSwitchingCard = false;
    }

    @Override
    public CardType get() {
        return cardType;
    }

    @Override
    public void set(CardType cardType) {
        this.cardType = cardType;
    }

    @Override
    public void setSwitchingCard(boolean i) {
        isSwitchingCard = i;
    }

    @Override
    public boolean isSwitchingCard() {
        return isSwitchingCard;
    }
}
