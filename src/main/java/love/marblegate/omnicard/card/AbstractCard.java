package love.marblegate.omnicard.card;

import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractCard {
    final String name;
    final String category;
    final Supplier<Item> retrievedItem;

    protected AbstractCard(Builder<?> builder) {
        name = builder.name;
        category = builder.category;
        retrievedItem = builder.retrievedItem;
    }

    public String getCardName() {
        return name;
    }

    public Optional<Item> getRetrievedItem() {
        if (retrievedItem == null)
            return Optional.empty();
        else return Optional.of(retrievedItem.get());
    }

    public String getTexturePath() {
        return category + "/" + name + ".png";
    }

    public abstract static class Builder<T extends Builder<T>> {
        private final String name;
        private final String category;
        private Supplier<Item> retrievedItem;

        public Builder(String name, String category) {
            this.name = name;
            this.category = category;
        }

        public T setRetrievedItem(Supplier<Item> retrievedItem) {
            this.retrievedItem = retrievedItem;
            return self();
        }

        public abstract AbstractCard build();

        protected abstract T self();

    }
}
