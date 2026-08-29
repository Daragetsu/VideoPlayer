package com.daragetsu.daragetsuvideoplayer.data;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ColorData(ColorRecord[] blocks) {

    public static final Codec<ColorData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            ColorRecord.CODEC.listOf()
                .xmap(
                    list -> list.toArray(ColorRecord[]::new),
                    List::of
                )
                .fieldOf("blocks")
                .forGetter(ColorData::blocks)
        ).apply(instance, ColorData::new)
    );

    public record ColorRecord(
        String block,
        int color
    ) {
        public static final Codec<ColorRecord> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.STRING.fieldOf("block").forGetter(ColorRecord::block),
                Codec.INT.fieldOf("color").forGetter(ColorRecord::color)
            ).apply(instance, ColorRecord::new)
        );
    }
}