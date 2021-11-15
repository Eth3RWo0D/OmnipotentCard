package love.marblegate.omnicard.entity;

import love.marblegate.omnicard.misc.CardType;
import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EffectRegistry;
import love.marblegate.omnicard.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
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
    private CardType type;


    public FlyingCardEntity(EntityType<? extends FlyingCardEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public FlyingCardEntity(LivingEntity livingEntity, double xPower, double yPower, double zPower, World world, CardType cardType) {
        super(EntityRegistry.FLYING_CARD.get(), livingEntity, xPower, yPower, zPower, world);
        type = cardType;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("cardfly_normal", true));
        return PlayState.CONTINUE;
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "card_controller", 1, this::predicate));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult) {
        // TODO temporary
        super.onHit(rayTraceResult);
        if (!this.level.isClientSide) {
            this.remove();
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        super.onHitEntity(entityRayTraceResult);
        if(!this.level.isClientSide()){
            Entity entity = entityRayTraceResult.getEntity();
            if(entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                if(type==CardType.BLANK){
                    livingEntity.hurt(ModDamage.causeCardDamage(getOwner(), type), 6);
                }

                else if(type==CardType.INK){
                    livingEntity.hurt(ModDamage.causeCardDamage(getOwner(), type), 6);
                    livingEntity.addEffect(new EffectInstance(Effects.WITHER, 80));
                }

                else if(type==CardType.RED){
                    if(livingEntity.hasEffect(EffectRegistry.READY_TO_EXPLODE.get())){
                        Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(livingEntity.level, livingEntity) ? Explosion.Mode.BREAK : Explosion.Mode.NONE;
                        livingEntity.level.explode(livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 3, explosion$mode);
                        livingEntity.hurt(ModDamage.causeCardDamage(getOwner(), CardType.RED).setExplosion(), 6);
                    }
                    livingEntity.addEffect(new EffectInstance(EffectRegistry.READY_TO_EXPLODE.get(), 30));
                }

                else if(type==CardType.CORAL){
                    livingEntity.hurt(ModDamage.causeCardDamage(getOwner(), type), 6);
                    if(livingEntity instanceof PlayerEntity){
                        livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 80,3));
                        livingEntity.addEffect(new EffectInstance(Effects.CONFUSION, 80));
                    } else {
                        livingEntity.addEffect(new EffectInstance(EffectRegistry.DIZZY.get(), 80));
                        livingEntity.addEffect(new EffectInstance(Effects.CONFUSION, 80));
                    }
                }

                else if(type==CardType.GOLD){
                    livingEntity.hurt(ModDamage.causeCardDamage(getOwner(), type), 6);
                    livingEntity.addEffect(new EffectInstance(Effects.LEVITATION, 80));
                    livingEntity.addEffect(new EffectInstance(Effects.GLOWING, 80));
                }

                else if(type==CardType.SEA_GREEN){
                    if(livingEntity.isInvertedHealAndHarm()){
                        livingEntity.hurt(ModDamage.causeCardDamage(getOwner(), type).setMagic(), 8);
                    } else {
                        livingEntity.heal(8);
                    }
                }

                else if(type==CardType.AZURE){

                }

                else if(type==CardType.CERULEAN_BLUE){
                    livingEntity.hurt(ModDamage.causeCardDamage(getOwner(), type), 6);
                    livingEntity.addEffect(new EffectInstance(Effects.BLINDNESS, 100));
                }

                else if(type==CardType.HELIOTROPE){
                    livingEntity.hurt(ModDamage.causeCardDamage(getOwner(), type), 6);
                    livingEntity.addEffect(new EffectInstance(Effects.POISON, 80));
                    livingEntity.addEffect(new EffectInstance(EffectRegistry.POISON_NOW_LETHAL.get(), 81));
                }

                else if(type==CardType.FLAME){

                }else if(type==CardType.TORRENT){

                }else if(type==CardType.THUNDER){

                }else if(type==CardType.BRAMBLE){

                }else if(type==CardType.END){

                }else if(type==CardType.EARTH){

                }
            } else {
                if(type.recyclable && type!=CardType.TORRENT){
                    this.level.addFreshEntity(new ItemEntity(this.level, entityRayTraceResult.getLocation().x, entityRayTraceResult.getLocation().y, entityRayTraceResult.getLocation().z, type.retrievedItem.get().getDefaultInstance()));
                } else if(type==CardType.TORRENT){

                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        if(!this.level.isClientSide()){
            if(type.recyclable && type!=CardType.TORRENT){
                this.level.addFreshEntity(new ItemEntity(this.level, blockRayTraceResult.getLocation().x, blockRayTraceResult.getLocation().y, blockRayTraceResult.getLocation().z, type.retrievedItem.get().getDefaultInstance()));
            } else if(type==CardType.TORRENT){

            }
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected IParticleData getTrailParticle() {
        return ParticleTypes.FIREWORK;
    }

    public CardType getCardType() {
        return type;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        type = CardType.valueOf(compoundNBT.getString("card_type"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
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
