package com.daragetsu.daragetsuvideoplayer.blocks.bigtv;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.daragetsu.daragetsuvideoplayer.blocks.ModBlockEntities;
import com.daragetsu.daragetsuvideoplayer.blocks.ModBlocks;
import com.daragetsu.daragetsuvideoplayer.data.DataLoader;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;

public class BigScreenBlockEntity extends BlockEntity{
    public int imageWidth = 0;
    public int imageHeight = 0;
    public BufferedImage image;
    public int frames = 0;
    public int running = 0;
    public ArrayList<String> runnables = new ArrayList<>();
    public volatile boolean playingSound = false;
    public Thread thr = new Thread();
    public volatile Process process;
    public BigScreenBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.BIG_SCREEN_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, BigScreenBlockEntity blockEntity) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        AABB box = new AABB(
            pos.getX(),
            pos.getY()+1,
            pos.getZ(),
            pos.getX()+blockEntity.imageWidth,
            pos.getY()+1,
            pos.getZ()+blockEntity.imageHeight
        );
        int imgX = 0;
        int imgZ = 0;
        for(double x = box.minX; x < box.maxX; x++){
            for(double z = box.minZ; z < box.maxZ; z++){
                BlockPos pixelPos = new BlockPos((int)x,pos.getY()+1,(int)z);
                level.setBlock(new BlockPos(pixelPos), ModBlocks.PIXEL_BLOCK.get().defaultBlockState(), 3);
                ((PixelBlockEntity)level.getBlockEntity(pixelPos)).setColor(blockEntity.image.getRGB(imgX, imgZ));
                imgZ++;
            }
            imgX++;
            imgZ = 0;
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
    }
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.imageWidth = tag.getInt("imageWidth");
        this.imageHeight = tag.getInt("imageHeight");
        this.frames = tag.getInt("frames");
        this.running = tag.getInt("running");
        this.playingSound = tag.getBoolean("playingSound");
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
