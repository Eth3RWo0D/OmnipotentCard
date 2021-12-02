package love.marblegate.omnicard.entity;

import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
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
    private static final DataParameter<Boolean> DONE_HIT = EntityDataManager.defineId(FlyingCardEntity.class, DataSerializers.BOOLEAN);
    private int disappearCountdown;


    public FallingStoneEntity(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public FallingStoneEntity(World world) {
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
                    livingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 100));
                    livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100));
                }
                getEntityData().set(DONE_HIT, true);
            }
            if (isInWall()) {
                getEntityData().set(DONE_HIT, true);
            }
            if (getEntityData().get(DONE_HIT)) {
                if (disappearCountdown <= 0) {
                    remove();
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
    protected void readAdditionalSaveData(CompoundNBT compoundNBT) {
        getEntityData().set(DONE_HIT, compoundNBT.getBoolean("done_hit"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compoundNBT) {
        compoundNBT.putBoolean("done_hit", getEntityData().get(DONE_HIT));
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
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
