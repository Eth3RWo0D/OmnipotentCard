package love.marblegate.omnicard.effect;

import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;
import java.util.List;

public class HolyFlame extends Effect {

    public HolyFlame() {
        super(EffectType.HARMFUL, 16766001);
    }


    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
       if(!entity.level.isClientSide()){
           entity.setSecondsOnFire(2);
           entity.addEffect(new EffectInstance(EffectRegistry.HOLY_FLAME.get(),21,amplifier));
          if( entity.fireImmune() || entity.level.isRaining() ){
              entity.hurt(ModDamage.causeHolyFlameDamage(),1);
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
