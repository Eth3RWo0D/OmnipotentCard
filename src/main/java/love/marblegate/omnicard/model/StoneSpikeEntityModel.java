package love.marblegate.omnicard.model;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.entity.StoneSpikeEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StoneSpikeEntityModel extends AnimatedGeoModel<StoneSpikeEntity> {
    @Override
    public ResourceLocation getModelLocation(StoneSpikeEntity object) {
        return new ResourceLocation(OmniCard.MODID, "geo/entity/stone_spike.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(StoneSpikeEntity object) {
        return new ResourceLocation(OmniCard.MODID, "textures/entity/stone_entity.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(StoneSpikeEntity animatable) {
        return new ResourceLocation(OmniCard.MODID, "animations/entity/stone_spike.animation.json");
    }
}
