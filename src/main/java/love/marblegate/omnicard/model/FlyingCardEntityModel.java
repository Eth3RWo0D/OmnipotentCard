package love.marblegate.omnicard.model;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FlyingCardEntityModel extends AnimatedGeoModel<FlyingCardEntity> {
    @Override
    public ResourceLocation getModelLocation(FlyingCardEntity object) {
        return new ResourceLocation(OmniCard.MODID, "geo/entity/card.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FlyingCardEntity object) {
        return new ResourceLocation(OmniCard.MODID, "textures/card/" + object.getCardType().getTexturePath());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(FlyingCardEntity animatable) {
        return new ResourceLocation(OmniCard.MODID, "animations/entity/card.animation.json");
    }
}
