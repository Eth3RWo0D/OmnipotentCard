package love.marblegate.omnicard.effect;

import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;

public class DelayedExplosion extends HiddenEffect {
    public DelayedExplosion(MobEffectCategory p_i50391_1_) {
        super(p_i50391_1_);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (this == MobEffectRegistry.READY_TO_EXPLODE.get() && !livingEntity.level.isClientSide()) {
            Explosion.BlockInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(livingEntity.level, livingEntity) ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE;
            livingEntity.level.explode(livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 3, explosion$mode);
            livingEntity.hurt(ModDamage.causeExplosion(), 6);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration == 1;
    }
}
