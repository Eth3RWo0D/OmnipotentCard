package love.marblegate.omnicard.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;

public class EmptyParticle extends TexturedParticle {
    public EmptyParticle(ClientWorld p_i232411_1_, double p_i232411_2_, double p_i232411_4_, double p_i232411_6_) {
        super(p_i232411_1_, p_i232411_2_, p_i232411_4_, p_i232411_6_);
    }

    @Override
    public void render(IVertexBuilder p_225606_1_, ActiveRenderInfo p_225606_2_, float p_225606_3_) {}

    @Override
    protected float getU0() {
        return 0;
    }

    @Override
    protected float getU1() {
        return 8;
    }

    @Override
    protected float getV0() {
        return 0;
    }

    @Override
    protected float getV1() {
        return 9;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.NO_RENDER;
    }
}
