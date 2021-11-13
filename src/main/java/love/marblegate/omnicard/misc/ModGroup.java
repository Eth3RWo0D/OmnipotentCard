package love.marblegate.omnicard.misc;

import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModGroup {

    public final static GeneralGroup GENERAL = new GeneralGroup();

    public static class GeneralGroup extends ItemGroup{

        public GeneralGroup() {
            super("omni_card.general");
        }

        @Override
        public ItemStack makeIcon() {
            return ItemRegistry.BLANK_CARD.get().getDefaultInstance();
        }
    }
}
