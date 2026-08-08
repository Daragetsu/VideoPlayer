package com.daragetsu.daragetsuvideoplayer.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class DataLoader extends SimpleJsonResourceReloadListener{
    private static final Gson GSON = new GsonBuilder().create();

    public DataLoader() {
        super(GSON, "videos");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            JsonElement element = entry.getValue();
            Data data = GSON.fromJson(element, Data.class);
            File main = Minecraft.getInstance().gameDirectory;
            File folder = new File(main, "frames");
            File f = new File(folder, data.name());
            if(!f.exists()){
                File file = new File(System.getProperty("user.home"), data.location());
                f.mkdirs();
                if(file.exists() && file.isFile()){
                    try {
                        ProcessBuilder builder = new ProcessBuilder(
                            "ffmpeg",
                            "-i", file.getAbsolutePath(),
                            "-vsync", "0",
                            f.getAbsolutePath() + "/%01d_" + file.getName() + ".png"
                        );
                        builder.redirectErrorStream(true);
                        Process p = builder.start();
                        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String line;
                        while (true) {
                            line = r.readLine();
                            if (line == null) { break; }
                            System.out.println(line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            ArrayList<String> ff = new ArrayList<>();
            Map<Integer, String> map = new HashMap<>();
            for(String fileName : f.list()){
                map.put(Integer.parseInt(fileName.substring(0, fileName.indexOf('_', 0))), fileName);
            }
            SortedSet<Integer> keys = new TreeSet<>(map.keySet());
            for(int i : keys){
                ff.add(map.get(i));
            }
            Global.files.put(data.name(), ff);
        }
    }
}