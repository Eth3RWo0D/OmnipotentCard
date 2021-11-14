package love.marblegate.omnicard.capability.cardtype;

import love.marblegate.omnicard.misc.CardType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CardTypeData {
    @CapabilityInject(ICardTypeData.class)
    public static final Capability<ICardTypeData> CARD_TYPE_DATA_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(ICardTypeData.class, new Storage(), CardTypeDataImpl::new);
    }

    public static class Storage implements Capability.IStorage<ICardTypeData> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ICardTypeData> capability, ICardTypeData instance, Direction side) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putString("card_type", instance.get().name());
            compoundNBT.putBoolean("is_switching", instance.isSwitchingCard());
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<ICardTypeData> capability, ICardTypeData instance, Direction side, INBT nbt) {
            instance.set(CardType.valueOf(((CompoundNBT) nbt).getString("card_type")));
            instance.setSwitchingCard((((CompoundNBT) nbt).getBoolean("is_switching")));
        }
    }
}
