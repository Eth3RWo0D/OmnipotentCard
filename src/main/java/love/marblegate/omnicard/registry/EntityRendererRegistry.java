package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.renderer.CardTrapEntityRenderer;
import love.marblegate.omnicard.renderer.FlyingCardEntityRenderer;
import love.marblegate.omnicard.renderer.SpecialCardBlockRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRendererRegistry {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetUpEvent(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.FLYING_CARD.get(), FlyingCardEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.CARD_TRAP.get(), CardTrapEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.SPECIAL_CARD_BLOCK_TILEENTITY.get(), SpecialCardBlockRenderer::new);


    }
}
