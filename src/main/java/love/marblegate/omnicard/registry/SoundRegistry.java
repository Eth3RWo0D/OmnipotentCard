package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, OmniCard.MODID);
    public static final RegistryObject<SoundEvent> THROW_COMMON_CARD = SOUNDS.register("throw_common_card", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "throw_common_card")));
    public static final RegistryObject<SoundEvent> THROW_ELEMENTAL_CARD = SOUNDS.register("throw_elemental_card", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "throw_elemental_card")));
    public static final RegistryObject<SoundEvent> ELEMENTAL_CARD_HIT = SOUNDS.register("elemental_card_hit", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "elemental_card_hit")));
    public static final RegistryObject<SoundEvent> CUTTING_CARD = SOUNDS.register("cutting_card", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "cutting_card")));


}
