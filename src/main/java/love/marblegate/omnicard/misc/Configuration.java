package love.marblegate.omnicard.misc;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.DoubleValue FLYING_CARD_SPEED;
    public static ForgeConfigSpec.BooleanValue HURT_MOUNT;
    public static ForgeConfigSpec.BooleanValue HURT_PET;
    public static ForgeConfigSpec.DoubleValue FLYING_CARD_BRIGHTNESS;
    public static ForgeConfigSpec.DoubleValue TRAP_CARD_BRIGHTNESS;
    public static ForgeConfigSpec.IntValue FIELD_CARD_BRIGHTNESS;


    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("flying_card_setting");
        FLYING_CARD_SPEED = builder.comment("Flying Speed Of Card.", "Default is 15.0").defineInRange("FLYING_CARD_SPEED", 15.0, 0.1, 99.9);
        HURT_MOUNT = builder.comment("Should Flying Card Hurt Your Mount?").define("HURT_MOUNT", false);
        HURT_PET = builder.comment("Should Flying Card Hurt Your Pet?").define("HURT_PET", false);
        FLYING_CARD_SPEED = builder.comment("Flying Card Brightness.", "Default is 0.2").defineInRange("FLYING_CARD_SPEED", 0.2, 0.0, 1.0);
        builder.pop();

        builder.push("trap_card_setting");
        TRAP_CARD_BRIGHTNESS = builder.comment("Trap Card Brightness.", "Default is 0").defineInRange("TRAP_CARD_BRIGHTNESS", 0.0, 0.0, 1.0);
        builder.pop();

        builder.push("field_card_setting");
        FIELD_CARD_BRIGHTNESS = builder.comment("Field Card Brightness.", "Default is 3").defineInRange("FIELD_CARD_BRIGHTNESS", 3, 0, 15);
        builder.pop();
        COMMON_CONFIG = builder.build();
    }
}
