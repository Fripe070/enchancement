/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client;

import moriyashiine.enchancement.client.event.*;
import moriyashiine.enchancement.client.packet.*;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.client.render.entity.AmethystShardEntityRenderer;
import moriyashiine.enchancement.client.render.entity.BrimstoneEntityRenderer;
import moriyashiine.enchancement.client.render.entity.IceShardEntityRenderer;
import moriyashiine.enchancement.client.render.entity.TorchEntityRenderer;
import moriyashiine.enchancement.client.render.entity.mob.FrozenPlayerEntityRenderer;
import moriyashiine.enchancement.client.screen.EnchantingTableScreen;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import moriyashiine.enchancement.common.registry.ModScreenHandlerTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.brimstone.CrossbowItemAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;

@Environment(EnvType.CLIENT)
public class EnchancementClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(AddStrafeParticlesPacket.ID, AddStrafeParticlesPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AddGaleParticlesPacket.ID, AddGaleParticlesPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(PlayBrimstoneSoundPacket.ID, PlayBrimstoneSoundPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(StopBrimstoneSoundsS2CPacket.ID, StopBrimstoneSoundsS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(ResetFrozenTicksPacket.ID, ResetFrozenTicksPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusS2C.ID, SyncFrozenPlayerSlimStatusS2C::receive);
		ClientPlayNetworking.registerGlobalReceiver(AddMoltenParticlesPacket.ID, AddMoltenParticlesPacket::receive);
		EntityRendererRegistry.register(ModEntityTypes.FROZEN_PLAYER, FrozenPlayerEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.ICE_SHARD, IceShardEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.BRIMSTONE, BrimstoneEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.AMETHYST_SHARD, AmethystShardEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.TORCH, TorchEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.GRAPPLE_FISHING_BOBBER, FishingBobberEntityRenderer::new);
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, Enchancement.id("brimstone"), (stack, world, entity, seed) -> {
			if (CrossbowItemAccessor.enchancement$getProjectiles(stack).stream().anyMatch(foundStack -> ItemStack.areEqual(foundStack, EnchancementUtil.BRIMSTONE_STACK))) {
				return stack.getSubNbt(Enchancement.MOD_ID).getInt("BrimstoneDamage") / 12F;
			}
			return 0;
		});
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, Enchancement.id("amethyst_shard"), (stack, world, entity, seed) -> CrossbowItem.hasProjectile(stack, Items.AMETHYST_SHARD) ? 1 : 0);
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, Enchancement.id("torch"), (stack, world, entity, seed) -> CrossbowItem.hasProjectile(stack, Items.TORCH) ? 1 : 0);
		HandledScreens.register(ModScreenHandlerTypes.ENCHANTING_TABLE, EnchantingTableScreen::new);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(FrozenReloadListener.INSTANCE);
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(Enchancement.id("alternate_dash"), modContainer, ResourcePackActivationType.NORMAL));
		initEvents();
	}

	private void initEvents() {
		ItemTooltipCallback.EVENT.register(new AssimilationTooltipEvent());
		HudRenderCallback.EVENT.register(new DashRenderEvent());
		HudRenderCallback.EVENT.register(new BouncyRenderEvent());
		HudRenderCallback.EVENT.register(new GaleRenderEvent());
		HudRenderCallback.EVENT.register(new BrimstoneRenderEvent());
	}
}
