package love.marblegate.omnicard.item;

import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.misc.CardType;
import love.marblegate.omnicard.misc.ModGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ElementalCard extends Item {
    public final CardType cardType;

    public ElementalCard(CardType cardType) {
        super(new Properties().tab(ModGroup.GENERAL));
        this.cardType = cardType;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!worldIn.isClientSide()) {
            Vector3d vector3d = player.getViewVector(1.0F);

            double x = (vector3d.x * 4D);
            double y = (vector3d.y * 4D);
            double z = (vector3d.z * 4D);

            FlyingCardEntity flyingCardEntity = new FlyingCardEntity(player, x, y, z, worldIn, cardType);
            flyingCardEntity.setPos(player.getX(), player.getY() + player.getEyeHeight(player.getPose()), player.getZ());
            worldIn.addFreshEntity(flyingCardEntity);

            if(!player.abilities.instabuild){
                itemStack.shrink(1);
            }
        }
        return ActionResult.sidedSuccess(itemStack,worldIn.isClientSide());
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        if(context.getPlayer().isShiftKeyDown()){
            if(context.getClickedFace() == Direction.UP){
                if(!context.getLevel().isClientSide()){
                    CardTrapEntity cardTrapEntity = new CardTrapEntity(context.getLevel(),cardType);
                    cardTrapEntity.setPos(context.getClickLocation().x,context.getClickLocation().y,context.getClickLocation().z);
                    cardTrapEntity.yRot = context.getPlayer().yRot;
                    context.getLevel().addFreshEntity(cardTrapEntity);
                    if(!context.getPlayer().abilities.instabuild){
                        stack.shrink(1);
                    }
                }
                return ActionResultType.SUCCESS;
            }
            else {
                return ActionResultType.FAIL;
            }
        } else {
            return super.onItemUseFirst(stack, context);
        }
    }
}
