/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ModMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.contains("integration.architectury")) {
			return FabricLoader.getInstance().isModLoaded("architectury");
		} else if (mixinClassName.contains("integration.geckolib")) {
			return FabricLoader.getInstance().isModLoaded("geckolib");
		} else if (mixinClassName.contains("integration.impaled")) {
			return FabricLoader.getInstance().isModLoaded("impaled");
		} else if (mixinClassName.contains("integration.sodium")) {
			return FabricLoader.getInstance().isModLoaded("sodium");
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
