package com.daragetsu.daragetsuvideoplayer.blocks;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class VideoPlayerBlockEntity extends BlockEntity{
    public int imageWidth = 1;
    public int imageHeight = 1;
    public BufferedImage image;
    public int frames = 0;
    public int running = 0;
    public ArrayList<String> runnables = new ArrayList<>();
    public VideoPlayerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.VIDEO_PLAYER_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, VideoPlayerBlockEntity blockEntity) {
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("imageWidth", this.imageWidth);
        tag.putInt("imageHeight", this.imageHeight);
        tag.putInt("frames", this.frames);
        tag.putInt("running", running);
    }
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.imageWidth = tag.getInt("imageWidth");
        this.imageHeight = tag.getInt("imageHeight");
        this.frames = tag.getInt("frames");
        this.running = tag.getInt("running");

    }
}