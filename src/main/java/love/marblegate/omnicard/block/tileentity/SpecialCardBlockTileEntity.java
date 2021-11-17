package love.marblegate.omnicard.block.tileentity;

import love.marblegate.omnicard.misc.CardType;
import love.marblegate.omnicard.misc.SpecialCardType;
import love.marblegate.omnicard.registry.TileEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class SpecialCardBlockTileEntity extends TileEntity implements ITickableTileEntity,IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private SpecialCardType type;
    private int lifetime;

    public SpecialCardBlockTileEntity() {
        super(TileEntityRegistry.SPECIAL_CARD_BLOCK_TILEENTITY.get());
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        // it seems like no need for animation
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "card_block_controller", 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory; //TODO
    }

    @Override
    public void tick() {
        // All logic should be handled in serverside since we don't sync lifetime
        if(!level.isClientSide()){
            if(lifetime==0)
                level.setBlockAndUpdate(this.getBlockPos(),Blocks.AIR.defaultBlockState());
            else{
                if(lifetime>0) lifetime--;
                // Handle Special Card Logic
                type.serverTickLogic.handle(this);
            }
        }

    }

    public SpecialCardType getCardType() {
        return type;
    }

    public int getLifetime() {
        return lifetime;
    }

    @Override
    public void deserializeNBT(BlockState state, CompoundNBT nbt) {
        super.deserializeNBT(state, nbt);
        type = SpecialCardType.valueOf(nbt.getString("card_type"));
        lifetime = nbt.getInt("lifetime");

    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = super.serializeNBT();
        if(compoundNBT==null) compoundNBT = new CompoundNBT();
        compoundNBT.putString("card_type",type.name());
        compoundNBT.putInt("lifetime",lifetime);
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
        return compoundNBT;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        type = SpecialCardType.valueOf(tag.getString("card_type"));
    }

}
