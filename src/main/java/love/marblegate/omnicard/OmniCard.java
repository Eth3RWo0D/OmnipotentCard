package love.marblegate.omnicard;

import love.marblegate.omnicard.capability.cardtype.CardTypeData;
import love.marblegate.omnicard.capability.cardtype.ICardTypeData;
import love.marblegate.omnicard.item.CardStack;
import love.marblegate.omnicard.misc.CardType;
import love.marblegate.omnicard.registry.*;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("omni_card")
public class OmniCard {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "omni_card";

    public OmniCard() {
        GeckoLib.initialize();

        ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityRegistry.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        EffectRegistry.EFFECT.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TileEntityRegistry.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParticleRegistry.PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        /*// Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        */
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        // MinecraftForge.EVENT_BUS.register(this);
    }

   /* private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }*/

    private void doClientStuff(final FMLClientSetupEvent event) {
        // Item Property Override
        ItemModelsProperties.register(ItemRegistry.CARD_STACK.get(),
                CardStack.CARD_CATEGORY_PROPERTY_NAME, (stack, world, living) -> {
                    ICardTypeData cardTypeData = stack.getCapability(CardTypeData.CARD_TYPE_DATA_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
                    CardType cardType = cardTypeData.get();
                    if (cardType == CardType.RED) {
                        return 0.1F;
                    } else if (cardType == CardType.CORAL) {
                        return 0.2F;
                    } else if (cardType == CardType.GOLD) {
                        return 0.3F;
                    } else if (cardType == CardType.SEA_GREEN) {
                        return 0.4F;
                    } else if (cardType == CardType.AZURE) {
                        return 0.5F;
                    } else if (cardType == CardType.CERULEAN_BLUE) {
                        return 0.6F;
                    } else if (cardType == CardType.HELIOTROPE) {
                        return 0.7F;
                    } else return 0;
                });
    }

    /*
    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }*/
}
