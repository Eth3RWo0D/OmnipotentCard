package love.marblegate.omnicard.card.function;

import love.marblegate.omnicard.entity.CardTrapEntity;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface ITrapCardActivationHandler {
    void handleTrap(CardTrapEntity trap, LivingEntity victim);
}
