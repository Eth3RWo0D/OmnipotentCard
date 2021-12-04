package love.marblegate.omnicard.card;

import love.marblegate.omnicard.block.tileentity.SpecialCardBlockTileEntity;

import java.util.Objects;


public class BlockCard extends AbstractCard {
    private final CardFunc.ISpecialCardBlockTick serverTickHandler;
    private final CardFunc.ISpecialCardBlockTick clientTickHandler;
    private final int lifetime;
    private final boolean canRetrieveByBreak;

    public int getLifetime() {
        return lifetime;
    }

    public boolean canRetrieve() {
        return canRetrieveByBreak;
    }


    public BlockCard(Builder builder) {
        super(builder);
        serverTickHandler = builder.serverTickHandler;
        clientTickHandler = builder.clientTickHandler;
        lifetime = builder.lifetime;
        canRetrieveByBreak = builder.canRetrieveByBreak;

    }

    public void handleServerTick(SpecialCardBlockTileEntity specialCardBlockTile) {
        if (specialCardBlockTile != null && serverTickHandler!=null) {
            serverTickHandler.handle(specialCardBlockTile);
        }
    }

    public void handleClientTick(SpecialCardBlockTileEntity specialCardBlockTile) {
        if (specialCardBlockTile != null && clientTickHandler!=null) {
            clientTickHandler.handle(specialCardBlockTile);
        }
    }

    public static class Builder extends AbstractCard.Builder<Builder> {
        private CardFunc.ISpecialCardBlockTick serverTickHandler;
        private CardFunc.ISpecialCardBlockTick clientTickHandler;
        private final int lifetime;
        private boolean canRetrieveByBreak = false;

        public Builder(String name, String category, int lifetime) {
            super(name, category);
            this.lifetime = lifetime;
        }

        public Builder isRetrievableWhenBreak() {
            canRetrieveByBreak = true;
            return this;
        }

        @Override
        public BlockCard build() {
            return new BlockCard(this);
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder setServerTickHandler(CardFunc.ISpecialCardBlockTick serverTickHandler) {
            this.serverTickHandler = Objects.requireNonNull(serverTickHandler);
            return this;
        }

        public Builder setClientTickHandler(CardFunc.ISpecialCardBlockTick serverTickHandler) {
            this.clientTickHandler = Objects.requireNonNull(serverTickHandler);
            return this;
        }


    }
}
