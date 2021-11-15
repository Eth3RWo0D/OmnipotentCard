package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, OmniCard.MODID);
    public static final RegistryObject<EntityType<FlyingCardEntity>> FLYING_CARD = ENTITIES.register("flying_card",
            () -> EntityType.Builder.<FlyingCardEntity>of(FlyingCardEntity::new, EntityClassification.MISC)
                    .sized(.4F, .06F)
                    .clientTrackingRange(8).updateInterval(5)
                    .build("flying_card"));
    public static final RegistryObject<EntityType<CardTrapEntity>> CARD_TRAP = ENTITIES.register("card_trap",
            () -> EntityType.Builder.<CardTrapEntity>of(CardTrapEntity::new, EntityClassification.MISC)
                    .sized(.3F, .02F)
                    .clientTrackingRange(10)
                    .build("card_trap"));

}
