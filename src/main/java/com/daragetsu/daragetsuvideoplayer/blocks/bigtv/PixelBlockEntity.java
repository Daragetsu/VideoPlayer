package com.daragetsu.daragetsuvideoplayer.blocks.bigtv;

import com.daragetsu.daragetsuvideoplayer.blocks.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PixelBlockEntity extends BlockEntity{

    private int color = 0;

    public PixelBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.PIXEL_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("color", this.color);
    }
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("color")){
            this.color = tag.getInt("color");
        }
    }
    public void setColor(int col){
        this.color = col;
        this.setChanged();
        if (!level.isClientSide()) {
            level.sendBlockUpdated(
                worldPosition,
                getBlockState(),
                getBlockState(),
                3
            );
        }
    }
    public int getColor(){
        return this.color;
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
}