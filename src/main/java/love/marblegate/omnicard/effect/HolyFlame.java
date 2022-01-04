package love.marblegate.omnicard.effect;

import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.MobEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HolyFlame extends MobEffect {

    public HolyFlame() {
        super(MobEffectCategory.HARMFUL, 10161421);
    }


    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level.isClientSide()) {
            entity.setSecondsOnFire(2);
            entity.addEffect(new MobEffectInstance(MobEffectRegistry.HOLY_FLAME.get(), 21, amplifier));
            if (entity.fireImmune() || entity.level.isRaining()) {
                entity.hurt(ModDamage.causeHolyFlameDamage(), 1);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
