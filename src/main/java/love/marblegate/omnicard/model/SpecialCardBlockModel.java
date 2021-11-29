package love.marblegate.omnicard.model;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SpecialCardBlockModel extends AnimatedGeoModel<SpecialCardBlockTileEntity> {
    @Override
    public ResourceLocation getModelLocation(SpecialCardBlockTileEntity object) {
        return new ResourceLocation(OmniCard.MODID, "geo/block/special_card_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SpecialCardBlockTileEntity object) {
        if (object.getCardType() == null)
            return new ResourceLocation(OmniCard.MODID, "textures/card/standard/unknown_card.png");
        return new ResourceLocation(OmniCard.MODID, "textures/card/" + object.getCardType().getTexturePath());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SpecialCardBlockTileEntity animatable) {
        return new ResourceLocation(OmniCard.MODID, "animations/block/special_card_block.animation.json");
    }
}
