package love.marblegate.omnicard.item;

import love.marblegate.omnicard.card.CommonCards;
import love.marblegate.omnicard.entity.FlyingCardEntity;
import love.marblegate.omnicard.misc.Configuration;
import love.marblegate.omnicard.misc.ModGroup;
import love.marblegate.omnicard.registry.SoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlankCard extends Item {

    public BlankCard() {
        super(new Properties().tab(ModGroup.GENERAL));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!worldIn.isClientSide()) {
            Vec3 vector3d = player.getViewVector(1.0F);

            double x = (vector3d.x * Configuration.FLYING_CARD_SPEED.get());
            double y = (vector3d.y * Configuration.FLYING_CARD_SPEED.get());
            double z = (vector3d.z * Configuration.FLYING_CARD_SPEED.get());

            FlyingCardEntity flyingCardEntity = new FlyingCardEntity(player, x, y, z, worldIn, CommonCards.BLANK);
            flyingCardEntity.setOwner(player);
            flyingCardEntity.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
            worldIn.addFreshEntity(flyingCardEntity);

            double d0 = -Mth.sin(player.getYRot() * ((float)Math.PI / 180F));
            double d1 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
            ((ServerLevel)worldIn).sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);

            worldIn.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundRegistry.THROW_CARD.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, worldIn.isClientSide());
    }
}
