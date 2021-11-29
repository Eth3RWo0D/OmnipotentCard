package love.marblegate.omnicard.misc;

import love.marblegate.omnicard.card.CommonCard;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ModDamage {

    public static DamageSource causeHolyFlameDamage() {
        return new SimpleDeathMessageDamageSource("omni_card.holy_fire");
    }

    public static DamageSource causeCardDamage(@Nullable Entity entity, CommonCard card) {
        if (entity instanceof LivingEntity)
            return new MobtoMobDamageSource("omni_card." + card.getCardName(), (LivingEntity) entity);
        else
            return new SimpleDeathMessageDamageSource("omni_card" + card.getCardName());
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
            String s = "death.attack." + msgId + ".with_source";
            return new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), entity.getDisplayName());
        }
    }

    public static class SimpleDeathMessageDamageSource extends DamageSource {
        public SimpleDeathMessageDamageSource(String damageTypeIn) {
            super(damageTypeIn);
        }

        @Override
        public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
            String s = "death.attack." + msgId + ".no_source";
            return new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName());
        }
    }
}
