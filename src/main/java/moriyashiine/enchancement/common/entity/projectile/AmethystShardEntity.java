/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.common.registry.ModDamageTypes;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.gen.Accessor;

public class AmethystShardEntity extends PersistentProjectileEntity {
	private static final ParticleEffect PARTICLE = new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.AMETHYST_SHARD));

	public AmethystShardEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	public AmethystShardEntity(World world, LivingEntity owner) {
		super(ModEntityTypes.AMETHYST_SHARD, owner, world);
	}

	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public void tick() {
		super.tick();
		if (!getWorld().isClient && age > 400) {
			playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
			addParticles();
			discard();
		}
	}

	@Override
	protected SoundEvent getHitSound() {
		return ModSoundEvents.ENTITY_SHARD_SHATTER;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if (!this.getWorld().isClient) {
			Entity entity = entityHitResult.getEntity();
			if (entity instanceof EnderDragonPart part) {
				entity = part.owner;
			}
			if (entity instanceof LivingEntity) {
				Entity owner = getOwner();
				if (EnchancementUtil.shouldHurt(owner, entity) && entity.damage(ModDamageTypes.create(getWorld(), ModDamageTypes.AMETHYST_SHARD, this, owner), (float) getDamage())) {
					if (isOnFire()) {
						entity.setOnFireFor(5);
					}
					playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
					addParticles();
					discard();
				}
			}
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		var our_world = this.getWorld();
		BlockState state = our_world.getBlockState(blockHitResult.getBlockPos());
		state.onProjectileHit(our_world, state, blockHitResult, this);
		if (!our_world.isClient) {
			playSound(getHitSound(), 1, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
			addParticles();
			discard();
		}
	}

	public void addParticles() {
		((ServerWorld) this.getWorld()).spawnParticles(PARTICLE, getX(), getY(), getZ(), 8, getWidth() / 2, getHeight() / 2, getWidth() / 2, 0);
	}
}
