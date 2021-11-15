package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.particle.EmptyParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, OmniCard.MODID);

    // Well, empty particle seems strange, but it's necessary since I reused the logic DamagingProjectileEntity
    public static final RegistryObject<EmptyParticleType> EMPTY = PARTICLE_TYPES.register("empty_particle", EmptyParticleType::new);
}
