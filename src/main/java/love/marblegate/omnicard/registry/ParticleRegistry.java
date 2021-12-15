package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, OmniCard.MODID);
    public static final RegistryObject<BasicParticleType> EMPTY = PARTICLE_TYPES.register("empty_particle", () -> new BasicParticleType(false));
}
