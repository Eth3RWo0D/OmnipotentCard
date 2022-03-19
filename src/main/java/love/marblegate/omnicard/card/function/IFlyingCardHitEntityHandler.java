package love.marblegate.omnicard.card.function;

import love.marblegate.omnicard.entity.FlyingCardEntity;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface IFlyingCardHitEntityHandler {
    void handleHit(FlyingCardEntity card, LivingEntity victim);
}
