package love.marblegate.omnicard.block;

import com.google.common.collect.Lists;
import love.marblegate.omnicard.block.blockentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.card.BlockCard;
import love.marblegate.omnicard.misc.Configuration;
import love.marblegate.omnicard.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpecialCardBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Block.box(5.5D, 2.5D, 5.5D, 10.5D, 12.5D, 10.5D);

    public SpecialCardBlock() {
        super(Properties.of(new Material(MaterialColor.NONE, false, false, false, false, false, false, PushReaction.BLOCK)).noCollission().strength(0.1F, 5F));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        Entity entity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof Player) {
            BlockEntity tileentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (tileentity instanceof SpecialCardBlockTileEntity) {
                SpecialCardBlockTileEntity specialCardBlockTileEntity = (SpecialCardBlockTileEntity) tileentity;
                if (specialCardBlockTileEntity.getCardType().canRetrieve() && specialCardBlockTileEntity.getCardType().getRetrievedItem().isPresent()) {
                    return Lists.newArrayList(specialCardBlockTileEntity.getCardType().getRetrievedItem().get().getDefaultInstance());
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BlockEntityRegistry.SPECIAL_CARD_BLOCK_TILEENTITY.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return (theLevel, pos, state, tile) -> {
                if (tile instanceof SpecialCardBlockTileEntity tileEntity) {
                    tileEntity.tickClient();
                }
            };
        } else {
            return (theLevel, pos, state, tile) -> {
                if (tile instanceof SpecialCardBlockTileEntity tileEntity) {
                    tileEntity.tickServer();
                }
            };
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return Configuration.FIELD_CARD_BRIGHTNESS.get();
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        return super.canHarvestBlock(state, world, pos, player);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        BlockCard type = ((SpecialCardBlockTileEntity) world.getBlockEntity(pos)).getCardType();
        if (type != null) {
            if (type.getRetrievedItem().isPresent())
                return type.getRetrievedItem().get().getDefaultInstance();
        }
        return ItemStack.EMPTY;
    }
}
