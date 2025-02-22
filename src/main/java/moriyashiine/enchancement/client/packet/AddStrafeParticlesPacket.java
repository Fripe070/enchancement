/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.StrafeComponent;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AddStrafeParticlesPacket {
	public static final Identifier ID = Enchancement.id("add_strafe_particles");

	public static void send(ServerPlayerEntity player, int id) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(id);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		int id = buf.readInt();
		client.execute(() -> {
			Entity entity = handler.getWorld().getEntityById(id);
			if (entity != null) {
				StrafeComponent.addStrafeParticles(entity);
			}
		});
	}
}
