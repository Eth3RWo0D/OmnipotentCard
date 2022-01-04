package love.marblegate.omnicard.capability.cardtype;

import love.marblegate.omnicard.card.CommonCards;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CardTypeItemStackProvider implements ICapabilitySerializable<CompoundTag> {
    private final CardTypeData imp = new CardTypeData();
    private final LazyOptional<CardTypeData> impOptional = LazyOptional.of(() -> imp);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CardTypeData.CAPABILITY) {
            return impOptional.cast();
        } else return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        if (CardTypeData.CAPABILITY != null) {
            compoundNBT.putByte("card_type", CommonCards.toByte(imp.get()));
            compoundNBT.putBoolean("is_switching", imp.isSwitchingCard());
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (CardTypeData.CAPABILITY != null) {
            imp.set(CommonCards.fromByte(((CompoundTag) nbt).getByte("card_type")));
            imp.setSwitchingCard((((CompoundTag) nbt).getBoolean("is_switching")));
        }
    }
}
