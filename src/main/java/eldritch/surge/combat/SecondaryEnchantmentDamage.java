package eldritch.surge.combat;

/** Prevents damage/explosions produced by an enchantment from recursively proccing it. */
public final class SecondaryEnchantmentDamage {
    private static final ThreadLocal<Boolean> ACTIVE = new ThreadLocal<>();

    private SecondaryEnchantmentDamage() {}

    public static boolean isActive() { return Boolean.TRUE.equals(ACTIVE.get()); }

    public static void run(Runnable effect) {
        if (isActive()) return;
        ACTIVE.set(true);
        try {
            effect.run();
        } finally {
            ACTIVE.remove();
        }
    }
}
