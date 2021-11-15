package love.marblegate.omnicard.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;

import javax.annotation.Nullable;

public class EmptyParticleFactory implements IParticleFactory<EmptyParticleType> {
    @Nullable
    @Override
    public Particle createParticle(EmptyParticleType data, ClientWorld clientWorld, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new EmptyParticle(clientWorld,x,y,z);
    }
}
