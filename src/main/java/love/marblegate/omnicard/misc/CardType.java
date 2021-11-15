package love.marblegate.omnicard.misc;

import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.registry.EffectRegistry;
import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.NBTTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.lwjgl.system.CallbackI;

import java.util.function.Supplier;

public enum CardType {
    BLANK("white_card", CateCategory.STANDARD, ItemRegistry.BLANK_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            victim.hurt(ModDamage.causeCardDamage(card.getOwner(), card.getCardType()), 6);
        }
    }),
    RED("red_card", CateCategory.STANDARD, ItemRegistry.BLANK_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            if (victim.hasEffect(EffectRegistry.READY_TO_EXPLODE.get())) {
                Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(victim.level, victim) ? Explosion.Mode.BREAK : Explosion.Mode.NONE;
                victim.level.explode(victim, victim.getX(), victim.getY(), victim.getZ(), 3, explosion$mode);
                victim.hurt(ModDamage.causeCardDamage(card.getOwner(), card.getCardType()).setExplosion(), 6);
            }
            victim.addEffect(new EffectInstance(EffectRegistry.READY_TO_EXPLODE.get(), 30));
        }
    }),
    CORAL("orange_card", CateCategory.STANDARD, ItemRegistry.BLANK_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            victim.hurt(ModDamage.causeCardDamage(card.getOwner(), card.getCardType()), 6);
            if (victim instanceof PlayerEntity) {
                victim.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 80, 3));
                victim.addEffect(new EffectInstance(Effects.CONFUSION, 80));
            } else {
                victim.addEffect(new EffectInstance(EffectRegistry.DIZZY.get(), 80));
                victim.addEffect(new EffectInstance(Effects.CONFUSION, 80));
            }
        }
    }),
    GOLD("gold_card", CateCategory.STANDARD, ItemRegistry.BLANK_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            victim.hurt(ModDamage.causeCardDamage(card.getOwner(), card.getCardType()), 6);
            victim.addEffect(new EffectInstance(Effects.LEVITATION, 80));
            victim.addEffect(new EffectInstance(Effects.GLOWING, 80));
        }
    }),
    SEA_GREEN("green_card", CateCategory.STANDARD, ItemRegistry.BLANK_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            if (victim.isInvertedHealAndHarm()) {
                victim.hurt(ModDamage.causeCardDamage(card.getOwner(), card.getCardType()), 8);
            } else {
                victim.heal(8);
            }
        }
    }),
    AZURE("sky_card", CateCategory.STANDARD, ItemRegistry.BLANK_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){

        }
    }),
    CERULEAN_BLUE("blue_card", CateCategory.STANDARD, ItemRegistry.BLANK_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            victim.hurt(ModDamage.causeCardDamage(card.getOwner(), card.getCardType()), 6);
            victim.addEffect(new EffectInstance(Effects.BLINDNESS, 100));
        }
    }),
    HELIOTROPE("violet_card", CateCategory.STANDARD, ItemRegistry.BLANK_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            victim.hurt(ModDamage.causeCardDamage(card.getOwner(), card.getCardType()), 6);
            victim.addEffect(new EffectInstance(Effects.POISON, 80));
            victim.addEffect(new EffectInstance(EffectRegistry.POISON_NOW_LETHAL.get(), 81));
        }
    }),
    INK("black_card", CateCategory.STANDARD, ItemRegistry.BLANK_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            victim.hurt(ModDamage.causeCardDamage(card.getOwner(), card.getCardType()), 6);
            victim.addEffect(new EffectInstance(Effects.WITHER, 80));
        }
    }),

    // Elemental Card
    FLAME("flame_card", CateCategory.ELEMENTAL, ItemRegistry.FLAME_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            victim.hurt(ModDamage.causeCardDamage(card.getOwner(),card.getCardType()),4);
            victim.setSecondsOnFire(3);
        }
    }, (trap, victim) -> {
        if (!trap.level.isClientSide()){
            victim.hurt(ModDamage.causeCardDamage(trap.getOwner(),trap.getCardType()).bypassArmor(),4);
            victim.setSecondsOnFire(3);
        }
    }),
    TORRENT("torrent_card", CateCategory.ELEMENTAL,  ItemRegistry.TORRENT_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            if(victim.isOnFire()){
                victim.clearFire();
            }
            Vector3d vector3d = card.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(2D);
            if (vector3d.lengthSqr() > 0.0D) {
                push(victim,vector3d.x, 0.8D, vector3d.z);
            }
        }
    }, (trap, victim) -> {
        if (!trap.level.isClientSide()){
            if(victim.isOnFire()){
                victim.clearFire();
            }
            push(victim,0,1.8F,0);
        }
    }),
    THUNDER("thunder_card", CateCategory.ELEMENTAL,  ItemRegistry.THUNDER_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(victim.level);
            lightningboltentity.setPos(victim.getX(), victim.getY(), victim.getZ());
            lightningboltentity.setVisualOnly(true);
            victim.level.addFreshEntity(lightningboltentity);
            if(victim instanceof VillagerEntity){
                lightningboltentity.setDamage(10);
                ((VillagerEntity) victim).thunderHit((ServerWorld) victim.level,lightningboltentity);
            } else {
                victim.hurt(ModDamage.causeCardDamage(card.getOwner(), card.getCardType()), 10);
            }
        }
    }, (trap, victim) -> {
        if (!trap.level.isClientSide()){
            LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(victim.level);
            lightningboltentity.setPos(victim.getX(), victim.getY(), victim.getZ());
            lightningboltentity.setVisualOnly(true);
            victim.level.addFreshEntity(lightningboltentity);
            if(victim instanceof VillagerEntity){
                lightningboltentity.setDamage(10);
                ((VillagerEntity) victim).thunderHit((ServerWorld) victim.level,lightningboltentity);
            } else {
                victim.hurt(ModDamage.causeCardDamage(trap.getOwner(), trap.getCardType()), 10);
            }
        }
    }),
    BRAMBLE("bramble_card", CateCategory.ELEMENTAL,  ItemRegistry.BRAMBLE_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){

        }
    }, (trap, victim) -> {
        if (!trap.level.isClientSide()){

        }
    }),
    EARTH("earth_card", CateCategory.ELEMENTAL,  ItemRegistry.EARTH_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){

        }
    }, (trap, victim) -> {
        if (!trap.level.isClientSide()){

        }
    }),
    END("end_card", CateCategory.ELEMENTAL,  ItemRegistry.END_CARD, (card, victim) -> {
        if (!card.level.isClientSide()){
            int trialTime = 0;
            boolean isPlayer = victim instanceof PlayerEntity;
            BlockPos blockPos = victim.blockPosition(), targetLocation = null;
            while(trialTime<=5){
                targetLocation = blockPos.offset(victim.getRandom().nextInt(11)-5,victim.getRandom().nextInt(22)+42,victim.getRandom().nextInt(11)-5);
                if(canTeleportTo(targetLocation,victim,isPlayer)){
                    break;
                }
                trialTime++;
            }
            victim.teleportTo(targetLocation.getX(),targetLocation.getY(),targetLocation.getZ());
            addParticleServerSide((ServerWorld) victim.level,ParticleTypes.PORTAL,
                    card.getRandomX(1.2D), card.getRandomY()-0.5d, card.getRandomZ(1.2D),
                    (victim.getRandom().nextDouble() - 0.5D) * 2.0D, (victim.getRandom().nextDouble() - 0.5D) * 2.0D, (victim.getRandom().nextDouble() - 0.5D) * 2.0D,
                    1, 80);
        }
    }, (trap, victim) -> {
        if (!trap.level.isClientSide()){
            int trialTime = 0;
            boolean isPlayer = victim instanceof PlayerEntity;
            BlockPos blockPos = victim.blockPosition(), targetLocation = null;
            while(trialTime<=5){
                targetLocation = blockPos.offset(victim.getRandom().nextInt(11)-5,victim.getRandom().nextInt(22)+42,victim.getRandom().nextInt(11)-5);
                if(canTeleportTo(targetLocation,victim,isPlayer)){
                    break;
                }
                trialTime++;
            }
            victim.teleportTo(targetLocation.getX(),targetLocation.getY(),targetLocation.getZ());
            addParticleServerSide((ServerWorld) victim.level,ParticleTypes.PORTAL,
                    trap.getX(), trap.getY() + 0.1D, trap.getZ(),
                    victim.getRandom().nextDouble() - 0.5D, victim.getRandom().nextDouble(), victim.getRandom().nextDouble() - 0.5D,
                    1, 50);
        }
    }),

    // Special Card
    FIELD("field_card", CateCategory.SPECIAL, ItemRegistry.FIELD_CARD),
    PURIFICATION("purification_card", CateCategory.SPECIAL),
    SEAL("seal_card", CateCategory.SPECIAL, ItemRegistry.SEAL_CARD),
    SUNNY("sunny_card", CateCategory.SPECIAL),
    RAINY("rainy_card", CateCategory.SPECIAL),
    THUNDERSTORM("thunderstorm_card", CateCategory.SPECIAL),
    BLOOM("bloom_card", CateCategory.SPECIAL, ItemRegistry.BLOOM_CARD);


    public String name;
    public CateCategory category;
    public Supplier<Item> entityDroppedItem;
    public ITrapCardActivationHandler trapCardActivationHandler;
    public IFlyingCardHitHandler flyingCardHitHandler;

    CardType(String i, CateCategory category) {
        name = i;
        this.category = category;
    }

    CardType(String i, CateCategory category, Supplier<Item> entityDroppedItem) {
        name = i;
        this.category = category;
        this.entityDroppedItem = entityDroppedItem;
    }

    CardType(String i, CateCategory category, Supplier<Item> entityDroppedItem, IFlyingCardHitHandler flyingCardHitHandler) {
        name = i;
        this.category = category;
        this.entityDroppedItem = entityDroppedItem;
        this.flyingCardHitHandler = flyingCardHitHandler;
    }

    CardType(String i, CateCategory category, Supplier<Item> entityDroppedItem, IFlyingCardHitHandler flyingCardHitHandler, ITrapCardActivationHandler trapCardActivationHandler) {
        name = i;
        this.category = category;
        this.entityDroppedItem = entityDroppedItem;
        this.flyingCardHitHandler = flyingCardHitHandler;
        this.trapCardActivationHandler = trapCardActivationHandler;
    }

    public String getTexturePath() {
        return category.path + name + ".png";
    }

    @FunctionalInterface
    public interface ITrapCardActivationHandler{
        void handleTrap(CardTrapEntity trap, LivingEntity victim);
    }

    @FunctionalInterface
    public interface IFlyingCardHitHandler{
        void handleHit(FlyingCardEntity card, LivingEntity victim);
    }

    public enum CateCategory {
        STANDARD("standard/"),
        ELEMENTAL("elemental/"),
        SPECIAL("special/l");

        public String path;

        CateCategory(String i) {
            path = i;
        }
    }

    private static boolean canTeleportTo(BlockPos pos, LivingEntity livingEntity, boolean isPlayer){
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


    private static void push(LivingEntity livingEntity,double x, double y, double z) {
        livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(x, y, z));
        livingEntity.hurtMarked = true;
        livingEntity.hasImpulse = true;
    }

    private static <T extends IParticleData> void addParticleServerSide(ServerWorld world, T particle, double x, double y, double z, double xDist, double yDist, double zDist, double maxSpeed, int count){
        world.sendParticles(particle,x,y,z,count,xDist,yDist,zDist,maxSpeed);
    }

}
