package com.daragetsu.daragetsuvideoplayer.blocks;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.daragetsu.daragetsuvideoplayer.data.DataLoader;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateDefinition.Builder;

public class VideoPlayerBlock extends BaseEntityBlock{
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    Map<Direction, VoxelShape> landscapeShape = new HashMap<>(Map.of(
        Direction.NORTH, Block.box(
                0, 
                0, 
                8, 
                16, 
                16, 
                16
            ),
        Direction.SOUTH, Block.box(
                0, 
                0, 
                0, 
                16, 
                16, 
                8
            ),
        Direction.EAST, Block.box(
                0, 
                0, 
                0, 
                8, 
                16, 
                16
            ),
        Direction.WEST, Block.box(
                8, 
                0, 
                0, 
                16, 
                16, 
                16
            ),
        Direction.UP, Block.box(
                0, 
                0, 
                0, 
                16, 
                8, 
                16
            ),
        Direction.DOWN, Block.box(
                0, 
                8, 
                0, 
                16, 
                16, 
                16
            )
    ));
    Map<Direction, VoxelShape> portraitShape = new HashMap<>(Map.of(
        Direction.NORTH, Block.box(
                3.5, 
                0, 
                8, 
                12.5, 
                16, 
                16
            ),
        Direction.SOUTH, Block.box(
                3.5, 
                0, 
                0, 
                12.5, 
                16, 
                8
            ),
        Direction.EAST, Block.box(
                0, 
                0, 
                3.5, 
                8, 
                16, 
                12.5
            ),
        Direction.WEST, Block.box(
                8, 
                0, 
                3.5, 
                16, 
                16, 
                12.5
            ),
        Direction.UP, Block.box(
                3.5, 
                0, 
                0, 
                12.5, 
                8, 
                16
            ),
        Direction.DOWN, Block.box(
                3.5, 
                8, 
                0, 
                12.5, 
                16, 
                16
            )
    ));
    public static final BooleanProperty IS_WIDE = BooleanProperty.create("is_wide");

    public VideoPlayerBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(IS_WIDE, true));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(IS_WIDE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(IS_WIDE, true);
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
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos,
            CollisionContext context) {
        Direction facing = state.getValue(VideoPlayerBlock.FACING);
        VoxelShape shape = landscapeShape.get(facing);
        if(!state.getValue(VideoPlayerBlock.IS_WIDE)){
            shape = portraitShape.get(facing);
        }
        return shape;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult result) {
        if(level.isClientSide)return InteractionResult.SUCCESS;
        if(DataLoader.files.isEmpty())return InteractionResult.PASS;
        VideoPlayerBlockEntity blockEntity = (VideoPlayerBlockEntity)level.getBlockEntity(pos);
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