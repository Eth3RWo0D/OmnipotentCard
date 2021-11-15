package love.marblegate.omnicard.entity;

import love.marblegate.omnicard.misc.CardType;
import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CardTrapEntity extends Entity implements IAnimatable, IEntityAdditionalSpawnData {
    private final AnimationFactory factory = new AnimationFactory(this);
    private CardType type;
    private UUID ownerUUID;

    public CardTrapEntity(EntityType<? extends CardTrapEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public CardTrapEntity(World p_i50173_2_, CardType cardType) {
        super(EntityRegistry.CARD_TRAP.get(), p_i50173_2_);
        type = cardType;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        // it seems like no need for animation
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "card_controller", 1, this::predicate));
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        if (type.entityDroppedItem != null) {
            if (!player.level.isClientSide()) {
                player.level.addFreshEntity(new ItemEntity(player.level, this.getX(), this.getY(), this.getZ(), type.entityDroppedItem.get().getDefaultInstance()));
                remove();
            }
            return ActionResultType.sidedSuccess(player.level.isClientSide());
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        // Handle activated by entity
        if(type.trapCardActivationHandler!=null){
            List<LivingEntity> targets = getTriggeringTarget();
            if (!targets.isEmpty()) {
                activateTrap(targets);
            }
        }

        if (isAlive()) {
            // Natural falling and movement`
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));

            if (this.level.isClientSide) {
                this.noPhysics = false;
            } else {
                this.noPhysics = !this.level.noCollision(this);
                if (this.noPhysics) {
                    this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
                }
            }

            if (!this.onGround || getHorizontalDistanceSqr(this.getDeltaMovement()) > (double) 1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
                this.move(MoverType.SELF, this.getDeltaMovement());
                float f1 = 0.98F;
                if (this.onGround) {
                    f1 = this.level.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ())).getSlipperiness(level, new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ()), this) * 0.98F;
                }

                this.setDeltaMovement(this.getDeltaMovement().multiply(f1, 0.98D, f1));
                if (this.onGround) {
                    Vector3d vector3d1 = this.getDeltaMovement();
                    if (vector3d1.y < 0.0D) {
                        this.setDeltaMovement(vector3d1.multiply(1.0D, -0.5D, 1.0D));
                    }
                }
            }
        }
    }

    private List<LivingEntity> getTriggeringTarget() {
        return level.getEntities(this,getBoundingBox().expandTowards(0,1,0),entity -> entity instanceof LivingEntity)
                .stream().map(entity -> (LivingEntity) entity).collect(Collectors.toList());
    }

    private void activateTrap(List<LivingEntity> targets){
        for(LivingEntity livingEntity:targets)
            type.trapCardActivationHandler.handleTrap(this,livingEntity);
        remove();
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUUID = entity.getUUID();
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.ownerUUID != null && this.level instanceof ServerWorld) {
            return ((ServerWorld)this.level).getEntity(this.ownerUUID);
        }
        return null;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    public CardType getCardType() {
        return type;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        type = CardType.valueOf(compoundNBT.getString("card_type"));
        if(compoundNBT.contains("owner_uuid"))
            ownerUUID = compoundNBT.getUUID("owner_uuid");
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        compoundNBT.putString("card_type", type.toString());
        if(ownerUUID!=null){
            compoundNBT.putUUID("owner_uuid",ownerUUID);
        }
    }


    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeEnum(type);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        type = additionalData.readEnum(CardType.class);
    }

}
