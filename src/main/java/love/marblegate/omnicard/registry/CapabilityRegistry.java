package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.capability.cardtype.CardTypeData;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityRegistry {
    @SubscribeEvent
    public static void onSetUpEvent(RegisterCapabilitiesEvent event) {
        event.register(CardTypeData.class);
    }
}
