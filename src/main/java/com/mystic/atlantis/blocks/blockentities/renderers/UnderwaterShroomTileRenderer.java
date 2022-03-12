package com.mystic.atlantis.blocks.blockentities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mystic.atlantis.blocks.blockentities.models.UnderwaterShroomModel;
import com.mystic.atlantis.blocks.blockentities.plants.UnderwaterShroomTileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class UnderwaterShroomTileRenderer extends GeoBlockRenderer<UnderwaterShroomTileEntity> {
    public UnderwaterShroomTileRenderer() {
        super(new UnderwaterShroomModel());
    }

    @Override
    public RenderType getRenderType(UnderwaterShroomTileEntity animatable, float partialTicks, PoseStack stack,
                                     MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                     ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
