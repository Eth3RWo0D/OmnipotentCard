package love.marblegate.omnicard.capability.cardtype;

import love.marblegate.omnicard.card.CommonCards;
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
            compoundNBT.putByte("card_type", CommonCards.toByte(instance.get()));
            compoundNBT.putBoolean("is_switching", instance.isSwitchingCard());
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<ICardTypeData> capability, ICardTypeData instance, Direction side, INBT nbt) {
            instance.set(CommonCards.fromByte(((CompoundNBT) nbt).getByte("card_type")));
            instance.setSwitchingCard((((CompoundNBT) nbt).getBoolean("is_switching")));
        }
    }
}
