package love.marblegate.omnicard.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ModDamage {

    public static DamageSource causeHolyFlameDamage() {
        return new SimpleDeathMessageDamageSource("omni_card.holy_fire");
    }

    public static DamageSource causeLethalPoisonDamage() {
        return new SimpleDeathMessageDamageSource("omni_card.lethal_poison").bypassArmor();
    }

    public static DamageSource causeExplosion() {
        return new SimpleDeathMessageDamageSource("omni_card.explosion").setExplosion();
    }

    public static DamageSource causeCardDamage(Entity damageSource, @Nullable Entity owner) {
        if (owner instanceof LivingEntity)
            return DamageSource.mobAttack((LivingEntity) owner);
        else
            return DamageSource.thrown(damageSource, owner);
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
