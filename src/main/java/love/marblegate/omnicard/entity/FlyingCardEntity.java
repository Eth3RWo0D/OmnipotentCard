package love.marblegate.omnicard.entity;

import love.marblegate.omnicard.card.CommonCard;
import love.marblegate.omnicard.card.CommonCards;
import love.marblegate.omnicard.misc.Configuration;
import love.marblegate.omnicard.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;


public class FlyingCardEntity extends ProjectileEntity implements IAnimatable, IEntityAdditionalSpawnData {
    private double xPower;
    private double yPower;
    private double zPower;
    private final AnimationFactory factory = new AnimationFactory(this);
    private CommonCard card;
    private static final DataParameter<Boolean> CAN_PICK_UP = EntityDataManager.defineId(FlyingCardEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> LIFETIME = EntityDataManager.defineId(FlyingCardEntity.class, DataSerializers.INT);

    public FlyingCardEntity(EntityType<? extends FlyingCardEntity> entityType, World world) {
        super(entityType, world);
    }

    public FlyingCardEntity(LivingEntity livingEntity, double xPower, double yPower, double zPower, World world, CommonCard card) {
        super(EntityRegistry.FLYING_CARD.get(), world);
        this.moveTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), this.yRot, this.xRot);
        this.reapplyPosition();
        double d0 = (double) MathHelper.sqrt(xPower * xPower + yPower * yPower + zPower * zPower);
        if (d0 != 0.0D) {
            this.xPower = xPower / d0 * 0.1D;
            this.yPower = yPower / d0 * 0.1D;
            this.zPower = zPower / d0 * 0.1D;
        }
        this.card = card;
        setRemainingLifetime(60 * 20);
        setPickUpStatus(false);
    }

    private PlayState predicate(AnimationEvent<FlyingCardEntity> event) {
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
        this.entityData.define(CAN_PICK_UP, false);
        this.entityData.define(LIFETIME, 20*60);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 = d0 * 128.0D;
        return p_70112_1_ < d0 * d0;
    }

    @Override
    public void tick() {
        //Removal Check
        Entity entity = this.getOwner();
        if (!level.isClientSide()) {
            // This Height limit needs to be changed in 1.17
            if (blockPosition().getY() >= 300)
                remove();
            if (getRemainingLifetime() <= 0) {
                if (card.getRetrievedItem().isPresent())
                    this.spawnAtLocation(card.getRetrievedItem().get().getDefaultInstance(), 0.1F);
                remove();
            } else {
                setRemainingLifetime(getRemainingLifetime()-1);
                // Pickup Card on Ground
                if (canPickUp() && qualifiedToBeRetrieved()) {
                    this.spawnAtLocation(card.getRetrievedItem().get().getDefaultInstance(), 0.1F);
                    remove();
                }
            }
        }

        //Handle Movement & Hit
        if (this.level.isClientSide || (entity == null || !entity.removed) && this.level.hasChunkAt(this.blockPosition())) {
            super.tick();

            RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }
            if(!canPickUp()){
                card.onFly(this);
            }
            this.checkInsideBlocks();
            Vector3d vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            float f = this.getInertia();
            if (this.isInWater()) {
                f = 0.8F;
            }
            this.setDeltaMovement(vector3d.add(this.xPower, this.yPower, this.zPower).scale(f));
            this.setPos(d0, d1, d2);
        } else {
            this.remove();
        }


    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && !entity.noPhysics;
    }

    protected float getInertia() {
        return 0.99F;
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
            if((!Configuration.HURT_MOUNT.get() && entity.getPassengers().stream().anyMatch(entity1 -> {
                if (getOwner() != null)
                    return entity1.getUUID().equals(getOwner().getUUID());
                else return false;
            })) || (!Configuration.HURT_PET.get() && isPet(entity))){
                this.spawnAtLocation(card.getRetrievedItem().get().getDefaultInstance(), 0.1F);
                remove();
            } else {
                card.hitEntity(this, (LivingEntity) entity);
                remove();
            }
        }
    }

    private boolean isPet(Entity entity) {
        if (entity instanceof TameableEntity) {
            TameableEntity tameableEntity = (TameableEntity) entity;
            return tameableEntity.getOwnerUUID() != null;
        }
        return false;
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        card.hitBlock(this,blockRayTraceResult.getBlockPos(), blockRayTraceResult.getDirection());
        if(this.isAlive())
            stayOnBlock(blockRayTraceResult);
    }

    @Override
    public float getBrightness() {
        return Configuration.FLYING_CARD_BRIGHTNESS.get().floatValue();
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
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




    public CommonCard getCardType() {
        return card;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (compoundNBT.contains("power", 9)) {
            ListNBT listnbt = compoundNBT.getList("power", 6);
            if (listnbt.size() == 3) {
                this.xPower = listnbt.getDouble(0);
                this.yPower = listnbt.getDouble(1);
                this.zPower = listnbt.getDouble(2);
            }
        }
        card = CommonCards.fromByte(compoundNBT.getByte("card_type"));
        setRemainingLifetime(compoundNBT.getInt("remaining_life_time"));
        setPickUpStatus(compoundNBT.getBoolean("can_pickup"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.put("power", this.newDoubleList(new double[]{this.xPower, this.yPower, this.zPower}));
        compoundNBT.putByte("card_type", CommonCards.toByte(card));
        compoundNBT.putInt("remaining_life_time", getRemainingLifetime());
        compoundNBT.putBoolean("can_pickup", canPickUp());
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeDouble(xPower);
        buffer.writeDouble(yPower);
        buffer.writeDouble(zPower);
        buffer.writeByte(CommonCards.toByte(card));
        buffer.writeInt(getRemainingLifetime());
        buffer.writeBoolean(canPickUp());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        xPower = additionalData.readDouble();
        yPower = additionalData.readDouble();
        zPower = additionalData.readDouble();
        card = CommonCards.fromByte(additionalData.readByte());
        setRemainingLifetime(additionalData.readInt());
        setPickUpStatus(additionalData.readBoolean());
    }

    // For Delay Handling
    // 5 tick lifespan is counted
    public boolean justBeenThrown(){
        return getRemainingLifetime() > 60 * 20 - 5;
    }

    private boolean canPickUp() {
        return this.entityData.get(CAN_PICK_UP);
    }

    private void setPickUpStatus(boolean b) {
        entityData.set(CAN_PICK_UP, b);
    }

    public int getRemainingLifetime(){
        return this.entityData.get(LIFETIME);
    }

    private void setRemainingLifetime(int lifetime){
        entityData.set(LIFETIME, lifetime);
    }

}
