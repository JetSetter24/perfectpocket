package net.jetsetter.perfectpocket;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class PerfectPocketClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        RegistryKey<World> perfectPitchKey = RegistryKey.of(
                RegistryKeys.WORLD,
                Identifier.of(PerfectPocket.MOD_ID, "perfect_pitch")
        );

        // Remove the sun, moon, stars and normal sky rendering
        DimensionRenderingRegistry.registerSkyRenderer(
                perfectPitchKey,
                context -> {
                }
        );

        // Remove clouds
        DimensionRenderingRegistry.registerCloudRenderer(
                perfectPitchKey,
                context -> {
                }
        );
    }
}