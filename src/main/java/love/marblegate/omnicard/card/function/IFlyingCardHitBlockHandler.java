package love.marblegate.omnicard.card.function;

import love.marblegate.omnicard.entity.FlyingCardEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

@FunctionalInterface
public interface IFlyingCardHitBlockHandler {
    void handleHit(FlyingCardEntity card, BlockPos pos, Direction face);
}
