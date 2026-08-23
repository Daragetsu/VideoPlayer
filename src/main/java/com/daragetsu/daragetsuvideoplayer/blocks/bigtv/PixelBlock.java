package com.daragetsu.daragetsuvideoplayer.blocks.bigtv;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PixelBlock extends BaseEntityBlock {

    public PixelBlock(Properties p_49224_) {
        super(p_49224_);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new PixelBlockEntity(p_153215_, p_153216_);
    }
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.INVISIBLE;
    }
}
