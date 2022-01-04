package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.block.blockentity.SpecialCardBlockTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCKENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, OmniCard.MODID);
    public static final RegistryObject<BlockEntityType<SpecialCardBlockTileEntity>> SPECIAL_CARD_BLOCK_TILEENTITY = BLOCKENTITIES.register("special_card_block_tileentity", () -> BlockEntityType.Builder.of(SpecialCardBlockTileEntity::new, BlockRegistry.SPECIAL_CARD_BLOCK.get()).build(null));
}
