package love.marblegate.omnicard.item;

import love.marblegate.omnicard.misc.CardType;
import love.marblegate.omnicard.misc.ModGroup;
import net.minecraft.item.Item;

public class SpecialCard extends Item {
    public final CardType cardType;

    public SpecialCard(CardType cardType) {
        super(new Properties().tab(ModGroup.GENERAL));
        this.cardType = cardType;
    }
}
