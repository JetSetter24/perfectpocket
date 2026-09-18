package net.jetsetter.perfectpocket.item;

import net.jetsetter.perfectpocket.PerfectPitchReturnData;
import net.jetsetter.perfectpocket.PerfectPocket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PocketItem extends Item {

    public PocketItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity player,
            Hand hand
    ) {

        if (!world.isClient && player.getServer() != null) {

            ServerPlayerEntity serverPlayer =
                    (ServerPlayerEntity) player;

            RegistryKey<World> perfectPitchKey =
                    RegistryKey.of(
                            RegistryKeys.WORLD,
                            PerfectPocket.id("perfect_pitch")
                    );

            // Always store the data in the Overworld.
            // This allows the data to be accessed from any dimension.
            ServerWorld overworld =
                    serverPlayer.getServer().getOverworld();

            PerfectPitchReturnData data =
                    PerfectPitchReturnData.get(
                            serverPlayer.getServer()
                    );

            // =====================================================
            // USING POCKET INSIDE PERFECT PITCH
            // =====================================================

            if (world.getRegistryKey().equals(perfectPitchKey)) {

                PerfectPitchReturnData.ReturnLocation returnLocation =
                        data.getLocation(serverPlayer.getUuid());

                if (returnLocation != null) {

                    ServerWorld returnWorld =
                            serverPlayer.getServer()
                                    .getWorld(returnLocation.world());

                    if (returnWorld != null) {

                        // Save the player's CURRENT Perfect Pitch
                        // position before leaving.
                        data.setLocation(
                                serverPlayer.getUuid(),
                                new PerfectPitchReturnData.ReturnLocation(

                                        // Keep the normal-world location
                                        returnLocation.world(),
                                        returnLocation.x(),
                                        returnLocation.y(),
                                        returnLocation.z(),
                                        returnLocation.yaw(),
                                        returnLocation.pitch(),

                                        // Save current Perfect Pitch position
                                        serverPlayer.getX(),
                                        serverPlayer.getY(),
                                        serverPlayer.getZ(),
                                        serverPlayer.getYaw(),
                                        serverPlayer.getPitch()
                                )
                        );

                        // Portal sound
                        serverPlayer.playSound(
                                SoundEvents.BLOCK_PORTAL_TRAVEL,
                                0.5f,
                                1.0f
                        );

                        // Return to the saved normal-world location
                        serverPlayer.teleport(
                                returnWorld,
                                returnLocation.x(),
                                returnLocation.y(),
                                returnLocation.z(),
                                returnLocation.yaw(),
                                returnLocation.pitch()
                        );
                    }
                }
            }

            // =====================================================
            // USING POCKET IN A NORMAL DIMENSION
            // =====================================================

            else {

                ServerWorld perfectPitch =
                        serverPlayer.getServer()
                                .getWorld(perfectPitchKey);

                if (perfectPitch != null) {

                    PerfectPitchReturnData.ReturnLocation oldLocation =
                            data.getLocation(serverPlayer.getUuid());

                    double perfectPitchX;
                    double perfectPitchY;
                    double perfectPitchZ;

                    float perfectPitchYaw;
                    float perfectPitchPitch;

                    // =================================================
                    // FIRST EVER ENTRY INTO PERFECT PITCH
                    // =================================================

                    if (oldLocation == null) {

                        // Start Perfect Pitch at the same X/Z
                        // coordinates as the player.
                        perfectPitchX =
                                serverPlayer.getX();

                        perfectPitchZ =
                                serverPlayer.getZ();

                        // Find the floor.
                        BlockPos.Mutable pos =
                                new BlockPos.Mutable(
                                        (int) Math.floor(perfectPitchX),
                                        perfectPitch.getTopY() - 1,
                                        (int) Math.floor(perfectPitchZ)
                                );

                        // Default position in case no block is found.
                        perfectPitchY = 2;

                        for (
                                int y = perfectPitch.getTopY() - 1;
                                y >= perfectPitch.getBottomY();
                                y--
                        ) {

                            pos.setY(y);

                            if (!perfectPitch
                                    .getBlockState(pos)
                                    .isAir()) {

                                perfectPitchY = y + 1;
                                break;
                            }
                        }

                        perfectPitchYaw =
                                serverPlayer.getYaw();

                        perfectPitchPitch =
                                serverPlayer.getPitch();
                    }

                    // =================================================
                    // RETURNING TO AN EXISTING PERFECT PITCH LOCATION
                    // =================================================

                    else {

                        // IMPORTANT:
                        // Use the previously saved Perfect Pitch
                        // coordinates instead of creating a new one.

                        perfectPitchX =
                                oldLocation.perfectPitchX();

                        perfectPitchY =
                                oldLocation.perfectPitchY();

                        perfectPitchZ =
                                oldLocation.perfectPitchZ();

                        perfectPitchYaw =
                                oldLocation.perfectPitchYaw();

                        perfectPitchPitch =
                                oldLocation.perfectPitchPitch();
                    }

                    // =================================================
                    // SAVE NORMAL-WORLD LOCATION
                    // =================================================

                    data.setLocation(
                            serverPlayer.getUuid(),
                            new PerfectPitchReturnData.ReturnLocation(

                                    // Dimension the player is currently in
                                    world.getRegistryKey(),

                                    // Current normal-world position
                                    serverPlayer.getX(),
                                    serverPlayer.getY(),
                                    serverPlayer.getZ(),
                                    serverPlayer.getYaw(),
                                    serverPlayer.getPitch(),

                                    // Perfect Pitch position
                                    perfectPitchX,
                                    perfectPitchY,
                                    perfectPitchZ,
                                    perfectPitchYaw,
                                    perfectPitchPitch
                            )
                    );

                    // Portal sound
                    serverPlayer.playSound(
                            SoundEvents.BLOCK_PORTAL_TRAVEL,
                            0.5f,
                            1.0f
                    );

                    // =================================================
                    // ENTER PERFECT PITCH
                    // =================================================

                    serverPlayer.teleport(
                            perfectPitch,
                            perfectPitchX,
                            perfectPitchY,
                            perfectPitchZ,
                            perfectPitchYaw,
                            perfectPitchPitch
                    );
                }
            }
        }

        return TypedActionResult.success(
                player.getStackInHand(hand)
        );
    }
}