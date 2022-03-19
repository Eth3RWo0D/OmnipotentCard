package love.marblegate.omnicard.card.function;

import love.marblegate.omnicard.entity.FlyingCardEntity;

@FunctionalInterface
public interface IFlyingCardOnFlyHandler {
    void handle(FlyingCardEntity card);
}
