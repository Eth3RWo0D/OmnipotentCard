package love.marblegate.omnicard.item;

import love.marblegate.omnicard.card.CommonCards;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.misc.Configuration;
import love.marblegate.omnicard.misc.ModGroup;
import love.marblegate.omnicard.registry.SoundRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BlankCard extends Item {

    public BlankCard() {
        super(new Properties().tab(ModGroup.GENERAL));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!worldIn.isClientSide()) {
            Vector3d vector3d = player.getViewVector(1.0F);

            double x = (vector3d.x * Configuration.FLYING_CARD_SPEED.get());
            double y = (vector3d.y * Configuration.FLYING_CARD_SPEED.get());
            double z = (vector3d.z * Configuration.FLYING_CARD_SPEED.get());

            FlyingCardEntity flyingCardEntity = new FlyingCardEntity(player, x, y, z, worldIn, CommonCards.BLANK);
            flyingCardEntity.setOwner(player);
            flyingCardEntity.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
            worldIn.addFreshEntity(flyingCardEntity);

            double d0 = -MathHelper.sin(player.yRot * ((float)Math.PI / 180F));
            double d1 = MathHelper.cos(player.yRot * ((float)Math.PI / 180F));
            ((ServerWorld)worldIn).sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);

            worldIn.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundRegistry.THROW_CARD.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);

            if (!player.abilities.instabuild) {
                itemStack.shrink(1);
            }
        }
        return ActionResult.sidedSuccess(itemStack, worldIn.isClientSide());
    }
}
