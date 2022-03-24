package love.marblegate.omnicard.card;

import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class CommonCard extends AbstractCard {
    private final CardFunc.ITrapCardActivationHandler trapCardActivationHandler;
    private final CardFunc.IFlyingCardHitEntityHandler flyingCardHitEntityHandler;
    private final CardFunc.IFlyingCardHitBlockHandler flyingCardHitBlockHandler;
    private final CardFunc.IFlyingCardOnFlyHandler flyingCardOnFlyHandler;

    CommonCard(Builder builder) {
        super(builder);
        flyingCardHitEntityHandler = builder.flyingCardHitEntityHandler;
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
            flyingCardHitBlockHandler.handleHit(card, pos, face);
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
        private CardFunc.ITrapCardActivationHandler trapCardActivationHandler;
        private CardFunc.IFlyingCardHitEntityHandler flyingCardHitEntityHandler;
        private CardFunc.IFlyingCardHitBlockHandler flyingCardHitBlockHandler;
        private CardFunc.IFlyingCardOnFlyHandler flyingCardOnFlyHandler = CardFunc.CardOnFly::defaultCard;


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

        public Builder setFlyCardHitEntityHandler(CardFunc.IFlyingCardHitEntityHandler flyingCardHitHandler) {
            this.flyingCardHitEntityHandler = Objects.requireNonNull(flyingCardHitHandler);
            return this;
        }

        public Builder setFlyCardHitBlockHandler(CardFunc.IFlyingCardHitBlockHandler flyingCardHitBlockHandler) {
            this.flyingCardHitBlockHandler = Objects.requireNonNull(flyingCardHitBlockHandler);
            return this;
        }

        public Builder setTrapCardActivateHandler(CardFunc.ITrapCardActivationHandler trapCardActivationHandler) {
            this.trapCardActivationHandler = Objects.requireNonNull(trapCardActivationHandler);
            return this;
        }

        public Builder setFlyCardOnFlyEffect(CardFunc.IFlyingCardOnFlyHandler flyingCardOnFlyHandler) {
            this.flyingCardOnFlyHandler = Objects.requireNonNull(flyingCardOnFlyHandler);
            return this;
        }

    }
}
