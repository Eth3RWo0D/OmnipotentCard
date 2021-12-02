package love.marblegate.omnicard.effect;

import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.world.Explosion;

public class DelayedExplosion extends HiddenEffect {
    public DelayedExplosion(EffectType p_i50391_1_) {
        super(p_i50391_1_);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (this == EffectRegistry.READY_TO_EXPLODE.get() && !livingEntity.level.isClientSide()) {
            Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(livingEntity.level, livingEntity) ? Explosion.Mode.BREAK : Explosion.Mode.NONE;
            livingEntity.level.explode(livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 3, explosion$mode);
            livingEntity.hurt(ModDamage.causeExplosion(), 6);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration == 1;
    }
}
