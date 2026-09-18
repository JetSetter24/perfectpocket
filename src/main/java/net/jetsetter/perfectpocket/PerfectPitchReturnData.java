package net.jetsetter.perfectpocket;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PerfectPitchReturnData extends PersistentState {

    private static final String SAVE_ID =
            "perfectpocket_return_locations";

    private final Map<UUID, ReturnLocation> returnLocations =
            new HashMap<>();

    public static final Type<PerfectPitchReturnData> TYPE =
            new Type<>(
                    PerfectPitchReturnData::new,
                    PerfectPitchReturnData::createFromNbt,
                    null
            );

    /**
     * Gets the persistent data for the current server.
     *
     * The data is stored in the Overworld's save data,
     * so it is available regardless of which dimension
     * the player is currently in.
     */
    public static PerfectPitchReturnData get(
            MinecraftServer server
    ) {

        ServerWorld overworld =
                server.getOverworld();

        return overworld
                .getPersistentStateManager()
                .getOrCreate(
                        TYPE,
                        SAVE_ID
                );
    }

    /**
     * Gets a player's saved Pocket location.
     */
    public ReturnLocation getLocation(
            UUID uuid
    ) {
        return returnLocations.get(uuid);
    }

    /**
     * Saves or updates a player's Pocket location.
     */
    public void setLocation(
            UUID uuid,
            ReturnLocation location
    ) {

        returnLocations.put(
                uuid,
                location
        );

        // Tell Minecraft that this PersistentState
        // needs to be written to disk.
        markDirty();
    }

    /**
     * Loads the data from the world save.
     */
    private static PerfectPitchReturnData createFromNbt(
            NbtCompound nbt,
            RegistryWrapper.WrapperLookup registries
    ) {

        PerfectPitchReturnData data =
                new PerfectPitchReturnData();

        if (!nbt.contains("players")) {
            return data;
        }

        NbtCompound players =
                nbt.getCompound("players");

        for (String uuidString :
                players.getKeys()) {

            try {

                UUID uuid =
                        UUID.fromString(uuidString);

                NbtCompound player =
                        players.getCompound(
                                uuidString
                        );

                Identifier worldId =
                        Identifier.of(
                                player.getString(
                                        "world"
                                )
                        );

                RegistryKey<World> world =
                        RegistryKey.of(
                                RegistryKeys.WORLD,
                                worldId
                        );

                ReturnLocation location =
                        new ReturnLocation(

                                world,

                                player.getDouble("x"),
                                player.getDouble("y"),
                                player.getDouble("z"),

                                player.getFloat("yaw"),
                                player.getFloat("pitch"),

                                player.getDouble(
                                        "perfectPitchX"
                                ),
                                player.getDouble(
                                        "perfectPitchY"
                                ),
                                player.getDouble(
                                        "perfectPitchZ"
                                ),

                                player.getFloat(
                                        "perfectPitchYaw"
                                ),
                                player.getFloat(
                                        "perfectPitchPitch"
                                )
                        );

                data.returnLocations.put(
                        uuid,
                        location
                );

            } catch (Exception ignored) {

                // Ignore corrupted/invalid entries
                // rather than preventing the world
                // from loading.
            }
        }

        return data;
    }

    /**
     * Writes the data to the world save.
     */
    @Override
    public NbtCompound writeNbt(
            NbtCompound nbt,
            RegistryWrapper.WrapperLookup registries
    ) {

        NbtCompound players =
                new NbtCompound();

        for (Map.Entry<UUID, ReturnLocation> entry :
                returnLocations.entrySet()) {

            UUID uuid =
                    entry.getKey();

            ReturnLocation location =
                    entry.getValue();

            NbtCompound player =
                    new NbtCompound();

            player.putString(
                    "world",
                    location.world()
                            .getValue()
                            .toString()
            );

            player.putDouble(
                    "x",
                    location.x()
            );

            player.putDouble(
                    "y",
                    location.y()
            );

            player.putDouble(
                    "z",
                    location.z()
            );

            player.putFloat(
                    "yaw",
                    location.yaw()
            );

            player.putFloat(
                    "pitch",
                    location.pitch()
            );

            player.putDouble(
                    "perfectPitchX",
                    location.perfectPitchX()
            );

            player.putDouble(
                    "perfectPitchY",
                    location.perfectPitchY()
            );

            player.putDouble(
                    "perfectPitchZ",
                    location.perfectPitchZ()
            );

            player.putFloat(
                    "perfectPitchYaw",
                    location.perfectPitchYaw()
            );

            player.putFloat(
                    "perfectPitchPitch",
                    location.perfectPitchPitch()
            );

            players.put(
                    uuid.toString(),
                    player
            );
        }

        nbt.put(
                "players",
                players
        );

        return nbt;
    }

    public record ReturnLocation(

            RegistryKey<World> world,

            double x,
            double y,
            double z,

            float yaw,
            float pitch,

            double perfectPitchX,
            double perfectPitchY,
            double perfectPitchZ,

            float perfectPitchYaw,
            float perfectPitchPitch

    ) {
    }
}