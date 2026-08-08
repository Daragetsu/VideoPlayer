package com.daragetsu.daragetsuvideoplayer.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class VideoPlayerBlock extends BaseEntityBlock{

    public VideoPlayerBlock(Properties p_49795_) {
        super(p_49795_);
    }
    
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
        BlockEntityType<A> type, BlockEntityType<E> checkedType, BlockEntityTicker<? super E> ticker
    ) {
        return checkedType == type ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.VIDEO_PLAYER_BLOCK_ENTITY.get(), VideoPlayerBlockEntity::tick);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new VideoPlayerBlockEntity(p_153215_, p_153216_);
    }
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult result) {
        if(!level.isClientSide)return InteractionResult.SUCCESS;
        VideoPlayerBlockEntity blockEntity = (VideoPlayerBlockEntity)level.getBlockEntity(pos);
        if(!blockEntity.runnables.isEmpty()){
            if(blockEntity.running>=blockEntity.runnables.size()-1){
                blockEntity.running = 0;
            }else{
                blockEntity.running++;
            }
        }
        player.sendSystemMessage(Component.literal("Switched to: "+blockEntity.runnables.get(blockEntity.running)));
        return InteractionResult.SUCCESS;
    }
}