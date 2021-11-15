package love.marblegate.omnicard.entity;

import love.marblegate.omnicard.misc.CardType;
import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EffectRegistry;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
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
    private static final DataParameter<Boolean> CAN_PICK_UP = EntityDataManager.defineId(FlyingCardEntity.class, DataSerializers.BOOLEAN);
    private int remainingLifeTime;

    public FlyingCardEntity(EntityType<? extends FlyingCardEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public FlyingCardEntity(LivingEntity livingEntity, double xPower, double yPower, double zPower, World world, CardType cardType) {
        super(EntityRegistry.FLYING_CARD.get(), livingEntity, xPower, yPower, zPower, world);
        type = cardType;
        remainingLifeTime = 60 * 20;
        setPickUpStatus(false);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        if(!canPickUp()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("cardfly_normal", true));
            return PlayState.CONTINUE;
        }
        else{
            return PlayState.STOP;
        }
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "card_controller", 1, this::predicate));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CAN_PICK_UP,true);
    }

    @Override
    public void tick() {
        super.tick();
        if(!level.isClientSide()){
            if(remainingLifeTime<=0){
                if(type.retrievedItem!=null)
                    this.spawnAtLocation(type.retrievedItem.get().getDefaultInstance(), 0.1F);
                remove();
            }
            else{
                remainingLifeTime-=1;
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
        if(!this.level.isClientSide()){
            Entity entity = entityRayTraceResult.getEntity();

            // Pickup Card on Ground
            if(canPickUp() && entity instanceof PlayerEntity && type.retrievedItem!=null){
                this.spawnAtLocation(type.retrievedItem.get().getDefaultInstance(), 0.1F);
            }

            if(entity instanceof LivingEntity && !canPickUp()) {
                // hit living entity
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

                }

                else if(type==CardType.TORRENT){

                }

                else if(type==CardType.THUNDER){

                }

                else if(type==CardType.BRAMBLE){

                }

                else if(type==CardType.END){

                }

                else if(type==CardType.EARTH){

                }
                remove();
            } else if (!(entity instanceof FlyingCardEntity)){
                // Hit non-living entity
                entity.hurt(ModDamage.causeCardDamage(getOwner(), type), 6);
                remove();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        if(type!=CardType.TORRENT){
            // Card Stay on
            stayOnBlock(blockRayTraceResult);
        } else {
            // Torrent Card eliminates fire
            if(this.level.isClientSide()){

            }
            stayOnBlock(blockRayTraceResult);
        }
    }

    private void stayOnBlock(BlockRayTraceResult blockRayTraceResult){
        Vector3d vector3d = blockRayTraceResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vector3d);
        Vector3d vector3d1 = vector3d.normalize().scale((double)0.05F);
        this.setPosRaw(this.getX() - vector3d1.x, this.getY() - vector3d1.y, this.getZ() - vector3d1.z);
        setPickUpStatus(true);
    }

    private boolean canPickUp(){
        return this.entityData.get(CAN_PICK_UP);
    }

    private void setPickUpStatus(boolean b){
        entityData.set(CAN_PICK_UP,b);
    }




    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected IParticleData getTrailParticle() {
        if(!canPickUp()){
            return ParticleTypes.FIREWORK;
        } else
            return ParticleRegistry.EMPTY.get();

    }

    public CardType getCardType() {
        return type;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        type = CardType.valueOf(compoundNBT.getString("card_type"));
        remainingLifeTime = compoundNBT.getInt("remaining_life_time");
        boolean b = compoundNBT.getBoolean("can_pickup");
        setPickUpStatus(b);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putString("card_type", type.toString());
        compoundNBT.putInt("remaining_life_time",remainingLifeTime);
        compoundNBT.putBoolean("can_pickup",canPickUp());
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeEnum(type);
        buffer.writeInt(remainingLifeTime);
        buffer.writeByte(canPickUp()?1:0);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        type = additionalData.readEnum(CardType.class);
        remainingLifeTime = additionalData.readInt();
        boolean b = additionalData.readByte() == 1;
        setPickUpStatus(b);
    }

}
