package eldritch.surge.client;

public final class ShulkerGlintState {
    private static final ThreadLocal<Boolean> GLINT_ACTIVE = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private ShulkerGlintState() {
    }

    public static void set(boolean active) {
        if (active) {
            GLINT_ACTIVE.set(Boolean.TRUE);
        } else {
            GLINT_ACTIVE.remove();
        }
    }

    public static boolean isActive() {
        return Boolean.TRUE.equals(GLINT_ACTIVE.get());
    }
}
