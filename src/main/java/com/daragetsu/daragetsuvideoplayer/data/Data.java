package com.daragetsu.daragetsuvideoplayer.data;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Data(
    String name,
    String location
) {
    public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.STRING.fieldOf("name").forGetter(Data::name),
            Codec.STRING.fieldOf("location").forGetter(Data::location)
        ).apply(instance, Data::new)
    );
    public static final Codec<List<Data>> LIST =
        Data.CODEC.listOf();
}
