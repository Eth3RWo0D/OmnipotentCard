package love.marblegate.omnicard.misc;

import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.data.ModRecipeGen;
import love.marblegate.omnicard.registry.BlockRegistry;
import love.marblegate.omnicard.registry.EffectRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.Random;
import java.util.stream.Collectors;


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

    public static BlockPos getBlockRandomPos(int x, int y, int z, int radius, Random random) {
        return getBlockRandomPos(x,y,z,radius,radius,random);
    }

    public static BlockPos getBlockRandomPos(int x, int y, int z, int width, int height, Random random) {
        return new BlockPos(x + random.nextInt(2*width+1) - width,
                y + random.nextInt(2*height+1) - height,
                z + random.nextInt(2*width+1) - width);
    }

    public static boolean isHostile(LivingEntity livingEntity, boolean restrictMode) {
        if (restrictMode) {
            return (livingEntity instanceof MonsterEntity && !(livingEntity instanceof PiglinEntity) && !(livingEntity instanceof SpiderEntity) && !(livingEntity instanceof EndermanEntity)) ||
                    livingEntity instanceof SlimeEntity ||
                    livingEntity instanceof FlyingEntity ||
                    livingEntity instanceof HoglinEntity ||
                    livingEntity instanceof EnderDragonEntity;
        } else {
            return livingEntity instanceof MonsterEntity ||
                    livingEntity instanceof SlimeEntity ||
                    livingEntity instanceof FlyingEntity ||
                    livingEntity instanceof HoglinEntity ||
                    livingEntity instanceof EnderDragonEntity;
        }
    }

    public static void applyHolyFlameInArea(ServerWorld world, AxisAlignedBB aabb, int ticks){
        world.getEntities((Entity) null,aabb,entity -> entity instanceof LivingEntity && isHostile((LivingEntity) entity,false))
                .forEach(entity -> ((LivingEntity) entity).addEffect(new EffectInstance(EffectRegistry.HOLY_FLAME.get(),ticks)));
    }

    public static AxisAlignedBB buildAABB(BlockPos pos, int width, int height){
        return new AxisAlignedBB(pos.getX()-width,pos.getY()-height,pos.getZ()-width,pos.getX()+width,pos.getY()+height,pos.getZ()+width);
    }

    public static AxisAlignedBB buildAABB(BlockPos pos, int radius){
        return buildAABB(pos,radius,radius);
    }

}
