package net.jetsetter.perfectpocket;

import net.fabricmc.api.ModInitializer;
import net.jetsetter.perfectpocket.item.ModItems;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.jetsetter.perfectpocket.block.ModBlocks;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.world.GameRules;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class PerfectPocket implements ModInitializer {
	public static final String MOD_ID = "perfectpocket";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();

        ServerWorldEvents.LOAD.register((server, world) -> {
            RegistryKey<World> perfectPitchKey = RegistryKey.of(
                    RegistryKeys.WORLD,
                    PerfectPocket.id("perfect_pitch")
            );

            if (world.getRegistryKey().equals(perfectPitchKey)) {
                world.getGameRules()
                        .get(GameRules.DO_DAYLIGHT_CYCLE)
                        .set(false, server);
                        world.setTimeOfDay(6000);
            }
        });
    }

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
