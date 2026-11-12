package eldritch.surge.combat;

public final class SecondaryEnchantmentDamageTest {
    public static void main(String[] args) {
        int[] calls = {0};
        SecondaryEnchantmentDamage.run(() -> {
            if (!SecondaryEnchantmentDamage.isActive()) throw new AssertionError("Missing guard");
            calls[0]++;
            SecondaryEnchantmentDamage.run(() -> { throw new AssertionError("Recursive proc"); });
        });
        if (calls[0] != 1 || SecondaryEnchantmentDamage.isActive()) throw new AssertionError("Leaked guard");
        try {
            SecondaryEnchantmentDamage.run(() -> { throw new IllegalStateException("test"); });
            throw new AssertionError("Swallowed exception");
        } catch (IllegalStateException expected) {
            if (SecondaryEnchantmentDamage.isActive()) throw new AssertionError("Leaked guard after failure");
        }
        SecondaryEnchantmentDamage.run(() -> calls[0]++);
        if (calls[0] != 2) throw new AssertionError("Later attacks blocked");
        System.out.println("Validated recursion prevention and guard cleanup after exceptions.");
    }
}
