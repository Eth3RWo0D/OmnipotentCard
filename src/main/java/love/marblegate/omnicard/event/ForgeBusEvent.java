package love.marblegate.omnicard.event;

import love.marblegate.omnicard.block.blockentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.card.BlockCards;
import love.marblegate.omnicard.misc.MiscUtil;
import love.marblegate.omnicard.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEvent {
    @SubscribeEvent
    public static void preventHostileNearSEALCard(LivingSpawnEvent.CheckSpawn event) {
        // It only stops natural spawning
        if (event.getSpawnReason() == MobSpawnType.NATURAL) {
            if (MiscUtil.isHostile(event.getEntityLiving(), false)) {
                for (BlockPos pos : BlockPos.betweenClosed(new BlockPos(event.getX() - 8, (int) event.getY() - 8, event.getZ() - 8), new BlockPos(event.getX() + 8, event.getY() + 8, event.getZ() + 8))) {
                    BlockState blockState = event.getWorld().getBlockState(pos);
                    if (blockState.getBlock() == BlockRegistry.SPECIAL_CARD_BLOCK.get()) {
                        BlockEntity tileEntity = event.getWorld().getBlockEntity(pos);
                        if (tileEntity instanceof SpecialCardBlockTileEntity) {
                            SpecialCardBlockTileEntity specialCardBlockTile = (SpecialCardBlockTileEntity) tileEntity;
                            if (specialCardBlockTile.getCardType() == BlockCards.SEAL) {
                                event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
                            }
                        }
                    }
                }
            }
        }
    }
}
