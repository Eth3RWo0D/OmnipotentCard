package love.marblegate.omnicard.misc;

import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModGroup {

    public final static GeneralGroup GENERAL = new GeneralGroup();

    public static class GeneralGroup extends CreativeModeTab {

        public GeneralGroup() {
            super("omni_card.general");
        }

        @Override
        public ItemStack makeIcon() {
            return ItemRegistry.BLANK_CARD.get().getDefaultInstance();
        }
    }
}
