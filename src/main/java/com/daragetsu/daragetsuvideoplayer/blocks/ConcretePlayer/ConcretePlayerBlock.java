package com.daragetsu.daragetsuvideoplayer.blocks.ConcretePlayer;

import org.checkerframework.checker.units.qual.A;

import com.daragetsu.daragetsuvideoplayer.blocks.ModBlockEntities;
import com.daragetsu.daragetsuvideoplayer.data.DataLoader;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ConcretePlayerBlock extends BaseEntityBlock{
    public ConcretePlayerBlock(Properties p_49795_) {
        super(p_49795_);
    }
    
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
        BlockEntityType<A> type, BlockEntityType<E> checkedType, BlockEntityTicker<? super E> ticker
    ) {
        return checkedType == type ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BLOCK_TV_BLOCK_ENTITY.get(), ConcretePlayerBlockEntity::tick);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ConcretePlayerBlockEntity(p_153215_, p_153216_);
    }
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult result) {
        if(level.isClientSide)return InteractionResult.SUCCESS;
        if(DataLoader.files.isEmpty())return InteractionResult.PASS;
        ConcretePlayerBlockEntity blockEntity = (ConcretePlayerBlockEntity)level.getBlockEntity(pos);
        ItemStack stack = player.getMainHandItem();
        String toChangeTo = "";
        if(stack.is(Items.STICK)){
            if(stack.hasCustomHoverName()){
                toChangeTo = stack.getHoverName().getString();
            }
        }
        if(!blockEntity.runnables.isEmpty()){
            if(toChangeTo.isEmpty() && !blockEntity.runnables.contains(toChangeTo)){
                if(blockEntity.running>=blockEntity.runnables.size()-1){
                    blockEntity.running = 0;
                }else{
                    blockEntity.running++;
                }
                blockEntity.frames = 0;
                blockEntity.playingSound = false;
                if(blockEntity.thr.isAlive()){
                    blockEntity.thr.interrupt();
                    if(blockEntity.process.isAlive()){
                        blockEntity.process.destroy();
                    }
                }
            }else{
                blockEntity.running = blockEntity.runnables.indexOf(toChangeTo);
                blockEntity.frames = 0;
                blockEntity.playingSound = false;
                if(blockEntity.thr.isAlive()){
                    blockEntity.thr.interrupt();
                    if(blockEntity.process.isAlive()){
                        blockEntity.process.destroy();
                    }
                }
            }
        }
        player.sendSystemMessage(Component.literal("Switched to: "+blockEntity.runnables.get(blockEntity.running)));
        return InteractionResult.SUCCESS;
    }
}
