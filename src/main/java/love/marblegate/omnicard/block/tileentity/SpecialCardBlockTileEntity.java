package love.marblegate.omnicard.block.tileentity;

import love.marblegate.omnicard.misc.SpecialCardType;
import love.marblegate.omnicard.registry.TileEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class SpecialCardBlockTileEntity extends TileEntity implements ITickableTileEntity,IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private SpecialCardType type;
    private int lifetime;
    private boolean preparedVanish;

    public SpecialCardBlockTileEntity() {
        super(TileEntityRegistry.SPECIAL_CARD_BLOCK_TILEENTITY.get());
        preparedVanish = false;
    }

    public SpecialCardBlockTileEntity(SpecialCardType type) {
        super(TileEntityRegistry.SPECIAL_CARD_BLOCK_TILEENTITY.get());
        preparedVanish = false;
        this.type = type;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (preparedVanish) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("card_on_disappear", false));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("card_floating", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "card_block_controller", 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {
        // All logic should be handled in serverside since we don't sync lifetime
        if(!level.isClientSide()){
            if(lifetime==0)
                level.setBlockAndUpdate(this.getBlockPos(),Blocks.AIR.defaultBlockState());
            else{
                // Transfer to Disappear Animation
                if(lifetime == 30){
                    preparedVanish = true;
                    SyncToClient();
                }
                if(lifetime>0) lifetime--;
                // Handle Special Card Logic
                type.serverTickLogic.handle(this);
            }
        }

    }

    private void SyncToClient(){
        level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
    }

    public SpecialCardType getCardType() {
        return type;
    }

    @Override
    public void deserializeNBT(BlockState state, CompoundNBT nbt) {
        super.deserializeNBT(state, nbt);
        type = SpecialCardType.valueOf(nbt.getString("card_type"));
        lifetime = nbt.getInt("lifetime");
        preparedVanish = nbt.getBoolean("should_disappear");

    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = super.serializeNBT();
        if(compoundNBT==null) compoundNBT = new CompoundNBT();
        compoundNBT.putString("card_type",type.name());
        compoundNBT.putInt("lifetime",lifetime);
        compoundNBT.putBoolean("should_disappear",preparedVanish);
        return compoundNBT;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(level.getBlockState(pkt.getPos()), pkt.getTag());

    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compoundNBT = super.getUpdateTag();
        compoundNBT.putString("card_type",type.name());
        compoundNBT.putBoolean("should_disappear",preparedVanish);
        return compoundNBT;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        type = SpecialCardType.valueOf(tag.getString("card_type"));
        preparedVanish = tag.getBoolean("should_disappear");
    }

}
