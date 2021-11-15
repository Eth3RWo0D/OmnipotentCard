package love.marblegate.omnicard.renderer;

import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.model.FlyingCardEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class FlyingCardEntityRenderer extends GeoProjectilesRenderer<FlyingCardEntity> {
    public FlyingCardEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager, new FlyingCardEntityModel());
        shadowRadius = 0.03F;
    }
}
