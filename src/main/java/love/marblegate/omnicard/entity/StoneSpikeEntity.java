package love.marblegate.omnicard.entity;

import love.marblegate.omnicard.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class StoneSpikeEntity extends Entity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<Boolean> DONE_STRIKE = SynchedEntityData.defineId(FlyingCardEntity.class, EntityDataSerializers.BOOLEAN);
    private int lifetime;


    public StoneSpikeEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public StoneSpikeEntity(Level world) {
        super(EntityRegistry.STONE_SPIKE.get(), world);
        lifetime = 25;
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            if (lifetime <= 0)
                remove(RemovalReason.DISCARDED);
            else
                lifetime--;
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DONE_STRIKE, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundNBT) {
        getEntityData().set(DONE_STRIKE, compoundNBT.getBoolean("done_strike"));
        lifetime = compoundNBT.getInt("done_strike");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundNBT) {
        compoundNBT.putBoolean("done_strike", getEntityData().get(DONE_STRIKE));
        compoundNBT.putInt("lifetime", lifetime);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getEntityData().get(DONE_STRIKE))
            return PlayState.STOP;
        else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("rise", false));
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
}
