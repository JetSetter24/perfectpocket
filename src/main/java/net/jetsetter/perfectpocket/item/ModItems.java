package net.jetsetter.perfectpocket.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jetsetter.perfectpocket.PerfectPocket;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.jetsetter.perfectpocket.block.ModBlocks;
import net.minecraft.item.BlockItem;

public class ModItems {

    public static final Item POCKET = registerItem("pocket", new PocketItem(new Item.Settings()));

    public static final Item PERFECT_PITCH_BLACK = registerItem(
            "perfect_pitch_black",
            new BlockItem(ModBlocks.PERFECT_PITCH_BLACK, new Item.Settings())
    );

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, PerfectPocket.id(name), item);
    }

    public static void registerModItems() {
        PerfectPocket.LOGGER.info("Registering Mod Items for " + PerfectPocket.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(POCKET);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(PERFECT_PITCH_BLACK);
        });
    }
}