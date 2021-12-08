package love.marblegate.omnicard.item;

import com.google.common.collect.Lists;
import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.capability.cardtype.CardTypeData;
import love.marblegate.omnicard.capability.cardtype.CardTypeItemStackProvider;
import love.marblegate.omnicard.capability.cardtype.ICardTypeData;
import love.marblegate.omnicard.card.CommonCard;
import love.marblegate.omnicard.card.CommonCards;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.misc.MiscUtil;
import love.marblegate.omnicard.misc.ModGroup;
import love.marblegate.omnicard.misc.ThemeColor;
import love.marblegate.omnicard.registry.ItemRegistry;
import love.marblegate.omnicard.registry.SoundRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

public class CardStack extends Item {
    public static ResourceLocation CARD_CATEGORY_PROPERTY_NAME = new ResourceLocation(OmniCard.MODID, "card_type");
    private static final List<CommonCard> availableCardType = Lists.newArrayList(CommonCards.INK, CommonCards.RED, CommonCards.CORAL, CommonCards.GOLD, CommonCards.SEA_GREEN, CommonCards.AZURE, CommonCards.CERULEAN_BLUE, CommonCards.HELIOTROPE);

    public CardStack() {
        super(new Properties().tab(ModGroup.GENERAL));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand hand) {
        if (!worldIn.isClientSide()) {
            ICardTypeData cardTypeData = player.getItemInHand(hand).getCapability(CardTypeData.CARD_TYPE_DATA_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));

            if (!player.isShiftKeyDown()) {
                if (cardTypeData.isSwitchingCard()) {
                    // stop card picking
                    cardTypeData.setSwitchingCard(false);
                } else {
                    // Throwing Card
                    if ((player.abilities.instabuild || hasEnoughBlankCard(player)) && cardTypeData.get() != CommonCards.UNKNOWN) {
                        Vector3d vector3d = player.getViewVector(1.0F);

                        double x = (vector3d.x * 8D);
                        double y = (vector3d.y * 8D);
                        double z = (vector3d.z * 8D);

                        FlyingCardEntity flyingCardEntity = new FlyingCardEntity(player, x, y, z, worldIn, cardTypeData.get());
                        flyingCardEntity.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
                        worldIn.addFreshEntity(flyingCardEntity);

                        worldIn.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundRegistry.THROW_COMMON_CARD.get(), SoundCategory.PLAYERS, 1.0F, 1.0F);

                        if (!player.abilities.instabuild) {
                            consumeBlankCard(player);
                            player.causeFoodExhaustion(1);
                        }
                    } else {
                        return ActionResult.fail(player.getItemInHand(hand));
                    }
                }
            } else {
                // enable card picking
                cardTypeData.setSwitchingCard(true);
            }

        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), worldIn.isClientSide());
    }

    private boolean hasEnoughBlankCard(PlayerEntity player) {
        for (ItemStack itemStack : player.inventory.items) {
            if (itemStack.getItem().equals(ItemRegistry.BLANK_CARD.get())) return true;
        }
        return false;
    }

    private void consumeBlankCard(PlayerEntity player) {
        for (ItemStack itemStack : player.inventory.items) {
            if (itemStack.getItem().equals(ItemRegistry.BLANK_CARD.get())) {
                itemStack.shrink(1);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide() && selected) {
            ICardTypeData cardTypeData = itemStack.getCapability(CardTypeData.CARD_TYPE_DATA_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
            if (world.getDayTime() % 10 == 0 && cardTypeData.isSwitchingCard()) {
                cardTypeData.set(switchingToNextCard(cardTypeData.get()));
                world.playSound((PlayerEntity)null, entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.CUTTING_CARD.get(), SoundCategory.PLAYERS, 0.6F, 1F);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag iTooltipFlag) {
        ICardTypeData cardTypeData = itemStack.getCapability(CardTypeData.CARD_TYPE_DATA_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
        CommonCard card = cardTypeData.get();
        boolean f = cardTypeData.isSwitchingCard();
        if (f) {
            tooltips.add(MiscUtil.tooltip("tooltip.omni_card.card_stack.is_switching", ThemeColor.MAIN));
            tooltips.add(MiscUtil.tooltipBold("tooltip.omni_card.card_stack.end_switching_operation", ThemeColor.OPERATION)
                    .append(MiscUtil.tooltip("tooltip.omni_card.card_stack.to_end_switching", ThemeColor.OPERATION_EXPLAIN)));
        } else if (card == CommonCards.UNKNOWN) {
            tooltips.add(MiscUtil.tooltip("tooltip.omni_card.card_stack.not_switched", ThemeColor.MAIN));
            tooltips.add(MiscUtil.tooltip("tooltip.omni_card.card_stack.intro_1", ThemeColor.HINT)
                    .append(MiscUtil.tooltip("item.omni_card.blank_card", ThemeColor.HINT_EMP))
                    .append(MiscUtil.tooltip("tooltip.omni_card.card_stack.intro_2", ThemeColor.HINT))
                    .append(MiscUtil.tooltip("tooltip.omni_card.card_stack.intro_3", ThemeColor.HINT_EMP))
                    .append(MiscUtil.tooltip("tooltip.omni_card.card_stack.intro_4", ThemeColor.HINT))
                    .append(MiscUtil.tooltipBold("tooltip.omni_card.card_stack.intro_5", ThemeColor.OPERATION))
                    .append(MiscUtil.tooltip("tooltip.omni_card.card_stack.intro_6", ThemeColor.HINT)));
            tooltips.add(MiscUtil.tooltipBold("tooltip.omni_card.card_stack.start_switching_operation", ThemeColor.OPERATION)
                    .append(MiscUtil.tooltip("tooltip.omni_card.card_stack.to_start_switching", ThemeColor.OPERATION_EXPLAIN)));
        } else {
            tooltips.add(MiscUtil.tooltip("tooltip.omni_card.card_stack.function." + card.getCardName(), ThemeColor.HINT));
            tooltips.add(MiscUtil.tooltipBold("tooltip.omni_card.card_stack.start_switching_operation", ThemeColor.OPERATION)
                    .append(MiscUtil.tooltip("tooltip.omni_card.card_stack.to_start_switching", ThemeColor.OPERATION_EXPLAIN)));
        }
        super.appendHoverText(itemStack, world, tooltips, iTooltipFlag);
    }

    private CommonCard switchingToNextCard(CommonCard presentCard) {
        if (presentCard == CommonCards.UNKNOWN) {
            return availableCardType.get(0);
        }
        int position = availableCardType.indexOf(presentCard);
        if (position != availableCardType.size() - 1) {
            return availableCardType.get(position + 1);
        } else return availableCardType.get(0);
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT compoundNBT = super.getShareTag(stack);
        if (compoundNBT == null) {
            compoundNBT = new CompoundNBT();
        }
        ICardTypeData cardTypeData = stack.getCapability(CardTypeData.CARD_TYPE_DATA_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
        compoundNBT.putByte("card_type", CommonCards.toByte(cardTypeData.get()));
        return compoundNBT;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null) {
            ICardTypeData cardTypeData = stack.getCapability(CardTypeData.CARD_TYPE_DATA_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
            cardTypeData.set(CommonCards.fromByte(nbt.getByte("card_type")));
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CardTypeItemStackProvider();
    }


}
