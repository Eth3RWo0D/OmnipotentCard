package love.marblegate.omnicard.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class MiscUtil {
    public static boolean canTeleportTo(BlockPos pos, LivingEntity livingEntity, boolean isPlayer){
        World level = livingEntity.level;
        if(level.getBlockState(pos).getBlock().isPossibleToRespawnInThis() || level.getBlockState(pos.above()).getBlock().isPossibleToRespawnInThis()){
            if(isPlayer){
                for(int i = -1;i>-6;i--){
                    if(level.getBlockState(pos.below(i)).getMaterial().isSolid()){
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public static void applyKnockback(LivingEntity livingEntity, double x, double y, double z) {
        livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(x, y, z));
        livingEntity.hurtMarked = true;
        livingEntity.hasImpulse = true;
    }

    public static <T extends IParticleData> void addParticleServerSide(ServerWorld world, T particle, double x, double y, double z, double xDist, double yDist, double zDist, double maxSpeed, int count){
        world.sendParticles(particle,x,y,z,count,xDist,yDist,zDist,maxSpeed);
    }
}
