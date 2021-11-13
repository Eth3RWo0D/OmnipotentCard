package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.entity.CardEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, OmniCard.MODID);
    public static final RegistryObject<EntityType<CardEntity>> CARD = ENTITIES.register("card",
            () -> EntityType.Builder.<CardEntity>of(CardEntity::new, EntityClassification.MISC)
                    .sized(0.3F, 0.06F)
                    .clientTrackingRange(4).updateInterval(5)
                    .build("card"));

}
