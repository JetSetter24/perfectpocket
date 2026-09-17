package net.jetsetter.perfectpocket;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerfectPocket implements ModInitializer {
	public static final String MOD_ID = "perfectpocket";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
