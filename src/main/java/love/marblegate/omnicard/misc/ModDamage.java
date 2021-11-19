package love.marblegate.omnicard.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ModDamage {

    public static DamageSource causeHolyFlameDamage(){
        return new SimpleDeathMessageDamageSource("holy_fire");
    }

    public static DamageSource causeCardDamage(@Nullable Entity entity, CardType type) {
        if (entity instanceof LivingEntity)
            return new MobtoMobDamageSource("omni_card." + type.name, (LivingEntity) entity);
        else
            return new SimpleDeathMessageDamageSource("omni_card." + type.name);
    }

    public static class MobtoMobDamageSource extends EntityDamageSource {
        public MobtoMobDamageSource(String damageTypeIn, LivingEntity damageSourceEntity) {
            super(damageTypeIn, damageSourceEntity);
        }

        @Override
        @Nullable
        public Entity getEntity() {
            return entity;
        }

        /**
         * Gets the death message that is displayed when the player dies
         */
        @Override
        public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            String s = "death.attack." + msgId;
            return new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), entity.getDisplayName());
        }
    }

    public static class SimpleDeathMessageDamageSource extends DamageSource {
        public SimpleDeathMessageDamageSource(String damageTypeIn) {
            super(damageTypeIn);
        }

        @Override
        public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            String s = "death.attack." + msgId;
            return new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName());
        }
    }
}
