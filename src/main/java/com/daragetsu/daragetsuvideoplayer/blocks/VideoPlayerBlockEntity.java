package com.daragetsu.daragetsuvideoplayer.blocks;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.daragetsu.daragetsuvideoplayer.data.DataLoader;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelResource;

public class VideoPlayerBlockEntity extends BlockEntity{
    public int imageWidth = 0;
    public int imageHeight = 0;
    public BufferedImage image;
    public int frames = 0;
    public int running = 0;
    public ArrayList<String> runnables = new ArrayList<>();
    public int[][] pixels = new int[64][64];
    public volatile boolean playingSound = false;
    public Thread thr = new Thread();
    public volatile Process process;
    public VideoPlayerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.VIDEO_PLAYER_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, VideoPlayerBlockEntity blockEntity) {
        if(level.isClientSide())return;
        if(DataLoader.files.isEmpty())return;
        if(blockEntity.runnables.isEmpty()){
            for(String key : DataLoader.files.keySet()){
                blockEntity.runnables.add(key);
            }
        }
        if(blockEntity.runnables.isEmpty())return;
        File main = level.getServer().getWorldPath(LevelResource.ROOT).toFile();
        File folf = new File(main, "frames");
        if(DataLoader.files.get(blockEntity.runnables.get(blockEntity.running))==null)return;
        File fol = new File(folf, blockEntity.runnables.get(blockEntity.running));
        File soundFIle = new File(fol, blockEntity.runnables.get(blockEntity.running)+".mp3");
        if(!soundFIle.exists()){
            blockEntity.playingSound = true;
        }
        if(!blockEntity.playingSound){
            blockEntity.thr = new Thread(()->{
                try {
                    ProcessBuilder builder = new ProcessBuilder(
                        "ffplay",
                        "-nodisp",
                        "-autoexit",
                        "-fflags",
                        "nobuffer",
                        "-flags",
                        "low_delay",
                        "-analyzeduration",
                        "0",
                        "-probesize",
                        "32",
                        soundFIle.getAbsolutePath()
                    );
                    builder.redirectErrorStream(true);
                    blockEntity.process = builder.start();
                    BufferedReader r = new BufferedReader(new InputStreamReader(blockEntity.process.getInputStream()));
                    String line;
                    while (true) {
                        line = r.readLine();
                        if (line == null) { break; }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            blockEntity.thr.start();
            blockEntity.playingSound = true;
        }
        if(!blockEntity.playingSound)return;
        if(blockEntity.frames>=DataLoader.files.get(blockEntity.runnables.get(blockEntity.running)).size()-1){
            blockEntity.frames = 0;
        }
        blockEntity.frames++;
        try {
            File folder = new File(folf, blockEntity.runnables.get(blockEntity.running));
            String name = DataLoader.files.get(blockEntity.runnables.get(blockEntity.running)).get(blockEntity.frames);
            File file = new File(folder, name);
            blockEntity.image = ImageIO.read(file);
            blockEntity.imageWidth = blockEntity.image.getWidth();
            blockEntity.imageHeight = blockEntity.image.getHeight();
            blockEntity.pixels = new int[blockEntity.image.getWidth()][blockEntity.image.getHeight()];
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int x = 0; x < blockEntity.imageWidth; x++){
            for(int y = 0; y < blockEntity.imageHeight; y++){
                blockEntity.pixels[x][y] = blockEntity.image.getRGB(x, y);
            }
        }
        blockEntity.setChanged();
        level.sendBlockUpdated(pos, blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("imageWidth", this.imageWidth);
        tag.putInt("imageHeight", this.imageHeight);
        tag.putInt("frames", this.frames);
        tag.putInt("running", this.running);
        tag.putBoolean("playingSound", this.playingSound);
        for(int x = 0; x < this.imageWidth; x++){
            for(int y = 0; y < this.imageHeight; y++){
                tag.putInt(x+"-"+y, this.image.getRGB(x, y));
            }
        }
    }
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.imageWidth = tag.getInt("imageWidth");
        this.imageHeight = tag.getInt("imageHeight");
        this.frames = tag.getInt("frames");
        this.running = tag.getInt("running");
        this.playingSound = tag.getBoolean("playingSound");
        this.pixels = new int[this.imageWidth][this.imageHeight];
        for(int x = 0; x < this.imageWidth; x++){
            for(int y = 0; y < this.imageHeight; y++){
                this.pixels[x][y] = tag.getInt(x+"-"+y);
            }
        }
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void setRemoved() {
        if(!this.level.isClientSide()){
            this.thr.interrupt();
            this.process.destroy();
        }
        super.setRemoved();
    }
}