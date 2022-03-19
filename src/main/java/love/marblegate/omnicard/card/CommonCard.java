package love.marblegate.omnicard.card;

import love.marblegate.omnicard.card.function.*;
import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class CommonCard extends AbstractCard {
    private final ITrapCardActivationHandler trapCardActivationHandler;
    private final IFlyingCardHitEntityHandler flyingCardHitEntityHandler;
    private final IFlyingCardHitBlockHandler flyingCardHitBlockHandler;
    private final IFlyingCardOnFlyHandler flyingCardOnFlyHandler;

    CommonCard(Builder builder) {
        super(builder);
        flyingCardHitEntityHandler = builder.iFlyingCardHitEntityHandler;
        trapCardActivationHandler = builder.trapCardActivationHandler;
        flyingCardHitBlockHandler = builder.flyingCardHitBlockHandler;
        flyingCardOnFlyHandler = builder.flyingCardOnFlyHandler;
    }

    public void hitEntity(FlyingCardEntity card, LivingEntity victim) {
        if (flyingCardHitEntityHandler != null) {
            flyingCardHitEntityHandler.handleHit(card, victim);
        }
    }

    public void hitBlock(FlyingCardEntity card, BlockPos pos, Direction face) {
        if (flyingCardHitBlockHandler != null) {
            flyingCardHitBlockHandler.handleHit(card, pos,face);
        }
    }

    public void activate(CardTrapEntity trap, LivingEntity victim) {
        if (trapCardActivationHandler != null) {
            trapCardActivationHandler.handleTrap(trap, victim);
        }
    }

    public void onFly(FlyingCardEntity card){
        flyingCardOnFlyHandler.handle(card);
    }


    public static class Builder extends AbstractCard.Builder<Builder> {
        private ITrapCardActivationHandler trapCardActivationHandler;
        private IFlyingCardHitEntityHandler iFlyingCardHitEntityHandler;
        private IFlyingCardHitBlockHandler flyingCardHitBlockHandler;
        private IFlyingCardOnFlyHandler flyingCardOnFlyHandler = CardFunc.CardOnFly::defaultCard;

        public Builder(String name, String category) {
            super(name, category);
        }

        @Override
        public CommonCard build() {
            return new CommonCard(this);
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder setFlyCardHitEntityHandler(IFlyingCardHitEntityHandler flyingCardHitHandler) {
            this.iFlyingCardHitEntityHandler = Objects.requireNonNull(flyingCardHitHandler);
            return this;
        }

        public Builder setFlyCardHitBlockHandler(IFlyingCardHitBlockHandler flyingCardHitBlockHandler) {
            this.flyingCardHitBlockHandler = Objects.requireNonNull(flyingCardHitBlockHandler);
            return this;
        }

        public Builder setTrapCardActivateHandler(ITrapCardActivationHandler trapCardActivationHandler) {
            this.trapCardActivationHandler = Objects.requireNonNull(trapCardActivationHandler);
            return this;
        }

        public Builder setFlyCardOnFlyEffect(IFlyingCardOnFlyHandler flyingCardOnFlyHandler) {
            this.flyingCardOnFlyHandler = Objects.requireNonNull(flyingCardOnFlyHandler);
            return this;
        }

    }
}
