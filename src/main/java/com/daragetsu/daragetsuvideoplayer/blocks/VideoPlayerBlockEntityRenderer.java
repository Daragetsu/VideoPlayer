package com.daragetsu.daragetsuvideoplayer.blocks;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;

import com.daragetsu.daragetsuvideoplayer.data.Global;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import java.awt.Color;
import java.io.File;
public class VideoPlayerBlockEntityRenderer implements BlockEntityRenderer<VideoPlayerBlockEntity>{

    public VideoPlayerBlockEntityRenderer(BlockEntityRendererProvider.Context context){
    }

    @Override
    public void render(VideoPlayerBlockEntity block, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if(block.runnables.isEmpty()){
            for(String key : Global.files.keySet()){
                block.runnables.add(key);
            }
        }
        if(block.runnables.isEmpty())return;
        if(Global.files.get(block.runnables.get(block.running))==null)return;
        if(block.frames>=Global.files.get(block.runnables.get(block.running)).size()-1){
            block.frames = 0;
        }
        block.frames++;
        VertexConsumer vc = bufferSource.getBuffer(RenderType.debugQuads());
        stack.pushPose();
        stack.translate(0D, 1D, 0.5D);
        stack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));
        Matrix4f m = stack.last().pose();
        try {
            File main = Minecraft.getInstance().gameDirectory;
            File folf = new File(main, "frames");
            File folder = new File(folf, block.runnables.get(block.running));
            String name = Global.files.get(block.runnables.get(block.running)).get(block.frames);
            File file = new File(folder, name);
            block.image = ImageIO.read(file);
            block.imageWidth = block.image.getWidth();
            block.imageHeight = block.image.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(block.image==null)return;
        for (int x = 0; x < block.imageWidth; x++) {
            for (int y = 0; y < block.imageHeight; y++) {
                Color color = new Color(block.image.getRGB(x, y), true);
                float px = x / (float) block.imageWidth;
                float py = y / (float) block.imageHeight;
                float width = 1.0f / block.imageWidth;
                float height = 1.0f / block.imageHeight;
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