package love.marblegate.omnicard.block.tileentity;

import love.marblegate.omnicard.card.BlockCard;
import love.marblegate.omnicard.card.BlockCards;
import love.marblegate.omnicard.registry.TileEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class SpecialCardBlockTileEntity extends TileEntity implements ITickableTileEntity, IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private BlockCard card;

    public int getLifetime() {
        return lifetime;
    }

    private int lifetime;
    private boolean preparedVanish;

    public SpecialCardBlockTileEntity() {
        super(TileEntityRegistry.SPECIAL_CARD_BLOCK_TILEENTITY.get());
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
        if (!level.isClientSide()) {
            if (lifetime == 0)
                level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
            else {
                // Transfer to Disappear Animation
                if (lifetime == 30) {
                    preparedVanish = true;
                    SyncToClient();
                }
                if (lifetime > 0){
                    lifetime--;
                    if(lifetime%20==0)
                        SyncToClient();
                }
                // Handle Special Card Logic
                if (card != null) {
                    card.handleServerTick(this);
                }
            }
        } else {
            lifetime--;
            if (card != null) {
                card.handleClientTick(this);
            }
        }

    }

    private void SyncToClient() {
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
    }

    public BlockCard getCardType() {
        return card;
    }

    public void initializingData(BlockCard card) {
        this.card = card;
        lifetime = card.getLifetime();
        preparedVanish = false;
    }


    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        card = BlockCards.fromByte(nbt.getByte("card_type"));
        lifetime = nbt.getInt("lifetime");
        preparedVanish = nbt.getBoolean("should_disappear");
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        CompoundNBT compoundNBT = super.save(nbt);
        compoundNBT.putByte("card_type", BlockCards.toByte(card));
        compoundNBT.putInt("lifetime", lifetime);
        compoundNBT.putBoolean("should_disappear", preparedVanish);
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
        compoundNBT.putByte("card_type", BlockCards.toByte(card));
        compoundNBT.putBoolean("should_disappear", preparedVanish);
        compoundNBT.putInt("lifetime",lifetime);
        return compoundNBT;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        card = BlockCards.fromByte(tag.getByte("card_type"));
        preparedVanish = tag.getBoolean("should_disappear");
        lifetime = tag.getInt("lifetime");
    }

}
