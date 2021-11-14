package love.marblegate.omnicard.misc;

public enum CardType {
    BLANK("white_card"),
    RED("red_card"),
    CORAL("orange_card"),
    GOLD("gold_card"),
    SEA_GREEN("green_card"),
    AZURE("sky_card"),
    CERULEAN_BLUE("blue_card"),
    HELIOTROPE("violet_card"),
    INK("black_card");

    public String name;

    CardType(String i) {
        name = i;
    }
}
