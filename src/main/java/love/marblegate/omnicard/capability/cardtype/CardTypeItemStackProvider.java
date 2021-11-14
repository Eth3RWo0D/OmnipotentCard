package love.marblegate.omnicard.capability.cardtype;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CardTypeItemStackProvider implements ICapabilitySerializable<CompoundNBT> {
    private final ICardTypeData imp = new CardTypeDataImpl();
    private final LazyOptional<ICardTypeData> impOptional = LazyOptional.of(() -> imp);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CardTypeData.CARD_TYPE_DATA_CAPABILITY) {
            return impOptional.cast();
        } else return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (CardTypeData.CARD_TYPE_DATA_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) CardTypeData.CARD_TYPE_DATA_CAPABILITY.writeNBT(imp, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (CardTypeData.CARD_TYPE_DATA_CAPABILITY != null) {
            CardTypeData.CARD_TYPE_DATA_CAPABILITY.readNBT(imp, null, nbt);
        }
    }
}
