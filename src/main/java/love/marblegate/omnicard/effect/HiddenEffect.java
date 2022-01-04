package love.marblegate.omnicard.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.EffectRenderer;

import java.util.function.Consumer;

public class HiddenEffect extends MobEffect {
    public HiddenEffect(MobEffectCategory p_i50391_1_) {
        super(p_i50391_1_, 0);
    }

    @Override
    public void initializeClient(Consumer<EffectRenderer> consumer) {
        consumer.accept(new EffectRenderer() {
            @Override
            public boolean shouldRender(MobEffectInstance effect) {
                return false;
            }

            @Override
            public boolean shouldRenderInvText(MobEffectInstance effect) {
                return false;
            }

            @Override
            public boolean shouldRenderHUD(MobEffectInstance effect) {
                return false;
            }

            @Override
            public void renderInventoryEffect(MobEffectInstance effect, EffectRenderingInventoryScreen<?> gui, PoseStack mStack, int x, int y, float z) {
            }

            @Override
            public void renderHUDEffect(MobEffectInstance effect, GuiComponent gui, PoseStack mStack, int x, int y, float z, float alpha) {
            }
        });
    }
}
