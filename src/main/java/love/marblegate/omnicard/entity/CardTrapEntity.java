package love.marblegate.omnicard.entity;

import com.google.common.collect.Lists;
import love.marblegate.omnicard.misc.CardType;
import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EntityRegistry;
import love.marblegate.omnicard.registry.ItemRegistry;
import love.marblegate.omnicard.renderer.CardTrapEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
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

import java.util.Collections;


public class CardTrapEntity extends Entity implements IAnimatable, IEntityAdditionalSpawnData {
    private final AnimationFactory factory = new AnimationFactory(this);
    private CardType type;

    public CardTrapEntity(EntityType<? extends CardTrapEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public CardTrapEntity(World p_i50173_2_, CardType cardType) {
        super(EntityRegistry.CARD_TRAP.get(), p_i50173_2_);
        type = cardType;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        // it seems like no need for animation
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "card_controller", 1, this::predicate));
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        if(type.retrievedItem!=null){
            if(!player.level.isClientSide()){
                player.level.addFreshEntity(new ItemEntity(player.level,this.getX(),this.getY(),this.getZ(),type.retrievedItem.get().getDefaultInstance()));
                remove();
            }
            return ActionResultType.sidedSuccess(player.level.isClientSide());
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return !(entity instanceof CardTrapEntity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void tick(){
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));

        if (this.level.isClientSide) {
            this.noPhysics = false;
        } else {
            this.noPhysics = !this.level.noCollision(this);
            if (this.noPhysics) {
                this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
            }
        }

        if (!this.onGround || getHorizontalDistanceSqr(this.getDeltaMovement()) > (double)1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
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

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void defineSynchedData() {}

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
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        compoundNBT.putString("card_type", type.toString());
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
