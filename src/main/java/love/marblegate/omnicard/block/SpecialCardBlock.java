package love.marblegate.omnicard.block;

import com.google.common.collect.Lists;
import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.card.BlockCard;
import love.marblegate.omnicard.registry.TileEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpecialCardBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(5.5D, 2.5D, 5.5D, 10.5D, 12.5D, 10.5D);

    public SpecialCardBlock() {
        super(Properties.of(new Material(MaterialColor.NONE, false, false, false, false, false, false, PushReaction.BLOCK)).noCollission().strength(0.1F, 5F));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        Entity entity = builder.getOptionalParameter(LootParameters.THIS_ENTITY);
        if (entity instanceof PlayerEntity) {
            TileEntity tileentity = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
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
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityRegistry.SPECIAL_CARD_BLOCK_TILEENTITY.get().create();
    }

    @Override
    public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return super.canHarvestBlock(state, world, pos, player);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        BlockCard type = ((SpecialCardBlockTileEntity) world.getBlockEntity(pos)).getCardType();
        if (type.getRetrievedItem().isPresent())
            return type.getRetrievedItem().get().getDefaultInstance();
        else
            return ItemStack.EMPTY;
    }
}
