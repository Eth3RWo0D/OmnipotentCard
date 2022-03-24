package love.marblegate.omnicard.card.function;

import love.marblegate.omnicard.block.blockentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FallingStoneEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.entity.StoneSpikeEntity;
import love.marblegate.omnicard.misc.ClientMiscUtil;
import love.marblegate.omnicard.misc.MiscUtil;
import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.MobEffectRegistry;
import love.marblegate.omnicard.registry.SoundRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;

public class CardFunc {
    public static class HitEntity {
        public static void whiteCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card, card.getOwner()), 6);
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.COLORED_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void redCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                if (victim.hasEffect(MobEffectRegistry.READY_TO_EXPLODE.get())) {
                    Explosion.BlockInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(victim.level, victim) ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE;
                    victim.level.explode(victim, ModDamage.causeCardDamage(card, card.getOwner()).setExplosion(), null, victim.getX(), victim.getY(), victim.getZ(), 0.71F, false, explosion$mode);
                }
                victim.addEffect(new MobEffectInstance(MobEffectRegistry.READY_TO_EXPLODE.get(), 30));
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.COLORED_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void orangeCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card, card.getOwner()), 6);
                if (victim instanceof Player) {
                    victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 3));
                } else {
                    victim.addEffect(new MobEffectInstance(MobEffectRegistry.DIZZY.get(), 80));
                }
                victim.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.COLORED_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void goldCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card, card.getOwner()), 6);
                victim.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 80));
                victim.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80));
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.COLORED_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void greenCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                if (victim.isInvertedHealAndHarm()) {
                    victim.hurt(ModDamage.causeCardDamage(card, card.getOwner()), 8);
                } else {
                    victim.heal(8);
                }
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.COLORED_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void skyCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60));
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.COLORED_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void blueCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card, card.getOwner()), 6);
                victim.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.COLORED_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void violetCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card, card.getOwner()), 6);
                victim.addEffect(new MobEffectInstance(MobEffects.POISON, 80));
                victim.addEffect(new MobEffectInstance(MobEffectRegistry.POISON_NOW_LETHAL.get(), 81));
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.COLORED_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void blackCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card, card.getOwner()), 6);
                victim.addEffect(new MobEffectInstance(MobEffects.WITHER, 80));
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.COLORED_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void flameCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card, card.getOwner()), 4);
                victim.setSecondsOnFire(3);
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.ELEMENTAL_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                MiscUtil.addParticle((ServerLevel) victim.level, ParticleTypes.FLAME,
                        card.getRandomX(1.2D), card.getRandomY() - 0.5d, card.getRandomZ(1.2D),
                        card.getDeltaMovement().normalize().x, card.getDeltaMovement().normalize().y, card.getDeltaMovement().normalize().z,
                        1, 50);
            }
        }

        public static void torrentCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                if (victim.isOnFire()) {
                    victim.clearFire();
                }
                Vec3 vector3d = card.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(2D);
                if (vector3d.lengthSqr() > 0.0D) {
                    MiscUtil.applyKnockback(victim, vector3d.x, 0.8D, vector3d.z);
                }
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.ELEMENTAL_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                MiscUtil.addParticle((ServerLevel) victim.level, ParticleTypes.RAIN,
                        card.getRandomX(1.2D), card.getRandomY() - 0.5d, card.getRandomZ(1.2D),
                        card.getDeltaMovement().normalize().x, card.getDeltaMovement().normalize().y, card.getDeltaMovement().normalize().z,
                        1, 70);
            }
        }

        public static void thunderCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                handleCommonThunderCardLogic(victim, card.getOwner(), card);
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.ELEMENTAL_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void brambleCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(card, card.getOwner()), 4);
                victim.addEffect(new MobEffectInstance(MobEffects.POISON, 80));
                victim.addEffect(new MobEffectInstance(MobEffectRegistry.POISON_NOW_LETHAL.get(), 81));
                victim.addEffect(new MobEffectInstance(MobEffectRegistry.DO_NOT_MOVE.get(), 60));
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.ELEMENTAL_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                handleCommonBrambleCardPlantBush(victim);
            }
        }

        public static void earthCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                FallingStoneEntity fallingStoneEntity = new FallingStoneEntity(card.level);
                fallingStoneEntity.setPos(victim.getX(), victim.getY() + 3, victim.getZ());
                card.level.addFreshEntity(fallingStoneEntity);
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.ELEMENTAL_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void endCard(FlyingCardEntity card, LivingEntity victim) {
            if (!card.level.isClientSide()) {
                handleCommonEndCardTeleport(victim);
                MiscUtil.addParticle((ServerLevel) victim.level, ParticleTypes.PORTAL,
                        card.getRandomX(1.2D), card.getRandomY() - 0.5d, card.getRandomZ(1.2D),
                        (victim.getRandom().nextDouble() - 0.5D) * 2.0D, (victim.getRandom().nextDouble() - 0.5D) * 2.0D, (victim.getRandom().nextDouble() - 0.5D) * 2.0D,
                        1, 80);
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.ELEMENTAL_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            }
        }
    }

    public static class HitBlock{
        public static void torrentCard(FlyingCardEntity card, BlockPos pos, Direction face){
            if(!card.level.isClientSide()){
                BlockState blockState = card.level.getBlockState(pos);
                boolean worked = false;
                if(blockState.getBlock() instanceof AbstractCandleBlock || blockState.getBlock() instanceof CampfireBlock){
                    if(blockState.getValue(BlockStateProperties.LIT)) {
                        card.level.setBlockAndUpdate(pos,blockState.setValue(BlockStateProperties.LIT,false));
                        worked = true;
                    }
                }
                for(BlockPos blockPos: BlockPos.betweenClosed(pos.above().east().south(),pos.below().north().west())){
                    if(card.level.getBlockState(blockPos).getBlock() instanceof BaseFireBlock){
                        card.level.setBlockAndUpdate(blockPos,Blocks.AIR.defaultBlockState());
                        worked = true;
                    }
                }
                if(blockState.is(Blocks.MAGMA_BLOCK)){
                    card.level.setBlockAndUpdate(pos,Blocks.COBBLESTONE.defaultBlockState());
                    worked = true;
                }
                if(worked){
                    card.level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), SoundRegistry.ELEMENTAL_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    MiscUtil.addParticle((ServerLevel) card.level, ParticleTypes.RAIN,
                            card.getRandomX(1.2D), card.getRandomY() - 0.5d, card.getRandomZ(1.2D),
                            card.getDeltaMovement().normalize().x, card.getDeltaMovement().normalize().y, card.getDeltaMovement().normalize().z,
                            1, 70);
                    card.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }

        public static void flameCard(FlyingCardEntity card, BlockPos pos, Direction face){
            if(!card.level.isClientSide()){
                BlockState blockState = card.level.getBlockState(pos);
                boolean worked = false;
                if(CampfireBlock.canLight(blockState) || CandleBlock.canLight(blockState) || CandleCakeBlock.canLight(blockState)){
                    card.level.setBlockAndUpdate(pos,blockState.setValue(BlockStateProperties.LIT,true));
                    worked = true;
                }
                if(blockState.isFlammable(card.level,pos,face) || blockState.is(Blocks.OBSIDIAN)){
                    if(tryCatchFire(card.level, pos))
                        worked = true;
                }
                if(blockState.is(Blocks.TNT)){
                    if(card.getOwner() instanceof LivingEntity)
                        blockState.onCaughtFire(card.level,pos,face, (LivingEntity) card.getOwner());
                    else blockState.onCaughtFire(card.level,pos,face, (LivingEntity) null);
                    card.level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
                    worked = true;
                }
                if(worked){
                    card.level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), SoundRegistry.ELEMENTAL_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    MiscUtil.addParticle((ServerLevel) card.level, ParticleTypes.FLAME,
                            card.getRandomX(1.2D), card.getRandomY() - 0.5d, card.getRandomZ(1.2D),
                            card.getDeltaMovement().normalize().x, card.getDeltaMovement().normalize().y, card.getDeltaMovement().normalize().z,
                            1, 50);
                    card.remove(Entity.RemovalReason.DISCARDED);
                }
            }

        }

        private static boolean tryCatchFire(Level level, BlockPos pos){
            boolean worked = false;
            if(level.getBlockState(pos.above()).isAir()){
                level.setBlockAndUpdate(pos.above(),Blocks.FIRE.defaultBlockState());
                worked = true;
            }
            if(level.getBlockState(pos.below()).isAir()){
                level.setBlockAndUpdate(pos.below(),Blocks.FIRE.defaultBlockState());
                worked = true;
            }
            if(level.getBlockState(pos.south()).isAir()){
                level.setBlockAndUpdate(pos.south(),Blocks.FIRE.defaultBlockState());
                worked = true;
            }
            if(level.getBlockState(pos.north()).isAir()){
                level.setBlockAndUpdate(pos.north(),Blocks.FIRE.defaultBlockState());
                worked = true;
            }
            if(level.getBlockState(pos.east()).isAir()){
                level.setBlockAndUpdate(pos.east(),Blocks.FIRE.defaultBlockState());
                worked = true;
            }
            if(level.getBlockState(pos.west()).isAir()){
                level.setBlockAndUpdate(pos.west(),Blocks.FIRE.defaultBlockState());
                worked = true;
            }
            return worked;
        }
    }

    public static class CardOnFly{
        public static void defaultCard(FlyingCardEntity card){
            if(card.level.isClientSide()){
                if(!card.justBeenThrown()){
                    Vec3 vec3 = card.getDeltaMovement();
                    double d0 = card.getX() + vec3.x;
                    double d1 = card.getY() + vec3.y;
                    double d2 = card.getZ() + vec3.z;
                    if (card.isInWater()) {
                        for (int i = 0; i < 4; ++i) {
                            card.level.addAlwaysVisibleParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                        }
                    }
                    card.level.addAlwaysVisibleParticle(ParticleTypes.FIREWORK, d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
                }
            }
            else
                System.out.println("Well it is Serverside!");
                ProjectileUtil.rotateTowardsMovement(card, 0.2F);

        }

        public static void noEffect(FlyingCardEntity card){}
    }


    public static class ActivateTrap {
        public static void flameCard(CardTrapEntity trap, LivingEntity victim) {
            if (!trap.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(trap, trap.getOwner()).bypassArmor(), 4);
                victim.setSecondsOnFire(3);
                MiscUtil.addParticle((ServerLevel) victim.level, ParticleTypes.FLAME, trap.getX(), trap.getY() + 0.1D, trap.getZ(),
                        victim.getRandom().nextDouble() / 10, victim.getRandom().nextDouble() * 2, victim.getRandom().nextDouble() / 10,
                        1, 30);
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.TRAP_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void torrentCard(CardTrapEntity trap, LivingEntity victim) {
            if (!trap.level.isClientSide()) {
                if (victim.isOnFire()) {
                    victim.clearFire();
                }
                MiscUtil.applyKnockback(victim, 0, 1.8F, 0);
                MiscUtil.addParticle((ServerLevel) victim.level, ParticleTypes.RAIN, trap.getX(), trap.getY() + 0.1D, trap.getZ(),
                        victim.getRandom().nextDouble() / 10, victim.getRandom().nextDouble() * 2, victim.getRandom().nextDouble() / 10,
                        1, 50);
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.TRAP_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void thunderCard(CardTrapEntity trap, LivingEntity victim) {

            if (!trap.level.isClientSide()) {
                handleCommonThunderCardLogic(victim, trap.getOwner(), trap);
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.TRAP_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void bramble_card(CardTrapEntity trap, LivingEntity victim) {
            if (!victim.level.isClientSide()) {
                victim.hurt(ModDamage.causeCardDamage(trap, trap.getOwner()), 4);
                victim.addEffect(new MobEffectInstance(MobEffects.POISON, 80));
                victim.addEffect(new MobEffectInstance(MobEffectRegistry.POISON_NOW_LETHAL.get(), 81));
                victim.addEffect(new MobEffectInstance(MobEffectRegistry.DO_NOT_MOVE.get(), 60));
                handleCommonBrambleCardPlantBush(victim);
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.TRAP_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void earthCard(CardTrapEntity trap, LivingEntity victim) {
            if (!trap.level.isClientSide()) {
                StoneSpikeEntity stoneSpikeEntity = new StoneSpikeEntity(trap.level);
                stoneSpikeEntity.setPos(trap.getX(), trap.getY(), trap.getZ());
                trap.level.addFreshEntity(stoneSpikeEntity);
                victim.hurt(ModDamage.causeCardDamage(trap, trap.getOwner()), 6);
                victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100));
                victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.TRAP_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        public static void endCard(CardTrapEntity trap, LivingEntity victim) {
            if (!trap.level.isClientSide()) {
                handleCommonEndCardTeleport(victim);
                MiscUtil.addParticle((ServerLevel) victim.level, ParticleTypes.PORTAL, trap.getX(), trap.getY() + 0.1D, trap.getZ(),
                        victim.getRandom().nextDouble() - 0.5D, victim.getRandom().nextDouble(), victim.getRandom().nextDouble() - 0.5D,
                        1, 50);
                victim.level.playSound((Player) null, victim.getX(), victim.getY(), victim.getZ(), SoundRegistry.TRAP_CARD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    public static class RunningField {
        public static void fieldCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLifetime() % 10 == 0) {
                MiscUtil.applyHolyFlameInArea((ServerLevel) tileEntity.getLevel(),
                        MiscUtil.buildAABB(tileEntity.getBlockPos(), 8), 200);
            }
            if (tileEntity.getLifetime() % 300 == 0) {
                playBlockCardOnRunSoundByChance(tileEntity.getLevel(), tileEntity.getBlockPos(), 0.2);
            }
        }

        public static void fieldCardClient(SpecialCardBlockTileEntity tileEntity) {
            int i = (int) (tileEntity.getLevel().getDayTime() % 32);
            ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.LAVA,
                    tileEntity.getBlockPos().getX() - 8 + (i * 0.5), tileEntity.getBlockPos().getY() + 8 + 0.5, tileEntity.getBlockPos().getZ() - 8,
                    0, 0, 0, 1);
            ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.LAVA,
                    tileEntity.getBlockPos().getX() - 8, tileEntity.getBlockPos().getY() + 8 + 0.5, tileEntity.getBlockPos().getZ() + 8 - (i * 0.5),
                    0, 0, 0, 1);
            ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.LAVA,
                    tileEntity.getBlockPos().getX() + 8, tileEntity.getBlockPos().getY() + 8 + 0.5, tileEntity.getBlockPos().getZ() - 8 + (i * 0.5),
                    0, 0, 0, 1);
            ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.LAVA,
                    tileEntity.getBlockPos().getX() + 8 - (i * 0.5), tileEntity.getBlockPos().getY() + 8 + 0.5, tileEntity.getBlockPos().getZ() + 8,
                    0, 0, 0, 1);
        }

        public static void sealCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLifetime() % 300 == 0) {
                playBlockCardOnRunSoundByChance(tileEntity.getLevel(), tileEntity.getBlockPos(), 0.2);
            }
        }

        public static void sealCardClient(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLifetime() % 20 == 0) {
                for (int i = 0; i < 16; i++) {
                    ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.FALLING_OBSIDIAN_TEAR,
                            tileEntity.getBlockPos().getX() - 8 + i, tileEntity.getBlockPos().getY() + 8 + 0.5, tileEntity.getBlockPos().getZ() - 8,
                            0, 0, 0, 1);
                    ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.FALLING_OBSIDIAN_TEAR,
                            tileEntity.getBlockPos().getX() - 8, tileEntity.getBlockPos().getY() + 8 + 0.5, tileEntity.getBlockPos().getZ() + 8 - i,
                            0, 0, 0, 1);
                    ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.FALLING_OBSIDIAN_TEAR,
                            tileEntity.getBlockPos().getX() + 8, tileEntity.getBlockPos().getY() + 8 + 0.5, tileEntity.getBlockPos().getZ() - 8 + i,
                            0, 0, 0, 1);
                    ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.FALLING_OBSIDIAN_TEAR,
                            tileEntity.getBlockPos().getX() + 8 - i, tileEntity.getBlockPos().getY() + 8 + 0.5, tileEntity.getBlockPos().getZ() + 8,
                            0, 0, 0, 1);
                }
            }
        }

        public static void purificationCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLifetime() == 90) {
                MiscUtil.applyHugeDamageThenApplyFireInArea((ServerLevel) tileEntity.getLevel(),
                        MiscUtil.buildAABB(tileEntity.getBlockPos(), 8), 20,3);
            }
        }

        public static void purificationCardClient(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLifetime() <= 90 && tileEntity.getLifetime() > 58 && tileEntity.getLifetime() % 4 == 0) {
                int h = (90 - tileEntity.getLifetime()) / 2;
                for (int i = 0; i < 8; i++) {
                    ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.LAVA,
                            tileEntity.getBlockPos().getX() - 8 + 0.5 + 2 * i, tileEntity.getBlockPos().getY() - 8 + h + 0.5, tileEntity.getBlockPos().getZ() - 8 + 0.5,
                            0, 0, 0, 1);
                    ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.LAVA,
                            tileEntity.getBlockPos().getX() - 8 + 0.5, tileEntity.getBlockPos().getY() - 8 + h + 0.5, tileEntity.getBlockPos().getZ() + 8 + 0.5 - 2 * i,
                            0, 0, 0, 1);
                    ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.LAVA,
                            tileEntity.getBlockPos().getX() + 8 + 0.5, tileEntity.getBlockPos().getY() - 8 + h + 0.5, tileEntity.getBlockPos().getZ() - 8 + 0.5 + 2 * i,
                            0, 0, 0, 1);
                    ClientMiscUtil.addParticle((ClientLevel) tileEntity.getLevel(), ParticleTypes.LAVA,
                            tileEntity.getBlockPos().getX() + 8 + 0.5 - 2 * i, tileEntity.getBlockPos().getY() - 8 + h + 0.5, tileEntity.getBlockPos().getZ() + 8 + 0.5,
                            0, 0, 0, 1);
                }
            }
        }

        public static void sunnyCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLifetime() == 90) {
                if (tileEntity.getLevel().dimension() == Level.OVERWORLD) {
                    ((ServerLevel) tileEntity.getLevel()).setWeatherParameters(24000, 0, false, false);
                }
            }
        }

        public static void rainyCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLifetime() == 90) {
                if (tileEntity.getLevel().dimension() == Level.OVERWORLD) {
                    ((ServerLevel) tileEntity.getLevel()).setWeatherParameters(0, 24000, true, false);
                }
            }

        }

        public static void thunderstormCard(SpecialCardBlockTileEntity tileEntity) {
            if (tileEntity.getLifetime() == 90) {
                if (tileEntity.getLevel().dimension() == Level.OVERWORLD) {
                    ((ServerLevel) tileEntity.getLevel()).setWeatherParameters(0, 24000, true, true);
                }
            }
        }

        public static void bloomCard(SpecialCardBlockTileEntity tileEntity) {
            int randomTickRate = tileEntity.getLevel().getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
            for (int l = 0; l < randomTickRate; ++l) {
                BlockPos blockpos = MiscUtil.getBlockRandomPos(tileEntity.getBlockPos().getX(), tileEntity.getBlockPos().getY(), tileEntity.getBlockPos().getZ(), 8, tileEntity.getLevel().random);
                BlockState blockstate = tileEntity.getLevel().getBlockState(blockpos);
                if (blockstate.isRandomlyTicking()) {
                    blockstate.randomTick((ServerLevel) tileEntity.getLevel(), blockpos, tileEntity.getLevel().random);
                }
            }
            if (tileEntity.getLifetime() % 300 == 0) {
                playBlockCardOnRunSoundByChance(tileEntity.getLevel(), tileEntity.getBlockPos(), 0.2);
            }
        }
    }


    private static void handleCommonThunderCardLogic(LivingEntity victim, Entity owner, Entity damageSource) {
        LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(victim.level);
        lightningboltentity.setPos(victim.getX(), victim.getY(), victim.getZ());
        lightningboltentity.setVisualOnly(true);
        victim.level.addFreshEntity(lightningboltentity);
        victim.thunderHit((ServerLevel) victim.level, lightningboltentity);
        victim.hurt(ModDamage.causeCardDamage(damageSource, owner), 10);
    }

    private static void handleCommonEndCardTeleport(LivingEntity victim) {
        int trialTime = 0;
        boolean isPlayer = victim instanceof Player;
        BlockPos blockPos = victim.blockPosition(), targetLocation = null;
        while (trialTime <= 5) {
            targetLocation = blockPos.offset(victim.getRandom().nextInt(11) - 5, victim.getRandom().nextInt(22) + 42, victim.getRandom().nextInt(11) - 5);
            if (MiscUtil.canTeleportTo(targetLocation, victim, isPlayer)) {
                break;
            }
            trialTime++;
        }
        victim.teleportTo(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
    }

    private static void handleCommonBrambleCardPlantBush(LivingEntity victim) {
        BlockPos pos = victim.blockPosition();
        if (victim.level.getBlockState(pos).getBlock().equals(Blocks.AIR) && victim.level.getBlockState(pos.below()).canSustainPlant((BlockGetter) victim.level, pos, Direction.UP, (IPlantable) Blocks.SWEET_BERRY_BUSH)) {
            victim.level.setBlockAndUpdate(pos, Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(SweetBerryBushBlock.AGE,3));
        }
    }

    private static void playBlockCardOnRunSoundByChance(Level level, BlockPos pos, double chance) {
        if (Math.random() < chance) {
            level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), SoundRegistry.BLOCK_CARD_ON_RUN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}
