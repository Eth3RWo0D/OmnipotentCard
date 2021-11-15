package love.marblegate.omnicard.model;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CardTrapEntityModel extends AnimatedGeoModel<CardTrapEntity> {
    @Override
    public ResourceLocation getModelLocation(CardTrapEntity object) {
        return new ResourceLocation(OmniCard.MODID, "geo/entity/card.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CardTrapEntity object) {
        return new ResourceLocation(OmniCard.MODID, "textures/card/" + object.getCardType().getTexturePath());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CardTrapEntity animatable) {
        return new ResourceLocation(OmniCard.MODID, "animations/entity/card.animation.json");
    }
}
