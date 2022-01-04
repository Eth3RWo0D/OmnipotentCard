package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.renderer.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RendererRegistry {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetUpEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.FLYING_CARD.get(), FlyingCardEntityRenderer::new);
        event.registerEntityRenderer(EntityRegistry.CARD_TRAP.get(), CardTrapEntityRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FALLING_STONE.get(), FallingStoneEntityRenderer::new);
        event.registerEntityRenderer(EntityRegistry.STONE_SPIKE.get(), StoneSpikeEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.SPECIAL_CARD_BLOCK_TILEENTITY.get(), SpecialCardBlockRenderer::new);


    }
}
