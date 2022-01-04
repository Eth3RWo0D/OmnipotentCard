package love.marblegate.omnicard.entity;

import love.marblegate.omnicard.card.CommonCard;
import love.marblegate.omnicard.card.CommonCards;
import love.marblegate.omnicard.registry.EntityRegistry;
import love.marblegate.omnicard.registry.ParticleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;


public class FlyingCardEntity extends DamagingProjectileEntity implements IAnimatable, IEntityAdditionalSpawnData {
    private final AnimationFactory factory = new AnimationFactory(this);
    private CommonCard card;
    private static final DataParameter<Boolean> CAN_PICK_UP = EntityDataManager.defineId(FlyingCardEntity.class, DataSerializers.BOOLEAN);
    private int remainingLifeTime;

    public FlyingCardEntity(EntityType<? extends FlyingCardEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public FlyingCardEntity(LivingEntity livingEntity, double xPower, double yPower, double zPower, World world, CommonCard card) {
        super(EntityRegistry.FLYING_CARD.get(), livingEntity, xPower, yPower, zPower, world);
        this.card = card;
        remainingLifeTime = 60 * 20;
        setPickUpStatus(false);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (!canPickUp()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("cardfly_normal", true));
            return PlayState.CONTINUE;
        } else {
            return PlayState.STOP;
        }
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "card_controller", 0, this::predicate));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CAN_PICK_UP, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide()) {
            // This Height limit needs to be changed in 1.17
            if (blockPosition().getY() >= 300)
                remove();
            if (remainingLifeTime <= 0) {
                if (card.getRetrievedItem().isPresent())
                    this.spawnAtLocation(card.getRetrievedItem().get().getDefaultInstance(), 0.1F);
                remove();
            } else {
                remainingLifeTime -= 1;
                // Pickup Card on Ground
                if (canPickUp() && qualifiedToBeRetrieved()) {
                    this.spawnAtLocation(card.getRetrievedItem().get().getDefaultInstance(), 0.1F);
                    remove();
                }
            }
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult) {
        super.onHit(rayTraceResult);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        super.onHitEntity(entityRayTraceResult);
        Entity entity = entityRayTraceResult.getEntity();
        if (entity instanceof LivingEntity && !canPickUp()) {
            card.hit(this, (LivingEntity) entity);
            remove();
        }
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        stayOnBlock(blockRayTraceResult);
    }

    private boolean qualifiedToBeRetrieved() {
        return !level.getEntities(this, this.getBoundingBox(), (entity -> entity instanceof PlayerEntity)).isEmpty();
    }

    private void stayOnBlock(BlockRayTraceResult blockRayTraceResult) {
        Vector3d vector3d = blockRayTraceResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vector3d);
        Vector3d vector3d1 = vector3d.normalize().scale((double) 0.05F);
        this.setPosRaw(this.getX() - vector3d1.x, this.getY() - vector3d1.y, this.getZ() - vector3d1.z);
        setPickUpStatus(true);
    }

    private boolean canPickUp() {
        return this.entityData.get(CAN_PICK_UP);
    }

    private void setPickUpStatus(boolean b) {
        entityData.set(CAN_PICK_UP, b);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected IParticleData getTrailParticle() {
        if (!canPickUp()) {
            return ParticleTypes.FIREWORK;
        } else
            return ParticleRegistry.EMPTY.get();

    }

    public CommonCard getCardType() {
        return card;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        card = CommonCards.fromByte(compoundNBT.getByte("card_type"));
        remainingLifeTime = compoundNBT.getInt("remaining_life_time");
        setPickUpStatus(compoundNBT.getBoolean("can_pickup"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putByte("card_type", CommonCards.toByte(card));
        compoundNBT.putInt("remaining_life_time", remainingLifeTime);
        compoundNBT.putBoolean("can_pickup", canPickUp());
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeByte(CommonCards.toByte(card));
        buffer.writeInt(remainingLifeTime);
        buffer.writeBoolean(canPickUp());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        card = CommonCards.fromByte(additionalData.readByte());
        remainingLifeTime = additionalData.readInt();
        setPickUpStatus(additionalData.readBoolean());
    }

}
