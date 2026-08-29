package com.daragetsu.daragetsuvideoplayer.data;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class ColorDataLoader extends SimpleJsonResourceReloadListener{
    private static final Gson GSON = new GsonBuilder().create();

    public static ColorData colorDB = null;

    public ColorDataLoader() {
        super(GSON, "blocks");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager,
            ProfilerFiller profiler) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            JsonElement element = entry.getValue();
            ColorDataLoader.colorDB = GSON.fromJson(element, ColorData.class);
        }
    }
}
