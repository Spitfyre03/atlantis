package com.mystic.atlantis.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mystic.atlantis.dimension.DimensionAtlantis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class RenderBubblesMixin extends GuiComponent {


    @Shadow
    @Final
    private Minecraft client;
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;
    private LivingEntity getCameraPlayer;

    @Shadow
    protected abstract Player getCameraPlayer();

    @Shadow
    protected abstract int getHeartCount(LivingEntity entity);

    @Shadow
    protected abstract int getHeartRows(int heartCount);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 2), method = "renderStatusBars", cancellable = true)
    public void RenderBubbles(PoseStack matrices, CallbackInfo ci) {
        ci.cancel();
        int ah;
        int ai;
        int ad;
        int ae;
        int o = this.scaledHeight - 39;
        int t = o - 10 - 10;
        int al;
        Entity playerEntity = this.getCameraPlayer();
        LivingEntity livingEntity = this.getCameraPlayer;
        int aa = this.getHeartCount(livingEntity);
        int n = this.scaledWidth / 2 + 91;

        this.client.getProfiler().popPush("air");
        ah = playerEntity.getMaxAirSupply();
        ai = Math.min(playerEntity.getAirSupply(), ah);
        if (playerEntity.level.dimension() == DimensionAtlantis.ATLANTIS_WORLD) {
            if (!playerEntity.isEyeInFluid(FluidTags.WATER) || ai < ah) {/*change*/
                ad = this.getHeartRows(aa) - 1;
                t -= ad * 10;
                ae = Mth.ceil((double) (ai - 2) * 10.0D / (double) ah);
                al = Mth.ceil((double) ai * 10.0D / (double) ah) - ae;

                for (int ar = 0; ar < ae + al; ++ar) {
                    if (ar < ae) {
                        this.blit(matrices, n - ar * 8 - 9, t, 16, 18, 9, 9);
                    } else {
                        this.blit(matrices, n - ar * 8 - 9, t, 25, 18, 9, 9);
                    }
                }
            }
        } else {
            if (playerEntity.isEyeInFluid(FluidTags.WATER) || ai < ah) {/*change*/
                ad = this.getHeartRows(aa) - 1;
                t -= ad * 10;
                ae = Mth.ceil((double) (ai - 2) * 10.0D / (double) ah);
                al = Mth.ceil((double) ai * 10.0D / (double) ah) - ae;

                for (int ar = 0; ar < ae + al; ++ar) {
                    if (ar < ae) {
                        this.blit(matrices, n - ar * 8 - 9, t, 16, 18, 9, 9);
                    } else {
                        this.blit(matrices, n - ar * 8 - 9, t, 25, 18, 9, 9);
                    }
                }
            }
        }
        this.client.getProfiler().pop();
    }
}
