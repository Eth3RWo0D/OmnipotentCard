package love.marblegate.omnicard.item;

import com.google.common.collect.Lists;
import love.marblegate.omnicard.OmniCard;
import love.marblegate.omnicard.capability.cardtype.CardTypeData;
import love.marblegate.omnicard.capability.cardtype.CardTypeItemStackProvider;
import love.marblegate.omnicard.capability.cardtype.ICardTypeData;
import love.marblegate.omnicard.entity.CardEntity;
import love.marblegate.omnicard.misc.CardType;
import love.marblegate.omnicard.misc.ModGroup;
import love.marblegate.omnicard.registry.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

public class CardStack extends Item{
    public static ResourceLocation CARD_CATEGORY_PROPERTY_NAME = new ResourceLocation(OmniCard.MODID,"card_type");
    private static final List<CardType> availableCardType = Lists.newArrayList(CardType.INK,CardType.RED,CardType.CORAL,CardType.GOLD,CardType.SEA_GREEN,CardType.AZURE,CardType.CERULEAN_BLUE,CardType.HELIOTROPE);

    public CardStack() {
        super(new Properties().tab(ModGroup.GENERAL));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand hand) {
        if (!worldIn.isClientSide()) {
            ICardTypeData cardTypeData = player.getItemInHand(hand).getCapability(CardTypeData.CARD_TYPE_DATA_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));

            if(!player.isShiftKeyDown()){
                if(cardTypeData.isSwitchingCard()){
                    // stop card picking
                    cardTypeData.setSwitchingCard(false);
                } else {
                    // Throwing Card
                    if(player.abilities.instabuild || hasEnoughBlankCard(player)){
                        Vector3d vector3d = player.getViewVector(1.0F);

                        double x = (vector3d.x * 4D);
                        double y = (vector3d.y * 4D);
                        double z = (vector3d.z * 4D);

                        CardEntity cardEntity = new CardEntity(player, x, y, z, worldIn, cardTypeData.get());
                        cardEntity.setPos(player.getX(), player.getY() + player.getEyeHeight(player.getPose()), player.getZ());
                        worldIn.addFreshEntity(cardEntity);
                        if(!player.abilities.instabuild){
                            consumeBlankCard(player);
                        }
                    } else {
                        return ActionResult.fail(player.getItemInHand(hand));
                    }
                }
            } else {
                // enable card picking
                cardTypeData.setSwitchingCard(true);
            }

        }
        return ActionResult.sidedSuccess( player.getItemInHand(hand),worldIn.isClientSide());
    }

    private boolean hasEnoughBlankCard(PlayerEntity player){
        for(ItemStack itemStack: player.inventory.items){
            if(itemStack.getItem().equals(ItemRegistry.BLANK_CARD.get())) return true;
        }
        return false;
    }

    private void consumeBlankCard(PlayerEntity player){
        for(ItemStack itemStack: player.inventory.items){
            if(itemStack.getItem().equals(ItemRegistry.BLANK_CARD.get())) {
                itemStack.shrink(1);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClientSide() && selected){
            ICardTypeData cardTypeData = itemStack.getCapability(CardTypeData.CARD_TYPE_DATA_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
            if(world.getDayTime()%10==0 && cardTypeData.isSwitchingCard()){
                cardTypeData.set(switchingToNextCard(cardTypeData.get()));
            }
        }
    }

    private CardType switchingToNextCard(CardType cardType){
        int position = availableCardType.indexOf(cardType);
        if(position!=availableCardType.size()-1){
            return availableCardType.get(position+1);
        }
        else return availableCardType.get(0);
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT compoundNBT = super.getShareTag(stack);
        if(compoundNBT == null){
            compoundNBT = new CompoundNBT();
        }
        ICardTypeData cardTypeData = stack.getCapability(CardTypeData.CARD_TYPE_DATA_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
        compoundNBT.putString("card_type",cardTypeData.get().name());
        return compoundNBT;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null) {
            ICardTypeData cardTypeData = stack.getCapability(CardTypeData.CARD_TYPE_DATA_CAPABILITY, null).orElseThrow(() -> new IllegalArgumentException("Capability of CardTypeData goes wrong!"));
            cardTypeData.set(CardType.valueOf(nbt.getString("card_type")));
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CardTypeItemStackProvider();
    }
}
