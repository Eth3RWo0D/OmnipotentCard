package love.marblegate.omnicard.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import love.marblegate.omnicard.registry.ParticleRegistry;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class EmptyParticleType extends ParticleType<EmptyParticleType> implements IParticleData {
    private static final IParticleData.IDeserializer<EmptyParticleType> DESERIALIZE = new IDeserializer<EmptyParticleType>() {
        @Override
        public EmptyParticleType fromCommand(ParticleType<EmptyParticleType> p_197544_1_, StringReader p_197544_2_) throws CommandSyntaxException {
            return new EmptyParticleType();
        }

        @Override
        public EmptyParticleType fromNetwork(ParticleType<EmptyParticleType> p_197543_1_, PacketBuffer p_197543_2_) {
            return new EmptyParticleType();
        }
    };

    public EmptyParticleType() {
        super(false, DESERIALIZE);
    }

    @Override
    public Codec<EmptyParticleType> codec() {
        return Codec.unit(new EmptyParticleType());
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.EMPTY.get();
    }

    @Override
    public void writeToNetwork(PacketBuffer p_197553_1_) {
    }

    @Override
    public String writeToString() {
        return "";
    }
}
