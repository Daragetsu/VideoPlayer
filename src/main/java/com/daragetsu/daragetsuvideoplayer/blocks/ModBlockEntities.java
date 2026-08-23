package com.daragetsu.daragetsuvideoplayer.blocks;

import com.daragetsu.daragetsuvideoplayer.DaragetsuVideoPlayer;
import com.daragetsu.daragetsuvideoplayer.blocks.bigtv.BigScreenBlockEntity;
import com.daragetsu.daragetsuvideoplayer.blocks.bigtv.PixelBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DaragetsuVideoPlayer.MOD_ID);
    
    public static final RegistryObject<BlockEntityType<BigScreenBlockEntity>> BIG_SCREEN_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("big_screen_block_entity", ()->BlockEntityType.Builder.of(BigScreenBlockEntity::new, ModBlocks.BIG_SCREEN_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<PixelBlockEntity>> PIXEL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("pixel_block_entity", ()->BlockEntityType.Builder.of(PixelBlockEntity::new, ModBlocks.PIXEL_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
