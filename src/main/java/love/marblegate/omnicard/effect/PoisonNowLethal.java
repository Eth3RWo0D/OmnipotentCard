package love.marblegate.omnicard.effect;

import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

public class PoisonNowLethal extends HiddenEffect {
    public PoisonNowLethal(EffectType p_i50391_1_) {
        super(p_i50391_1_);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (this == EffectRegistry.POISON_NOW_LETHAL.get() && !livingEntity.level.isClientSide()) {
            if (livingEntity.hasEffect(Effects.POISON) && livingEntity.getHealth() <= 1.5F) {
                livingEntity.hurt(ModDamage.causeLethalPoisonDamage(), 100);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
