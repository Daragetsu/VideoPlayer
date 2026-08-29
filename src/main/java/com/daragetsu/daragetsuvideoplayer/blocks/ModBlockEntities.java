package com.daragetsu.daragetsuvideoplayer.blocks;

import com.daragetsu.daragetsuvideoplayer.DaragetsuVideoPlayer;
import com.daragetsu.daragetsuvideoplayer.blocks.ConcretePlayer.ConcretePlayerBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DaragetsuVideoPlayer.MOD_ID);
    
    public static final RegistryObject<BlockEntityType<VideoPlayerBlockEntity>> VIDEO_PLAYER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("video_player_block_entity", ()->BlockEntityType.Builder.of(VideoPlayerBlockEntity::new, ModBlocks.VIDEO_PLAYER_BLOCK.get()).build(null));
    
    public static final RegistryObject<BlockEntityType<ConcretePlayerBlockEntity>> BLOCK_TV_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("block_tv_block_entity", ()->BlockEntityType.Builder.of(ConcretePlayerBlockEntity::new, ModBlocks.BLOCK_TV_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
