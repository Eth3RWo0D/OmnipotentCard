package love.marblegate.omnicard.entity;

import love.marblegate.omnicard.misc.ModDamage;
import love.marblegate.omnicard.registry.EntityRegistry;
import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;


public class CardEntity extends DamagingProjectileEntity implements IAnimatable, ISyncable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private CardType type;


    public CardEntity(EntityType<? extends CardEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
        type = CardType.BLANK;
    }

    public CardEntity(LivingEntity livingEntity, double xPower, double yPower, double zPower, World world, CardType cardType) {
        super(EntityRegistry.CARD.get(), livingEntity, xPower, yPower, zPower, world);
        type = cardType;
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "card_controller", 1, (animationEvent) -> PlayState.CONTINUE));
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
    public void onAnimationSync(int id, int state) {

    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void onHit(RayTraceResult rayTraceResult) {
        // TODO temporary
        super.onHit(rayTraceResult);
        if (!this.level.isClientSide) {
            this.level.addFreshEntity(new ItemEntity(this.level, rayTraceResult.getLocation().x, rayTraceResult.getLocation().y, rayTraceResult.getLocation().z, ItemRegistry.BLANK_CARD.get().getDefaultInstance()));
            this.remove();
        }

    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        super.onHitEntity(entityRayTraceResult);
        entityRayTraceResult.getEntity().hurt(ModDamage.causeCardDamage(getOwner(), CardType.BLANK), 5); //TODO temporary
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected IParticleData getTrailParticle() {
        return ParticleTypes.FIREWORK;
    }

    public String getCardType() {
        return type.name;
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

    public enum CardType {
        BLANK("whitecard"),
        RED("redcard"),
        CORAL("orangecard"),
        GOLD("goldcard"),
        SEA_GREEN("greencard"),
        AZURE("skycard"),
        CERULEAN_BLUE("bluecard"),
        HELIOTROPE("violetcard"),
        INK("blackcard");

        public String name;

        CardType(String i) {
            name = i;
        }
    }
}
