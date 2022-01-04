package love.marblegate.omnicard.renderer;

import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.model.FlyingCardEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class FlyingCardEntityRenderer extends GeoProjectilesRenderer<FlyingCardEntity> {

    public FlyingCardEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FlyingCardEntityModel());
        shadowRadius = 0.03F;
    }
}
