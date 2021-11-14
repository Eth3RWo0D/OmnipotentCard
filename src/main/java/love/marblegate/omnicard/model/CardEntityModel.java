package love.marblegate.omnicard.model;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.entity.CardEntity;
import love.marblegate.omnicard.misc.CardType;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CardEntityModel extends AnimatedGeoModel<CardEntity> {
    @Override
    public ResourceLocation getModelLocation(CardEntity object) {
        return new ResourceLocation(OmniCard.MODID, "geo/entity/card.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CardEntity object) {
        String type = object.getCardType();
        return new ResourceLocation(OmniCard.MODID, "textures/card/" + type + ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CardEntity animatable) {
        return new ResourceLocation(OmniCard.MODID, "animations/entity/card.animation.json");
    }
}
