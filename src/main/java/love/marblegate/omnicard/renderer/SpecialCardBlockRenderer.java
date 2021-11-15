package love.marblegate.omnicard.renderer;


import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.model.SpecialCardBlockModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SpecialCardBlockRenderer extends GeoBlockRenderer<SpecialCardBlockTileEntity> {
    public SpecialCardBlockRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new SpecialCardBlockModel());
    }
}
