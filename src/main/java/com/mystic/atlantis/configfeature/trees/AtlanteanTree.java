package com.mystic.atlantis.configfeature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.jetbrains.annotations.NotNull;

public class AtlanteanTree extends Feature<TreeConfiguration> {

    public AtlanteanTree(Codec<TreeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<TreeConfiguration> arg) {
        return true;
    }
}
