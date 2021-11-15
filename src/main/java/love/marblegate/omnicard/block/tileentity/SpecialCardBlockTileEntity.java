package love.marblegate.omnicard.block.tileentity;

import love.marblegate.omnicard.registry.TileEntityRegistry;
import net.minecraft.tileentity.TileEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SpecialCardBlockTileEntity extends TileEntity implements IAnimatable {
    public SpecialCardBlockTileEntity() {
        super(TileEntityRegistry.SPECIAL_CARD_BLOCK_TILEENTITY.get());
    }

    @Override
    public void registerControllers(AnimationData data) {
        //TODO
    }

    @Override
    public AnimationFactory getFactory() {
        return null; //TODO
    }
}
