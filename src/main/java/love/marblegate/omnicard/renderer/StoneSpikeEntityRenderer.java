package love.marblegate.omnicard.renderer;

import love.marblegate.omnicard.entity.StoneSpikeEntity;
import love.marblegate.omnicard.model.StoneSpikeEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class StoneSpikeEntityRenderer extends GeoProjectilesRenderer<StoneSpikeEntity> {
    public StoneSpikeEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StoneSpikeEntityModel());
    }
}
