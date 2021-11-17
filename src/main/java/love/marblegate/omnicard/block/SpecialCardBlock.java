package love.marblegate.omnicard.block;


import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.misc.CardType;
import love.marblegate.omnicard.misc.SpecialCardType;
import love.marblegate.omnicard.registry.TileEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class SpecialCardBlock extends Block {

    public SpecialCardBlock() {
        super(Properties.of(new Material(MaterialColor.NONE,false,false,false,false,false,false, PushReaction.BLOCK)).strength(0.1F,5F));
    }

    //@Override
    //public BlockRenderType getRenderShape(BlockState state) {
        //return BlockRenderType.ENTITYBLOCK_ANIMATED;
    //}

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
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        SpecialCardType type = ((SpecialCardBlockTileEntity)world.getBlockEntity(pos)).getCardType();
        return type.retrievedItem.get().getDefaultInstance();
    }

}
