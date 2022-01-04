package love.marblegate.omnicard.misc;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;

public class ClientMiscUtil {
    public static <T extends ParticleOptions> void addParticle(ClientLevel world, T particle, double x, double y, double z, double xDist, double yDist, double zDist, double maxSpeed) {
        double a = (double) (maxSpeed * xDist);
        double b = (double) (maxSpeed * yDist);
        double c = (double) (maxSpeed * zDist);
        world.addParticle(particle, x, y, z, a, b, c);
    }
}
