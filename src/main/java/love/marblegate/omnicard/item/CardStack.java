package love.marblegate.omnicard.item;

import com.google.common.collect.Lists;
import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.capability.cardtype.CardTypeData;
import love.marblegate.omnicard.capability.cardtype.CardTypeItemStackProvider;
import love.marblegate.omnicard.card.CommonCard;
import love.marblegate.omnicard.card.CommonCards;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.misc.Configuration;
import love.marblegate.omnicard.misc.MiscUtil;
import love.marblegate.omnicard.misc.ModGroup;
import love.marblegate.omnicard.misc.ThemeColor;
import love.marblegate.omnicard.registry.ItemRegistry;
import love.marblegate.omnicard.registry.SoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
        if (!worldIn.isClientSide()) {
            CardTypeData cardTypeData = player.getItemInHand(hand).getCapability(CardTypeData.CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
            if (!player.isShiftKeyDown()) {
                if (cardTypeData.isSwitchingCard()) {
                    // stop card picking
                    cardTypeData.setSwitchingCard(false);
                } else {
                    // Throwing Card
                    if ((player.getAbilities().instabuild || hasEnoughBlankCard(player)) && cardTypeData.get() != CommonCards.UNKNOWN) {
                        Vec3 vector3d = player.getViewVector(1.0F);

                        double x = (vector3d.x * Configuration.FLYING_CARD_SPEED.get());
                        double y = (vector3d.y * Configuration.FLYING_CARD_SPEED.get());
                        double z = (vector3d.z * Configuration.FLYING_CARD_SPEED.get());

                        FlyingCardEntity flyingCardEntity = new FlyingCardEntity(player, x, y, z, worldIn, cardTypeData.get());
                        flyingCardEntity.setOwner(player);
                        flyingCardEntity.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
                        worldIn.addFreshEntity(flyingCardEntity);

                        double d0 = -Mth.sin(player.getYRot() * ((float)Math.PI / 180F));
                        double d1 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
                        ((ServerLevel)worldIn).sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);

                        worldIn.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundRegistry.THROW_CARD.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                        if (!player.getAbilities().instabuild) {
                            consumeBlankCard(player);
                            player.causeFoodExhaustion(1);
                        }
                    } else {
                        return InteractionResultHolder.fail(player.getItemInHand(hand));
                    }
                }
            } else {
                // enable card picking
                cardTypeData.setSwitchingCard(true);
            }

        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), worldIn.isClientSide());
    }

    private boolean hasEnoughBlankCard(Player player) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem().equals(ItemRegistry.BLANK_CARD.get())) return true;
        }
        return false;
    }

    private void consumeBlankCard(Player player) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem().equals(ItemRegistry.BLANK_CARD.get())) {
                itemStack.shrink(1);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide() && selected) {
            if (world.getDayTime() % 20 == 0) {
                CardTypeData cardTypeData = itemStack.getCapability(CardTypeData.CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
                if (cardTypeData.isSwitchingCard()) {
                    cardTypeData.set(switchingToNextCard(cardTypeData.get()));
                    world.playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.CUTTING_CARD.get(), SoundSource.PLAYERS, 0.6F, 1F);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltips, TooltipFlag iTooltipFlag) {
        CardTypeData cardTypeData = itemStack.getCapability(CardTypeData.CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
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
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag compoundNBT = super.getShareTag(stack);
        if (compoundNBT == null) {
            compoundNBT = new CompoundTag();
        }
        CardTypeData cardTypeData = stack.getCapability(CardTypeData.CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
        compoundNBT.putByte("card_type", CommonCards.toByte(cardTypeData.get()));
        return compoundNBT;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null) {
            CardTypeData cardTypeData = stack.getCapability(CardTypeData.CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
            cardTypeData.set(CommonCards.fromByte(nbt.getByte("card_type")));
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new CardTypeItemStackProvider();
    }


}
