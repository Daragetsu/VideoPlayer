package com.daragetsu.daragetsuvideoplayer.blocks.bigtv;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.awt.Color;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class PixelBlockEntityRenderer implements BlockEntityRenderer<PixelBlockEntity>{

    public PixelBlockEntityRenderer(BlockEntityRendererProvider.Context context){
    }

    @Override
    public void render(PixelBlockEntity en, float p_112308_, PoseStack stack, MultiBufferSource bufferSource,
            int p_112311_, int p_112312_) {
        stack.pushPose();
        VertexConsumer vc = bufferSource.getBuffer(RenderType.debugQuads());
        Matrix4f m = stack.last().pose();
        Color color = new Color(en.getColor());
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float a = color.getAlpha() / 255.0f;

        vc.vertex(m, 0, 0, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 0, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 0, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 0, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 1, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 1, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 1, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 1, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 0, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 1, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 1, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 0, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 0, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 0, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 1, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 1, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 0, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 0, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 1, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 0, 1, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 0, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 1, 0).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 1, 1).color(r, g, b, a).endVertex();
        vc.vertex(m, 1, 0, 1).color(r, g, b, a).endVertex();

        stack.popPose();
    }
    @Override
    public int getViewDistance() {
        return 1024;
    }
}
