package love.marblegate.omnicard.entity;

import love.marblegate.omnicard.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class StoneSpikeEntity extends Entity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> DONE_STRIKE = EntityDataManager.defineId(FlyingCardEntity.class, DataSerializers.BOOLEAN);
    private int lifetime;


    public StoneSpikeEntity(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public StoneSpikeEntity(World world) {
        super(EntityRegistry.STONE_SPIKE.get(), world);
        lifetime = 25;
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            if (lifetime <= 0)
                remove();
            else
                lifetime--;
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DONE_STRIKE, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compoundNBT) {
        getEntityData().set(DONE_STRIKE, compoundNBT.getBoolean("done_strike"));
        lifetime = compoundNBT.getInt("done_strike");
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compoundNBT) {
        compoundNBT.putBoolean("done_strike", getEntityData().get(DONE_STRIKE));
        compoundNBT.putInt("lifetime", lifetime);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
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
        data.addAnimationController(new AnimationController<>(this, "falling_stone_controller", 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
