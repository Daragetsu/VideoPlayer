package com.daragetsu.daragetsuvideoplayer.blocks;

import com.daragetsu.daragetsuvideoplayer.DaragetsuVideoPlayer;
import com.daragetsu.daragetsuvideoplayer.blocks.bigtv.BigScreenBlock;
import com.daragetsu.daragetsuvideoplayer.blocks.bigtv.PixelBlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, DaragetsuVideoPlayer.MOD_ID);

    public static final RegistryObject<VideoPlayerBlock> VIDEO_PLAYER_BLOCK = BLOCKS.register("video_player_block", ()->new VideoPlayerBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE).noOcclusion()));
    public static final RegistryObject<BigScreenBlock> BIG_SCREEN_BLOCK = BLOCKS.register("big_screen_block", ()->new BigScreenBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE).noOcclusion()));
    public static final RegistryObject<PixelBlock> PIXEL_BLOCK = BLOCKS.register("pixel_block", ()->new PixelBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE).noOcclusion()));

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
