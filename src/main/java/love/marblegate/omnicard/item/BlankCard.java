package love.marblegate.omnicard.item;

import love.marblegate.omnicard.card.CommonCards;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.misc.ModGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class BlankCard extends Item {

    public BlankCard() {
        super(new Properties().tab(ModGroup.GENERAL));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!worldIn.isClientSide()) {
            Vector3d vector3d = player.getViewVector(1.0F);

            double x = (vector3d.x * 4D);
            double y = (vector3d.y * 4D);
            double z = (vector3d.z * 4D);

            FlyingCardEntity flyingCardEntity = new FlyingCardEntity(player, x, y, z, worldIn, CommonCards.BLANK);
            flyingCardEntity.setPos(player.getX(), player.getY() + player.getEyeHeight(player.getPose()), player.getZ());
            worldIn.addFreshEntity(flyingCardEntity);


            worldIn.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);

            if (!player.abilities.instabuild) {
                itemStack.shrink(1);
            }
        }
        return ActionResult.sidedSuccess(itemStack, worldIn.isClientSide());
    }
}
