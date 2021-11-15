package love.marblegate.omnicard.registry;

import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,  OmniCard.MODID);
    public static final RegistryObject<TileEntityType<SpecialCardBlockTileEntity>> SPECIAL_CARD_BLOCK_TILEENTITY = TILE_ENTITIES.register("special_card_block_tileentity", () -> TileEntityType.Builder.of(SpecialCardBlockTileEntity::new, BlockRegistry.SPECIAL_CARD_BLOCK.get()).build(null));
}
