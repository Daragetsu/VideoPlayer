package com.daragetsu.daragetsuvideoplayer.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.daragetsu.daragetsuvideoplayer.DaragetsuVideoPlayer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class DataLoader extends SimpleJsonResourceReloadListener{
    private static final Gson GSON = new GsonBuilder().create();
    public static Map<String, ArrayList<String>> files = new HashMap<>();
    private static Map<ResourceLocation, JsonElement> object = new HashMap<>();

    public DataLoader() {
        super(GSON, "videos");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        DataLoader.object = object;
    }

    public static void Load(File main){
        for (Map.Entry<ResourceLocation, JsonElement> entry : DataLoader.object.entrySet()) {
            JsonElement element = entry.getValue();
            Data data = GSON.fromJson(element, Data.class);
            File folder = new File(main, "frames");
            File f = new File(folder, data.name());
            File out = null;
            if(!f.exists()){
                File file = new File(System.getProperty("user.home"), data.location());
                f.mkdirs();
                if(file.exists() && file.isFile()){
                    try {
                        out = changeVideoFramerate(file, f);
                        ProcessBuilder builder = new ProcessBuilder(
                            "ffmpeg",
                            "-i", out.getAbsolutePath(),
                            "-vsync", "0",
                            "-vf", "scale=-1:260",
                            f.getAbsolutePath() + "/%01d_" + out.getName() + ".png"
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
                    if(
                        getMimeType(out).equalsIgnoreCase("mp4") ||
                        getMimeType(out).equalsIgnoreCase("mov") ||
                        getMimeType(out).equalsIgnoreCase("webm") ||
                        getMimeType(out).equalsIgnoreCase("avi") ||
                        getMimeType(out).equalsIgnoreCase("qt") ||
                        getMimeType(out).equalsIgnoreCase("mpeg") ||
                        getMimeType(out).equalsIgnoreCase("mpg") ||
                        getMimeType(out).equalsIgnoreCase("wmv") ||
                        getMimeType(out).equalsIgnoreCase("flv") ||
                        getMimeType(out).equalsIgnoreCase("mkv") ||
                        getMimeType(out).equalsIgnoreCase("3gp") ||
                        getMimeType(out).equalsIgnoreCase("3g2")
                    ){
                        try {
                            ProcessBuilder builder = new ProcessBuilder(
                                "ffmpeg",
                                "-i",
                                out.getAbsolutePath(),
                                "-q:a",
                                "0",
                                "-map",
                                "0:a",
                                f.getAbsolutePath() + "\\" + data.name() + ".mp3"
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
                    if(out!=null){
                        out.delete();
                    }
                }
            }
            ArrayList<String> ff = new ArrayList<>();
            Map<Integer, String> map = new HashMap<>();
            for(String fileName : f.list()){
                if(fileName.contains(".mp3"))continue;
                map.put(Integer.parseInt(fileName.substring(0, fileName.indexOf('_', 0))), fileName);
            }
            SortedSet<Integer> keys = new TreeSet<>(map.keySet());
            for(int i : keys){
                ff.add(map.get(i));
            }
            DataLoader.files.put(data.name(), ff);
        }
    }
    private static String getMimeType(File file) {
        return file.getName().substring(file.getName().lastIndexOf(".")+1, file.getName().length());
    }
    private static File changeVideoFramerate(File file, File folder){
        File output = new File(folder, file.getName()+"_out.mp4");
        try {
            ProcessBuilder builder = new ProcessBuilder(
                "ffmpeg",
                "-i",
                file.getAbsolutePath(),
                "-filter:v",
                "fps=20",
                output.getAbsolutePath()
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
        return output;
    }
}