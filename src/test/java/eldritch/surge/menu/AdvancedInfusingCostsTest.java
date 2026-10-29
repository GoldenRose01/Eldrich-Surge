package eldritch.surge.menu;

public final class AdvancedInfusingCostsTest {
    public static void main(String[] args) {
        for (int cap : new int[]{1, 3, 5, 10, 255}) {
            if (AdvancedInfusingCosts.experience(cap, cap) != 30 || AdvancedInfusingCosts.reagents(cap, cap) != 3)
                throw new AssertionError("A full enchantment must cost 30 levels and 3 reagents.");
            int previous = 0;
            for (int added = 1; added <= cap; added++) {
                int xp = AdvancedInfusingCosts.experience(added, cap);
                if (xp < 1 || xp < previous || xp > 30 || AdvancedInfusingCosts.reagents(added, cap) < 1)
                    throw new AssertionError("Upgrade prices must be positive, bounded and monotonic.");
                previous = xp;
            }
        }
        if (AdvancedInfusingCosts.experience(1, 5) != 6 || AdvancedInfusingCosts.experience(2, 5) != 12)
            throw new AssertionError("Only added levels should be charged, including without the formation.");
        for (int[] invalid : new int[][]{{0, 5}, {-1, 5}, {6, 5}, {1, 0}})
            if (AdvancedInfusingCosts.experience(invalid[0], invalid[1]) != 0 || AdvancedInfusingCosts.reagents(invalid[0], invalid[1]) != 0)
                throw new AssertionError("Invalid upgrades cannot have payable prices.");
        System.out.println("Validated infusing costs, upgrade deltas, caps and invalid requests.");
    }
}
