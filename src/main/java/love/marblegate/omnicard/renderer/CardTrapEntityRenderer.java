package love.marblegate.omnicard.renderer;

import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.model.CardTrapEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class CardTrapEntityRenderer extends GeoProjectilesRenderer<CardTrapEntity> {
    public CardTrapEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager, new CardTrapEntityModel());
        shadowRadius = 0.03F;
    }
}
