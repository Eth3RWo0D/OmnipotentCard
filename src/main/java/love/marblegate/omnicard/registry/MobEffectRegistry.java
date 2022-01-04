package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.effect.DelayedExplosion;
import love.marblegate.omnicard.effect.HiddenEffect;
import love.marblegate.omnicard.effect.HolyFlame;
import love.marblegate.omnicard.effect.PoisonNowLethal;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, OmniCard.MODID);
    public static final RegistryObject<MobEffect> HOLY_FLAME = MOB_EFFECT.register("holy_flame", HolyFlame::new);

    public static final RegistryObject<MobEffect> DIZZY = MOB_EFFECT.register("dizzy", () -> new HiddenEffect(MobEffectCategory.HARMFUL)
            .addAttributeModifier(Attributes.FOLLOW_RANGE, "831CF4BC-ED83-4072-A2A2-C115DD72317F", -0.96d, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "6E21DF28-A639-43E5-A189-D9ECFAE3AA39", -0.67D, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<MobEffect> READY_TO_EXPLODE = MOB_EFFECT.register("ready_to_explode", () -> new DelayedExplosion(MobEffectCategory.HARMFUL));

    public static final RegistryObject<MobEffect> POISON_NOW_LETHAL = MOB_EFFECT.register("poison_now_lethal", () -> new PoisonNowLethal(MobEffectCategory.NEUTRAL));

    public static final RegistryObject<MobEffect> DO_NOT_MOVE = MOB_EFFECT.register("do_not_move", () -> new HiddenEffect(MobEffectCategory.HARMFUL)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "4064F194-CC57-4F60-B871-98B00F4D254A", -0.99D, AttributeModifier.Operation.MULTIPLY_TOTAL));
}
