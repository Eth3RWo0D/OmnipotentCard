package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, OmniCard.MODID);
    public static final RegistryObject<SoundEvent> THROW_COMMON_CARD = SOUNDS.register("throw_common_card", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "throw_common_card")));
    public static final RegistryObject<SoundEvent> THROW_COLORED_CARD = SOUNDS.register("throw_colored_card", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "throw_colored_card")));
    public static final RegistryObject<SoundEvent> COLORED_CARD_HIT = SOUNDS.register("colored_card_hit", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "colored_card_hit")));
    public static final RegistryObject<SoundEvent> THROW_ELEMENTAL_CARD = SOUNDS.register("throw_elemental_card", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "throw_elemental_card")));
    public static final RegistryObject<SoundEvent> ELEMENTAL_CARD_HIT = SOUNDS.register("elemental_card_hit", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "elemental_card_hit")));
    public static final RegistryObject<SoundEvent> TRAP_CARD_HIT = SOUNDS.register("trap_card_hit", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "trap_card_hit")));
    public static final RegistryObject<SoundEvent> CUTTING_CARD = SOUNDS.register("cutting_card", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "cutting_card")));
    public static final RegistryObject<SoundEvent> BLOCK_CARD_ON_RUN = SOUNDS.register("block_card_on_run", () -> new SoundEvent(new ResourceLocation(OmniCard.MODID, "block_card_on_run")));


}
