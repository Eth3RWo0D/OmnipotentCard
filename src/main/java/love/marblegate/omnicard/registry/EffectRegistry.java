package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.effect.DelayedExplosion;
import love.marblegate.omnicard.effect.HiddenEffect;
import love.marblegate.omnicard.effect.PoisonNowLethal;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {
    public static final DeferredRegister<Effect> EFFECT = DeferredRegister.create(ForgeRegistries.POTIONS, OmniCard.MODID);
    public static final RegistryObject<Effect> DIZZY = EFFECT.register("dizzy", () -> new HiddenEffect(EffectType.HARMFUL)
            .addAttributeModifier(Attributes.FOLLOW_RANGE, "831CF4BC-ED83-4072-A2A2-C115DD72317F", -0.96d, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "6E21DF28-A639-43E5-A189-D9ECFAE3AA39", -0.67D, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Effect> READY_TO_EXPLODE = EFFECT.register("ready_to_explode", () -> new DelayedExplosion(EffectType.HARMFUL));

    public static final RegistryObject<Effect> POISON_NOW_LETHAL = EFFECT.register("poison_now_lethal", () -> new PoisonNowLethal(EffectType.NEUTRAL));


}
