package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, OmniCard.MODID);
    public static final RegistryObject<SimpleParticleType> EMPTY = PARTICLE_TYPES.register("empty_particle", () -> new SimpleParticleType(false));
}
