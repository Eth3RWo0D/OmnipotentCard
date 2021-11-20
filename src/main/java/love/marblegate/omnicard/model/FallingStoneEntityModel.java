package love.marblegate.omnicard.model;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.entity.FallingStoneEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FallingStoneEntityModel extends AnimatedGeoModel<FallingStoneEntity> {
    @Override
    public ResourceLocation getModelLocation(FallingStoneEntity object) {
        return new ResourceLocation(OmniCard.MODID, "geo/entity/falling_stone.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FallingStoneEntity object) {
        return new ResourceLocation(OmniCard.MODID, "textures/entity/stone_entity.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(FallingStoneEntity animatable) {
        return new ResourceLocation(OmniCard.MODID, "animations/entity/falling_stone.animation.json");
    }
}
