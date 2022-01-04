package love.marblegate.omnicard.renderer;


import love.marblegate.omnicard.block.blockentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.model.SpecialCardBlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SpecialCardBlockRenderer extends GeoBlockRenderer<SpecialCardBlockTileEntity> {

    public SpecialCardBlockRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new SpecialCardBlockModel());
    }
}
