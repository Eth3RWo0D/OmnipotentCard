package love.marblegate.omnicard.misc;

import love.marblegate.omnicard.registry.MobEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.Random;

public class MiscUtil {
    public static boolean canTeleportTo(BlockPos pos, LivingEntity livingEntity, boolean isPlayer) {
        Level level = livingEntity.level;
        if (level.getBlockState(pos).getBlock().isPossibleToRespawnInThis() || level.getBlockState(pos.above()).getBlock().isPossibleToRespawnInThis()) {
            if (isPlayer) {
                for (int i = -1; i > -6; i--) {
                    if (level.getBlockState(pos.below(i)).getMaterial().isSolid()) {
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

    public static <T extends ParticleOptions> void addParticle(ServerLevel world, T particle, double x, double y, double z, double xDist, double yDist, double zDist, double maxSpeed, int count) {
        world.sendParticles(particle, x, y, z, count, xDist, yDist, zDist, maxSpeed);
    }

    public static BlockPos getBlockRandomPos(int x, int y, int z, int radius, Random random) {
        return getBlockRandomPos(x, y, z, radius, radius, random);
    }

    public static BlockPos getBlockRandomPos(int x, int y, int z, int width, int height, Random random) {
        return new BlockPos(x + random.nextInt(2 * width + 1) - width,
                y + random.nextInt(2 * height + 1) - height,
                z + random.nextInt(2 * width + 1) - width);
    }

    public static boolean isHostile(LivingEntity livingEntity, boolean restrictMode) {
        if (restrictMode) {
            return (livingEntity instanceof Monster && !(livingEntity instanceof Piglin) && !(livingEntity instanceof Spider) && !(livingEntity instanceof EnderMan)) ||
                    livingEntity instanceof Slime ||
                    livingEntity instanceof FlyingMob ||
                    livingEntity instanceof Hoglin ||
                    livingEntity instanceof EnderDragon;
        } else {
            return livingEntity instanceof Monster ||
                    livingEntity instanceof Slime ||
                    livingEntity instanceof FlyingMob ||
                    livingEntity instanceof Hoglin ||
                    livingEntity instanceof EnderDragon;
        }
    }

    public static void applyHolyFlameInArea(ServerLevel world, AABB aabb, int ticks) {
        world.getEntities((Entity) null, aabb, entity -> entity instanceof LivingEntity && isHostile((LivingEntity) entity, false))
                .forEach(entity -> ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffectRegistry.HOLY_FLAME.get(), ticks)));
    }

    public static void applyHugeDamageThenApplyFireInArea(ServerLevel world, AABB aabb, float damage, int fireSecond) {
        world.getEntities((Entity) null, aabb, entity -> entity instanceof LivingEntity && isHostile((LivingEntity) entity, false))
                .forEach(entity -> {
                    entity.hurt(DamageSource.MAGIC,damage);
                    entity.setSecondsOnFire(fireSecond);
                });
    }

    public static AABB buildAABB(BlockPos pos, int width, int height) {
        return new AABB(pos.getX() - width, pos.getY() - height, pos.getZ() - width, pos.getX() + width, pos.getY() + height, pos.getZ() + width);
    }

    public static AABB buildAABB(BlockPos pos, int radius) {
        return buildAABB(pos, radius, radius);
    }

    public static MutableComponent tooltip(String id, TextColor color) {
        return new TranslatableComponent(id).setStyle(Style.EMPTY.withColor(color).withBold(false));
    }

    public static MutableComponent tooltipBold(String id, TextColor color) {
        return new TranslatableComponent(id).setStyle(Style.EMPTY.withColor(color).withBold(true));
    }

}
