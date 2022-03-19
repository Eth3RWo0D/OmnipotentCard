package love.marblegate.omnicard.model;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.entity.CardTrapEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import java.util.UUID;

public class CardTrapEntityModel extends AnimatedGeoModel<CardTrapEntity> {
    @Override
    public ResourceLocation getModelLocation(CardTrapEntity object) {
        return new ResourceLocation(OmniCard.MODID, "geo/entity/card.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CardTrapEntity object) {
        UUID uuid = object.getOwnerUUID();
        boolean flag = false;
        if(uuid != null && Minecraft.getInstance().player.getUUID()!=null)
            if (uuid.equals(Minecraft.getInstance().player.getUUID()))
                flag = true;
        if(flag)
            return new ResourceLocation(OmniCard.MODID, "textures/card/" + object.getCardType().getTexturePath());
        else
            return new ResourceLocation(OmniCard.MODID, "textures/card/trap_unknown.png");

    }

    @Override
    public ResourceLocation getAnimationFileLocation(CardTrapEntity animatable) {
        return new ResourceLocation(OmniCard.MODID, "animations/entity/card.animation.json");
    }
}
