/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.slide.client;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow
	@Final
	MinecraftClient client;

	@Inject(method = "bobView", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;", ordinal = 1), cancellable = true)
	private void enchancement$slide(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		if (ModEntityComponents.SLIDE.get(client.getCameraEntity()).isSliding()) {
			ci.cancel();
		}
	}
}
