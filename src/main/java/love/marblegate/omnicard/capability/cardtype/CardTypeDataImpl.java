package love.marblegate.omnicard.capability.cardtype;

import love.marblegate.omnicard.card.CommonCard;
import love.marblegate.omnicard.card.CommonCards;

public class CardTypeDataImpl implements ICardTypeData {
    private CommonCard type;
    private boolean isSwitchingCard;

    public CardTypeDataImpl() {
        type = CommonCards.UNKNOWN;
        isSwitchingCard = false;
    }

    @Override
    public CommonCard get() {
        return type;
    }

    @Override
    public void set(CommonCard type) {
        this.type = type;
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
