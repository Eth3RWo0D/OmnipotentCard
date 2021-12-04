package love.marblegate.omnicard.misc;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.server.ServerWorld;

public class ClientMiscUtil {
    public static <T extends IParticleData> void addParticle(ClientWorld world, T particle, double x, double y, double z, double xDist, double yDist, double zDist, double maxSpeed) {
        double a = (double)(maxSpeed * xDist);
        double b = (double)(maxSpeed * yDist);
        double c = (double)(maxSpeed * zDist);
        world.addParticle(particle, x, y, z, a, b, c);
    }
}
