package love.marblegate.omnicard.card;

import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import net.minecraft.entity.LivingEntity;

import java.util.Objects;

public class CommonCard extends AbstractCard {
    private final CardFunc.ITrapCardActivationHandler trapCardActivationHandler;
    private final CardFunc.IFlyingCardHitHandler flyingCardHitHandler;

    CommonCard(Builder builder) {
        super(builder);
        flyingCardHitHandler = builder.flyingCardHitHandler;
        trapCardActivationHandler = builder.trapCardActivationHandler;
    }

    public void hit(FlyingCardEntity card, LivingEntity victim) {
        if (flyingCardHitHandler != null) {
            flyingCardHitHandler.handleHit(card, victim);
        }
    }

    public void activate(CardTrapEntity trap, LivingEntity victim) {
        if (trapCardActivationHandler != null) {
            trapCardActivationHandler.handleTrap(trap, victim);
        }
    }


    public static class Builder extends AbstractCard.Builder<Builder> {
        private CardFunc.ITrapCardActivationHandler trapCardActivationHandler;
        private CardFunc.IFlyingCardHitHandler flyingCardHitHandler;

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

        public Builder setFlyCardHitHandler(CardFunc.IFlyingCardHitHandler flyingCardHitHandler) {
            this.flyingCardHitHandler = Objects.requireNonNull(flyingCardHitHandler);
            return this;
        }

        public Builder setTrapCardActivateHandler(CardFunc.ITrapCardActivationHandler trapCardActivationHandler) {
            this.trapCardActivationHandler = Objects.requireNonNull(trapCardActivationHandler);
            return this;
        }

    }
}
