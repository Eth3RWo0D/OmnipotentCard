package love.marblegate.omnicard.item;

import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.card.BlockCard;
import love.marblegate.omnicard.card.BlockCards;
import love.marblegate.omnicard.misc.ModGroup;
import love.marblegate.omnicard.registry.BlockRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.Property;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PlaceableSpecialCard extends Item {
    public final BlockCard card;

    public PlaceableSpecialCard(BlockCard card) {
        super(new Properties().tab(ModGroup.GENERAL));
        this.card = card;
    }

    public ActionResultType useOn(ItemUseContext context) {
        return this.place(new BlockItemUseContext(context));
    }

    public ActionResultType place(BlockItemUseContext context) {
        if (!context.canPlace()) {
            return ActionResultType.FAIL;
        } else {
            BlockState blockstate = BlockRegistry.SPECIAL_CARD_BLOCK.get().defaultBlockState();

            if (!this.placeBlock(context, blockstate)) {
                return ActionResultType.FAIL;
            } else {
                BlockPos blockpos = context.getClickedPos();
                World world = context.getLevel();
                PlayerEntity playerentity = context.getPlayer();
                ItemStack itemstack = context.getItemInHand();
                BlockState blockstate1 = world.getBlockState(blockpos);
                Block block = blockstate1.getBlock();
                if (block == blockstate.getBlock()) {
                    // blockstate1 = this.updateBlockStateFromTag(blockpos, world, itemstack, blockstate1);
                    updateCustomBlockEntityTag(world, playerentity, blockpos, itemstack);
                    block.setPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
                    if (playerentity instanceof ServerPlayerEntity) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerentity, blockpos, itemstack);
                    }
                }

                SoundType soundtype = blockstate1.getSoundType(world, blockpos, context.getPlayer());
                world.playSound(playerentity, blockpos, this.getPlaceSound(blockstate1, world, blockpos, context.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                if (card == BlockCards.PURIFICATION) {
                    context.getPlayer().addEffect(new EffectInstance(Effects.WEAKNESS, 1200));
                }

                if (playerentity == null || !playerentity.abilities.instabuild) {
                    itemstack.shrink(1);
                }

                return ActionResultType.sidedSuccess(world.isClientSide);
            }
        }
    }

    protected boolean placeBlock(BlockItemUseContext context, BlockState blockState) {
        return context.getLevel().setBlockAndUpdate(context.getClickedPos(), blockState);
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState blockState, Property<T> property, String nbtString) {
        return property.getValue(nbtString).map((string) -> blockState.setValue(property, string)).orElse(blockState);
    }

    protected boolean updateCustomBlockEntityTag(World level, @Nullable PlayerEntity player, BlockPos blockPos, ItemStack itemStack) {
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

            // Vanilla BlockItem use #save & #load method and also NBT tag from ItemStack.
            // But we do not use following code since #save requires custom data to be setup.
            // CompoundNBT compoundnbt = tileentity.save(new CompoundNBT());
            // tileentity.load(level.getBlockState(blockPos),compoundnbt);

            tileentity.setChanged();
            return true;
        }
        return false;
    }

    private SoundEvent getPlaceSound(BlockState state, World world, BlockPos pos, PlayerEntity entity) {
        return state.getSoundType(world, pos, entity).getPlaceSound();
    }
}
