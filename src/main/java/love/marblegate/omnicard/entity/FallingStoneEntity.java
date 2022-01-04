package love.marblegate.omnicard.entity;

import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.stream.Collectors;

public class FallingStoneEntity extends Entity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<Boolean> DONE_HIT = SynchedEntityData.defineId(FlyingCardEntity.class, EntityDataSerializers.BOOLEAN);
    private int disappearCountdown;


    public FallingStoneEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public FallingStoneEntity(Level world) {
        super(EntityRegistry.FALLING_STONE.get(), world);
        disappearCountdown = 17;
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            List<LivingEntity> targets = getLivingEntityBeneath();
            if (!targets.isEmpty()) {
                for (LivingEntity livingEntity : targets) {
                    livingEntity.hurt(ModDamage.causeCardDamage(this, null), 6);
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
                }
                getEntityData().set(DONE_HIT, true);
            }
            if (isInWall()) {
                getEntityData().set(DONE_HIT, true);
            }
            if (getEntityData().get(DONE_HIT)) {
                if (disappearCountdown <= 0) {
                    remove(RemovalReason.DISCARDED);
                } else
                    disappearCountdown--;
            }
        }
        if (!getEntityData().get(DONE_HIT)) {
            setDeltaMovement(0, -0.25D, 0);
        } else {
            setDeltaMovement(0, -0.2D, 0);
        }
        move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DONE_HIT, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundNBT) {
        getEntityData().set(DONE_HIT, compoundNBT.getBoolean("done_hit"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundNBT) {
        compoundNBT.putBoolean("done_hit", getEntityData().get(DONE_HIT));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getEntityData().get(DONE_HIT)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("disappear", false));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("falling", true));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "falling_stone_controller", 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private List<LivingEntity> getLivingEntityBeneath() {
        return level.getEntities(this, getBoundingBox().expandTowards(0, -0.3, 0), entity -> entity instanceof LivingEntity)
                .stream().map(entity -> (LivingEntity) entity).collect(Collectors.toList());
    }
}
