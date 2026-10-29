package eldritch.surge.enchantment.mechanics;

/** Implemented by creepers so a successful sword hit can extend their vanilla fuse. */
public interface FearDelayTarget {
    void eldrichSurge$delayExplosion();
}
