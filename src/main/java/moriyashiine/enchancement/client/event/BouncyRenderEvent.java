/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BouncyRenderEvent implements HudRenderCallback {
	private static final Identifier ICONS = new Identifier("textures/gui/icons.png");
	private static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onHudRender(DrawContext context, float tickDelta) {
		ModEntityComponents.BOUNCY.maybeGet(client.getCameraEntity()).ifPresent(bouncyComponent -> {
			if (bouncyComponent.hasBouncy()) {
				float boostProgress = bouncyComponent.getBoostProgress();
				if (boostProgress > 0) {
					context.getMatrices().push();
					int width = client.getWindow().getScaledWidth() / 2 - 91;
					int height = client.getWindow().getScaledHeight() - 32 + 3;
					context.drawTexture(ICONS, width, height, 0, 84, 182, 5, 256, 256);
					context.drawTexture(ICONS, client.getWindow().getScaledWidth() / 2 - 91, height, 0, 89, (int) (182 * boostProgress), 5, 256, 256);
					context.getMatrices().pop();
				}
			}
		});
	}
}
