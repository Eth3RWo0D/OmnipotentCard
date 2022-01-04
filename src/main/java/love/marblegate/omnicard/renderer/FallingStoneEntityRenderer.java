package love.marblegate.omnicard.renderer;

import love.marblegate.omnicard.entity.FallingStoneEntity;
import love.marblegate.omnicard.model.FallingStoneEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class FallingStoneEntityRenderer extends GeoProjectilesRenderer<FallingStoneEntity> {
    public FallingStoneEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FallingStoneEntityModel());
    }
}
