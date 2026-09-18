package net.jetsetter.perfectpocket.block;

import net.jetsetter.perfectpocket.PerfectPocket;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {

    public static final Block PERFECT_PITCH_BLACK = registerBlock(
            "perfect_pitch_black",
            new Block(AbstractBlock.Settings.create()
                    .strength(50.0f)
                    .requiresTool())
    );

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, PerfectPocket.id(name), block);
    }

    public static void registerModBlocks() {
        PerfectPocket.LOGGER.info("Registering Mod Blocks for " + PerfectPocket.MOD_ID);
    }
}