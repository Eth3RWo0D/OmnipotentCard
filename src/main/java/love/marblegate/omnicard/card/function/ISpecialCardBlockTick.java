package love.marblegate.omnicard.card.function;

import love.marblegate.omnicard.block.blockentity.SpecialCardBlockTileEntity;

@FunctionalInterface
public interface ISpecialCardBlockTick {
    void handle(SpecialCardBlockTileEntity tileEntity);
}
