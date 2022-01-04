package love.marblegate.omnicard.effect;

import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class PoisonNowLethal extends HiddenEffect {
    public PoisonNowLethal(MobEffectCategory p_i50391_1_) {
        super(p_i50391_1_);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (this == MobEffectRegistry.POISON_NOW_LETHAL.get() && !livingEntity.level.isClientSide()) {
            if (livingEntity.hasEffect(MobEffects.POISON) && livingEntity.getHealth() <= 1.5F) {
                livingEntity.hurt(ModDamage.causeLethalPoisonDamage(), 100);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
