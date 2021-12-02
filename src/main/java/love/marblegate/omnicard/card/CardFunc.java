package love.marblegate.omnicard.card;

import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FallingStoneEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.entity.StoneSpikeEntity;
import love.marblegate.omnicard.misc.MiscUtil;
import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EffectRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CardFunc {
    public static class FlyingCard {
        public static void whiteCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card,card.getOwner()), 6);
            }
        }

        public static void redCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                if (victim.hasEffect(EffectRegistry.READY_TO_EXPLODE.get())) {
                    Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(victim.level, victim) ? Explosion.Mode.BREAK : Explosion.Mode.NONE;
                    victim.level.explode(victim, ModDamage.causeCardDamage(card,card.getOwner()).setExplosion(), null, victim.getX(), victim.getY(), victim.getZ(), 0.71F, false, explosion$mode);
                }
                victim.addEffect(new EffectInstance(EffectRegistry.READY_TO_EXPLODE.get(), 30));
            }
        }

        public static void orangeCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card,card.getOwner()), 6);
                if (victim instanceof PlayerEntity) {
                    victim.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 80, 3));
                } else {
                    victim.addEffect(new EffectInstance(EffectRegistry.DIZZY.get(), 80));
                }
                victim.addEffect(new EffectInstance(Effects.CONFUSION, 80));
            }
        }

        public static void goldCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card,card.getOwner()), 6);
                victim.addEffect(new EffectInstance(Effects.LEVITATION, 80));
                victim.addEffect(new EffectInstance(Effects.GLOWING, 80));
            }
        }

        public static void greenCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                if (victim.isInvertedHealAndHarm()) {
                    victim.hurt(ModDamage.causeCardDamage(card,card.getOwner()), 8);
                } else {
                    victim.heal(8);
                }
            }
        }

        public static void skyCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 60));
            }
        }

        public static void blueCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card,card.getOwner()), 6);
                victim.addEffect(new EffectInstance(Effects.BLINDNESS, 100));
            }
        }

        public static void violetCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card,card.getOwner()), 6);
                victim.addEffect(new EffectInstance(Effects.POISON, 80));
                victim.addEffect(new EffectInstance(EffectRegistry.POISON_NOW_LETHAL.get(), 81));
            }
        }

        public static void blackCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card,card.getOwner()), 6);
                victim.addEffect(new EffectInstance(Effects.WITHER, 80));
            }
        }

        public static void flameCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card,card.getOwner()), 4);
                victim.setSecondsOnFire(3);
            }
        }

        public static void torrentCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                if (victim.isOnFire()) {
                    victim.clearFire();
                }
                Vector3d vector3d = card.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(2D);
                if (vector3d.lengthSqr() > 0.0D) {
                    MiscUtil.applyKnockback(victim, vector3d.x, 0.8D, vector3d.z);
                }
            }
        }

        public static void thunderCard(FlyingCardEntity card, LivingEntity victim) {
            handleCommonThunderCardLogic(victim, card.level.isClientSide(), card.getOwner(), card);
        }

        public static void brambleCard(FlyingCardEntity card, LivingEntity victim) {
            //TODO need implement
        }

        public static void earthCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                FallingStoneEntity fallingStoneEntity = new FallingStoneEntity(card.level);
                fallingStoneEntity.setPos(victim.getX(), victim.getY() + 3, victim.getZ());
                card.level.addFreshEntity(fallingStoneEntity);
            }
        }

        public static void endCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                int trialTime = 0;
                boolean isPlayer = victim instanceof PlayerEntity;
                BlockPos blockPos = victim.blockPosition(), targetLocation = null;
                while (trialTime <= 5) {
                    targetLocation = blockPos.offset(victim.getRandom().nextInt(11) - 5, victim.getRandom().nextInt(22) + 42, victim.getRandom().nextInt(11) - 5);
                    if (MiscUtil.canTeleportTo(targetLocation, victim, isPlayer)) {
                        break;
                    }
                    trialTime++;
                }
                victim.teleportTo(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
                MiscUtil.addParticleServerSide((ServerWorld) victim.level, ParticleTypes.PORTAL,
                        card.getRandomX(1.2D), card.getRandomY() - 0.5d, card.getRandomZ(1.2D),
                        (victim.getRandom().nextDouble() - 0.5D) * 2.0D, (victim.getRandom().nextDouble() - 0.5D) * 2.0D, (victim.getRandom().nextDouble() - 0.5D) * 2.0D,
                        1, 80);
            }
        }
    }

    public static class TrapCard {
        public static void flameCard(CardTrapEntity trap, LivingEntity victim) {
            if (!trap.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(trap, trap.getOwner()).bypassArmor(), 4);
                victim.setSecondsOnFire(3);
            }
        }

        public static void torrentCard(CardTrapEntity trap, LivingEntity victim) {
            if (!trap.level.isClientSide()) {
                if (victim.isOnFire()) {
                    victim.clearFire();
                }
                MiscUtil.applyKnockback(victim, 0, 1.8F, 0);
            }
        }

        public static void thunderCard(CardTrapEntity trap, LivingEntity victim) {
            handleCommonThunderCardLogic(victim, trap.level.isClientSide(), trap.getOwner(), trap);
        }

        public static void bramble_card(CardTrapEntity trap, LivingEntity victim) {
            //TODO need implement
        }

        public static void earthCard(CardTrapEntity trap, LivingEntity victim) {
            if (!trap.level.isClientSide()) {
                StoneSpikeEntity stoneSpikeEntity = new StoneSpikeEntity(trap.level);
                stoneSpikeEntity.setPos(trap.getX(), trap.getY(), trap.getZ());
                trap.level.addFreshEntity(stoneSpikeEntity);
                victim.hurt(ModDamage.causeCardDamage(trap,trap.getOwner()), 6);
                victim.addEffect(new EffectInstance(Effects.WEAKNESS, 100));
                victim.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100));
            }
        }

        public static void endCard(CardTrapEntity trap, LivingEntity victim) {
            if (!trap.level.isClientSide()) {
                int trialTime = 0;
                boolean isPlayer = victim instanceof PlayerEntity;
                BlockPos blockPos = victim.blockPosition(), targetLocation = null;
                while (trialTime <= 5) {
                    targetLocation = blockPos.offset(victim.getRandom().nextInt(11) - 5, victim.getRandom().nextInt(22) + 42, victim.getRandom().nextInt(11) - 5);
                    if (MiscUtil.canTeleportTo(targetLocation, victim, isPlayer)) {
                        break;
                    }
                    trialTime++;
                }
                victim.teleportTo(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
                MiscUtil.addParticleServerSide((ServerWorld) victim.level, ParticleTypes.PORTAL,
                        trap.getX(), trap.getY() + 0.1D, trap.getZ(),
                        victim.getRandom().nextDouble() - 0.5D, victim.getRandom().nextDouble(), victim.getRandom().nextDouble() - 0.5D,
                        1, 50);
            }
        }
    }

    public static class BlockCard {
        public static void fieldCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLifetime() % 10 == 0) {
                MiscUtil.applyHolyFlameInArea((ServerWorld) tileEntity.getLevel(),
                        MiscUtil.buildAABB(tileEntity.getBlockPos(), 8), 200);
            }
        }

        public static void purificationCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLifetime() == 90) {
                MiscUtil.applyHolyFlameInArea((ServerWorld) tileEntity.getLevel(),
                        MiscUtil.buildAABB(tileEntity.getBlockPos(), 8), 200);
            }
        }

        public static void sunnyCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLevel().dimension() == World.OVERWORLD) {
                ((ServerWorld) tileEntity.getLevel()).setWeatherParameters(24000, 0, false, false);
            }
        }

        public static void rainyCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLevel().dimension() == World.OVERWORLD) {
                ((ServerWorld) tileEntity.getLevel()).setWeatherParameters(0, 24000, true, false);
            }
        }

        public static void thunderstormCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLevel().dimension() == World.OVERWORLD) {
                ((ServerWorld) tileEntity.getLevel()).setWeatherParameters(0, 24000, true, true);
            }
        }

        public static void bloomCard(SpecialCardBlockTileEntity tileEntity) {
            int randomTickRate = tileEntity.getLevel().getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
            for (int l = 0; l < randomTickRate; ++l) {
                BlockPos blockpos = MiscUtil.getBlockRandomPos(tileEntity.getBlockPos().getX(), tileEntity.getBlockPos().getY(), tileEntity.getBlockPos().getZ(), 8, tileEntity.getLevel().random);
                BlockState blockstate = tileEntity.getLevel().getBlockState(blockpos);
                if (blockstate.isRandomlyTicking()) {
                    blockstate.randomTick((ServerWorld) tileEntity.getLevel(), blockpos, tileEntity.getLevel().random);
                }
            }
        }
    }


    @FunctionalInterface
    public interface ITrapCardActivationHandler {
        void handleTrap(CardTrapEntity trap, LivingEntity victim);
    }

    @FunctionalInterface
    public interface IFlyingCardHitHandler {
        void handleHit(FlyingCardEntity card, LivingEntity victim);
    }

    @FunctionalInterface
    public interface ISpecialCardBlockServerTick {
        void handle(SpecialCardBlockTileEntity tileEntity);
    }

    private static void handleCommonThunderCardLogic(LivingEntity victim, boolean clientSide, Entity owner, Entity damageSource) {
        if (!clientSide) {
            LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(victim.level);
            lightningboltentity.setPos(victim.getX(), victim.getY(), victim.getZ());
            lightningboltentity.setVisualOnly(true);
            victim.level.addFreshEntity(lightningboltentity);
            if (victim instanceof VillagerEntity) {
                lightningboltentity.setDamage(10);
                ((VillagerEntity) victim).thunderHit((ServerWorld) victim.level, lightningboltentity);
            } else {
                victim.hurt(ModDamage.causeCardDamage(damageSource,owner), 10);
            }
        }
    }
}
