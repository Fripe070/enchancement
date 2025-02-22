/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.singleenchantmentmode;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract boolean hasEnchantments();

	@Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
	private void enchancement$singleEnchantmentMode(Enchantment enchantment, int level, CallbackInfo ci) {
		if (ModConfig.singleEnchantmentMode && hasEnchantments()) {
			ci.cancel();
		}
	}
}
