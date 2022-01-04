package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.entity.CardTrapEntity;
import love.marblegate.omnicard.entity.FallingStoneEntity;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.entity.StoneSpikeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, OmniCard.MODID);
    public static final RegistryObject<EntityType<FlyingCardEntity>> FLYING_CARD = ENTITIES.register("flying_card",
            () -> EntityType.Builder.<FlyingCardEntity>of(FlyingCardEntity::new, MobCategory.MISC)
                    .sized(.6F, .06F)
                    .clientTrackingRange(10).updateInterval(5)
                    .build("flying_card"));
    public static final RegistryObject<EntityType<CardTrapEntity>> CARD_TRAP = ENTITIES.register("card_trap",
            () -> EntityType.Builder.<CardTrapEntity>of(CardTrapEntity::new, MobCategory.MISC)
                    .sized(.3F, .02F)
                    .clientTrackingRange(10)
                    .build("card_trap"));
    public static final RegistryObject<EntityType<FallingStoneEntity>> FALLING_STONE = ENTITIES.register("falling_stone",
            () -> EntityType.Builder.<FallingStoneEntity>of(FallingStoneEntity::new, MobCategory.MISC)
                    .sized(.8F, .3F)
                    .clientTrackingRange(10).updateInterval(5)
                    .build("falling_stone"));
    public static final RegistryObject<EntityType<StoneSpikeEntity>> STONE_SPIKE = ENTITIES.register("stone_spike",
            () -> EntityType.Builder.<StoneSpikeEntity>of(StoneSpikeEntity::new, MobCategory.MISC)
                    .sized(.8F, .8F)
                    .clientTrackingRange(10)
                    .build("stone_spike"));

}
