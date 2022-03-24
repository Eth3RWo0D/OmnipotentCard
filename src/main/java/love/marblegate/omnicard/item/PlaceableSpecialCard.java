package love.marblegate.omnicard.item;

import love.marblegate.omnicard.block.blockentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.card.BlockCard;
import love.marblegate.omnicard.card.BlockCards;
import love.marblegate.omnicard.misc.MiscUtil;
import love.marblegate.omnicard.misc.ModGroup;
import love.marblegate.omnicard.misc.ThemeColor;
import love.marblegate.omnicard.registry.BlockRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class PlaceableSpecialCard extends Item {
    public final BlockCard card;

    public PlaceableSpecialCard(BlockCard card) {
        super(new Properties().tab(ModGroup.GENERAL));
        this.card = card;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return this.place(new BlockPlaceContext(context));
    }

    public InteractionResult place(BlockPlaceContext context) {
        if (!context.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockState blockstate = BlockRegistry.SPECIAL_CARD_BLOCK.get().defaultBlockState();

            if (!this.placeBlock(context, blockstate)) {
                return InteractionResult.FAIL;
            } else {
                BlockPos blockpos = context.getClickedPos();
                Level world = context.getLevel();
                Player playerentity = context.getPlayer();
                ItemStack itemstack = context.getItemInHand();
                BlockState blockstate1 = world.getBlockState(blockpos);
                Block block = blockstate1.getBlock();
                if (block == blockstate.getBlock()) {
                    // blockstate1 = this.updateBlockStateFromTag(blockpos, world, itemstack, blockstate1);
                    updateCustomBlockEntityTag(world, playerentity, blockpos, itemstack);
                    block.setPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
                    if (playerentity instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) playerentity, blockpos, itemstack);
                    }
                }

                SoundType soundtype = blockstate1.getSoundType(world, blockpos, context.getPlayer());
                world.playSound(playerentity, blockpos, this.getPlaceSound(blockstate1, world, blockpos, context.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                if (card == BlockCards.PURIFICATION) {
                    context.getPlayer().addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200));
                }

                if (playerentity == null || !playerentity.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
    }

    protected boolean placeBlock(BlockPlaceContext context, BlockState blockState) {
        return context.getLevel().setBlockAndUpdate(context.getClickedPos(), blockState);
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState blockState, Property<T> property, String nbtString) {
        return property.getValue(nbtString).map((string) -> blockState.setValue(property, string)).orElse(blockState);
    }

    protected boolean updateCustomBlockEntityTag(Level level, @Nullable Player player, BlockPos blockPos, ItemStack itemStack) {
        MinecraftServer minecraftserver = level.getServer();
        if (minecraftserver == null) {
            return false;
        }
        SpecialCardBlockTileEntity tileentity = (SpecialCardBlockTileEntity) level.getBlockEntity(blockPos);
        if (tileentity != null) {
            if (!level.isClientSide && tileentity.onlyOpCanSetNbt() && (player == null || !player.canUseGameMasterBlocks())) {
                return false;
            }

            tileentity.initializingData(card);
            tileentity.setChanged();
            return true;
        }
        return false;
    }

    private SoundEvent getPlaceSound(BlockState state, Level world, BlockPos pos, Player entity) {
        return state.getSoundType(world, pos, entity).getPlaceSound();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level world, List<Component> tooltips, TooltipFlag iTooltipFlag) {
        tooltips.add(MiscUtil.tooltip("tooltip.omni_card.special_card.function." + card.getCardName(), ThemeColor.HINT));
        tooltips.add(MiscUtil.tooltip("tooltip.omni_card.special_card.is_1", ThemeColor.HINT)
                .append(MiscUtil.tooltip("tooltip.omni_card.special_card.is_" + (card.canRetrieve() ? "2" : "3"), ThemeColor.HINT_EMP))
                .append(MiscUtil.tooltip("tooltip.omni_card.special_card.is_4", ThemeColor.HINT)));
        String lifetime = card.getLifetime()==-1?" ∞ ":" " + card.getLifetime() / 20 + " ";
        tooltips.add(MiscUtil.tooltip("tooltip.omni_card.special_card.is_5", ThemeColor.HINT)
                .append(new TextComponent(lifetime).setStyle(Style.EMPTY.withColor(ThemeColor.HINT_EMP).withBold(false))));
        tooltips.add(MiscUtil.tooltipBold("tooltip.omni_card.special_card.operation_place", ThemeColor.OPERATION)
                .append(MiscUtil.tooltip("tooltip.omni_card.special_card.to_place", ThemeColor.OPERATION_EXPLAIN)));
        super.appendHoverText(itemStack, world, tooltips, iTooltipFlag);
    }
}
