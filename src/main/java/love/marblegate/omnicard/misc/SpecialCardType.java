package love.marblegate.omnicard.misc;

import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;
import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;
import java.util.stream.Collectors;


public enum SpecialCardType implements IStringSerializable {
    // Special Card
    FIELD("field_card",24000,true, ItemRegistry.FIELD_CARD, tileEntity -> {
        if(tileEntity.getLifetime() % 10 == 0){
            MiscUtil.applyHolyFlameInArea((ServerWorld) tileEntity.getLevel(),
                    MiscUtil.buildAABB(tileEntity.getBlockPos(),8),200);
        }
    }),
    PURIFICATION("purification_card",100,false, ItemRegistry.PURIFICATION_CARD, tileEntity -> {
        if(tileEntity.getLifetime() == 90){
            MiscUtil.applyHolyFlameInArea((ServerWorld) tileEntity.getLevel(),
                    MiscUtil.buildAABB(tileEntity.getBlockPos(),8),200);
        }
    }),
    SEAL("seal_card",-1,true, ItemRegistry.SEAL_CARD, tileEntity -> {}), // Event Handler is in MiscUtil.java
    SUNNY("sunny_card",24000,false, ItemRegistry.SUNNY_CARD, tileEntity -> {
        if(tileEntity.getLevel().dimension() == World.OVERWORLD){
            ((ServerWorld) tileEntity.getLevel()).setWeatherParameters(24000,0,false,false);
        }
    }),
    RAINY("rainy_card",24000,false,  ItemRegistry.RAINY_CARD, tileEntity -> {
        if(tileEntity.getLevel().dimension() == World.OVERWORLD){
            ((ServerWorld) tileEntity.getLevel()).setWeatherParameters(0,24000,true,false);
        }
    }),
    THUNDERSTORM("thunderstorm_card",24000,false,  ItemRegistry.THUNDERSTORM_CARD, tileEntity -> {
        if(tileEntity.getLevel().dimension() == World.OVERWORLD){
            ((ServerWorld) tileEntity.getLevel()).setWeatherParameters(0,24000,true,true);
        }
    }),
    BLOOM("bloom_card",-1,true, ItemRegistry.BLOOM_CARD, tileEntity -> {
        int randomTickRate = tileEntity.getLevel().getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        for(int l = 0; l < randomTickRate; ++l) {
            BlockPos blockpos = MiscUtil.getBlockRandomPos(tileEntity.getBlockPos().getX(), tileEntity.getBlockPos().getY(), tileEntity.getBlockPos().getZ(), 8, tileEntity.getLevel().random);
            BlockState blockstate = tileEntity.getLevel().getBlockState(blockpos);
            if (blockstate.isRandomlyTicking()) {
                blockstate.randomTick((ServerWorld) tileEntity.getLevel(), blockpos,tileEntity.getLevel().random);
            }
        }
    });

    public String name;
    public int lifetimeAfterPlace;
    public boolean canRetrieveByBreak;
    public Supplier<Item> retrievedItem;
    public ISpecialCardBlockServerTick serverTickLogic;

    SpecialCardType(String i,int lifetimeAfterPlace,boolean canRetrieveByBreak, Supplier<Item> retrievedItem, ISpecialCardBlockServerTick serverTickLogic) {
        name = i;
        this.lifetimeAfterPlace = lifetimeAfterPlace;
        this.canRetrieveByBreak = canRetrieveByBreak;
        this.retrievedItem = retrievedItem;
        this.serverTickLogic = serverTickLogic;
    }

    @Override
    public String getSerializedName() {
        return name();
    }

    @FunctionalInterface
    public interface ISpecialCardBlockServerTick{
        void handle(SpecialCardBlockTileEntity tileEntity);
    }

    public String getTexturePath() {
        return "special/" + name + ".png";
    }


}
