package eldritch.surge.menu;

/** Upgrades pay only for added levels, normalized to the configured full enchantment cap. */
public final class AdvancedInfusingCosts {
    private AdvancedInfusingCosts() { }

    public static int experience(int addedLevels, int fullCap) {
        return experience(addedLevels, fullCap, "common");
    }

    public static int experience(int addedLevels, int fullCap, String rarityClass) {
        return cost(baseCostForRarity(rarityClass), addedLevels, fullCap);
    }

    public static int baseCostForRarity(String rarityClass) {
        if (rarityClass == null) return 30;
        return switch (rarityClass.toLowerCase(java.util.Locale.ROOT)) {
            case "uncommon" -> 40;
            case "rare" -> 50;
            case "very_rare" -> 65;
            default -> 30;
        };
    }

    public static int reagents(int addedLevels, int fullCap) {
        return cost(3, addedLevels, fullCap);
    }

    private static int cost(int fullPrice, int addedLevels, int fullCap) {
        if (addedLevels < 1 || fullCap < 1 || addedLevels > fullCap) return 0;
        return (int) Math.max(1, ((long) fullPrice * addedLevels + fullCap - 1) / fullCap);
    }
}
