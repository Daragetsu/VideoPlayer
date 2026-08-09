package com.daragetsu.daragetsuvideoplayer;

import com.daragetsu.daragetsuvideoplayer.blocks.ModBlockEntities;
import com.daragetsu.daragetsuvideoplayer.blocks.ModBlocks;
import com.daragetsu.daragetsuvideoplayer.blocks.VideoPlayerBlockEntityRenderer;
import com.daragetsu.daragetsuvideoplayer.data.DataLoader;
import com.daragetsu.daragetsuvideoplayer.items.ModItems;
import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

@Mod(DaragetsuVideoPlayer.MOD_ID)
public class DaragetsuVideoPlayer
{
    public static final String MOD_ID = "daragetsuvideoplayer";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DaragetsuVideoPlayer(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey().equals(CreativeModeTabs.REDSTONE_BLOCKS)){
            event.accept(ModItems.VIDEO_PLAYER_BLOCK);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        DataLoader.Load(event.getServer().getWorldPath(LevelResource.ROOT).toFile());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(
                ModBlockEntities.VIDEO_PLAYER_BLOCK_ENTITY.get(),
                VideoPlayerBlockEntityRenderer::new
            );
        }
    }
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class Handler {
        @SubscribeEvent
        public static void addListeners(AddReloadListenerEvent event) {
            event.addListener(new DataLoader());
        }
    }
}
