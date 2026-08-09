package com.daragetsu.daragetsuvideoplayer.blocks;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

import java.awt.Color;
public class VideoPlayerBlockEntityRenderer implements BlockEntityRenderer<VideoPlayerBlockEntity>{

    public VideoPlayerBlockEntityRenderer(BlockEntityRendererProvider.Context context){
    }

    @Override
    public void render(VideoPlayerBlockEntity block, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        VertexConsumer vc = bufferSource.getBuffer(RenderType.debugQuads());
        stack.pushPose();
        Direction facing = block.getBlockState().getValue(VideoPlayerBlock.FACING);
        if(facing.equals(Direction.NORTH)){
            stack.translate(1D, 1D, 0D);
            stack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));
            stack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0F));
        }
        if(facing.equals(Direction.SOUTH)){
            stack.translate(0D, 1D, 0D);
            stack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-180.0F));
        }
        if(facing.equals(Direction.EAST)){
            stack.translate(0D, 1D, 1D);
            stack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));
            stack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(270.0F));
        }
        if(facing.equals(Direction.WEST)){
            stack.translate(0D, 1D, 0D);
            stack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));
            stack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90.0F));
        }
        if(facing.equals(Direction.UP)){
            stack.translate(0D, 1D, 0D);
            stack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
        }
        Matrix4f m = stack.last().pose();
        for (int x = 0; x < block.imageWidth; x++) {
            for (int y = 0; y < block.imageHeight; y++) {
                Color color = new Color(block.pixels[x][y], true);
                float px = (float) (x / (float) block.imageWidth);
                float py = (float) (y / (float) block.imageHeight);
                float width = (float) (1.0f / block.imageWidth);
                float height = (float) (1.0f / block.imageHeight);
                vc.vertex(m, px, py, 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();
                vc.vertex(m, px + width, py, 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();
                vc.vertex(m, px + width, py + height, 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();
                vc.vertex(m, px, py + height, 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();
            }
        }
        stack.popPose();
    }
}