package love.marblegate.omnicard.renderer;

import love.marblegate.omnicard.entity.CardEntity;
import love.marblegate.omnicard.model.CardEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class CardEntityRenderer extends GeoProjectilesRenderer<CardEntity> {
    public CardEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager, new CardEntityModel());
        shadowRadius = 0.03F;
    }
}
