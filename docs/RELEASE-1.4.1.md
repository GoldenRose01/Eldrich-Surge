# Eldritch Surge 1.4.1

For Minecraft 26.3 · Fabric

- Fixed an AnvilEnchantmentsMixin startup failure by accessing inherited ItemCombinerMenu fields through its superclass instead of declaring shadows on AnvilMenu.
- Fixed the ReplenishAmmoMixin HEAD callback signature to match the ItemStack return type of useAmmo.

Validation: the corrected code was built and launched successfully in the development client before this version update. The 1.4.1 release is rebuilt with the updated version metadata.
