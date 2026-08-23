package com.daragetsu.daragetsuvideoplayer.items;

import com.daragetsu.daragetsuvideoplayer.DaragetsuVideoPlayer;
import com.daragetsu.daragetsuvideoplayer.blocks.ModBlocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, DaragetsuVideoPlayer.MOD_ID);

    public static final RegistryObject<Item> VIDEO_PLAYER_BLOCK = ITEMS.register("video_player_block", ()->new BlockItem(ModBlocks.VIDEO_PLAYER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> BIG_SCREEN_BLOCK = ITEMS.register("big_screen_block", ()->new BlockItem(ModBlocks.BIG_SCREEN_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> PIXEL_BLOCK = ITEMS.register("pixel_block", ()->new BlockItem(ModBlocks.PIXEL_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
