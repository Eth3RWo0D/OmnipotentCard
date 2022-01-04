package love.marblegate.omnicard.item;

import love.marblegate.omnicard.card.CommonCard;
import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.misc.MiscUtil;
import love.marblegate.omnicard.misc.ModGroup;
import love.marblegate.omnicard.misc.ThemeColor;
import love.marblegate.omnicard.registry.SoundRegistry;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class ElementalCard extends Item {
    public final CommonCard card;

    public ElementalCard(CommonCard card) {
        super(new Properties().tab(ModGroup.GENERAL));
        this.card = card;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!worldIn.isClientSide()) {
            Vec3 vector3d = player.getViewVector(1.0F);

            double x = (vector3d.x * 8D);
            double y = (vector3d.y * 8D);
            double z = (vector3d.z * 8D);

            worldIn.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundRegistry.THROW_ELEMENTAL_CARD.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            FlyingCardEntity flyingCardEntity = new FlyingCardEntity(player, x, y, z, worldIn, card);
            flyingCardEntity.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
            flyingCardEntity.setOwner(player);
            worldIn.addFreshEntity(flyingCardEntity);

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, worldIn.isClientSide());
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getPlayer().isShiftKeyDown()) {
            if (context.getClickedFace() == Direction.UP) {
                if (!context.getLevel().isClientSide()) {
                    CardTrapEntity cardTrapEntity = new CardTrapEntity(context.getLevel(), card);
                    cardTrapEntity.setPos(context.getClickLocation().x, context.getClickLocation().y, context.getClickLocation().z);
                    cardTrapEntity.yRotO = context.getPlayer().yRotO;
                    context.getLevel().addFreshEntity(cardTrapEntity);

                    if (!context.getPlayer().getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return super.onItemUseFirst(stack, context);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltips, TooltipFlag iTooltipFlag) {
        tooltips.add(MiscUtil.tooltip("tooltip.omni_card.elemental_card.function." + card.getCardName(), ThemeColor.HINT));
        tooltips.add(MiscUtil.tooltipBold("tooltip.omni_card.elemental_card.operation_throw", ThemeColor.OPERATION)
                .append(MiscUtil.tooltip("tooltip.omni_card.elemental_card.to_throw", ThemeColor.OPERATION_EXPLAIN)));
        tooltips.add(MiscUtil.tooltipBold("tooltip.omni_card.elemental_card.operation_place", ThemeColor.OPERATION)
                .append(MiscUtil.tooltip("tooltip.omni_card.elemental_card.to_place", ThemeColor.OPERATION_EXPLAIN)));
        super.appendHoverText(itemStack, world, tooltips, iTooltipFlag);
    }
}
