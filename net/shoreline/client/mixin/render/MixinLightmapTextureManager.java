package net.shoreline.client.mixin.render;

import java.awt.Color;
import net.minecraft.class_1011;
import net.minecraft.class_243;
import net.minecraft.class_765;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.AmbientColorEvent;
import net.shoreline.client.impl.event.render.LightmapGammaEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({class_765.class})
public class MixinLightmapTextureManager {
   @Shadow
   @Final
   private class_1011 field_4133;

   @ModifyArgs(
      method = {"update"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"
)
   )
   private void hookUpdate(Args args) {
      LightmapGammaEvent lightmapGammaEvent = new LightmapGammaEvent((Integer)args.get(2));
      Shoreline.EVENT_HANDLER.dispatch(lightmapGammaEvent);
      if (lightmapGammaEvent.isCanceled()) {
         args.set(2, lightmapGammaEvent.getGamma());
      }

   }

   @Inject(
      method = {"update"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/texture/NativeImageBackedTexture;upload()V",
   shift = Shift.BEFORE
)}
   )
   private void hookUpdate(float delta, CallbackInfo ci) {
      AmbientColorEvent ambientColorEvent = new AmbientColorEvent();
      Shoreline.EVENT_HANDLER.dispatch(ambientColorEvent);
      if (ambientColorEvent.isCanceled()) {
         for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
               int color = this.field_4133.method_4315(i, j);
               int[] bgr = new int[]{color >> 16 & 255, color >> 8 & 255, color & 255};
               class_243 colors = new class_243((double)bgr[2] / 255.0D, (double)bgr[1] / 255.0D, (double)bgr[0] / 255.0D);
               Color c = ambientColorEvent.getColor();
               class_243 ncolors = new class_243((double)c.getRed() / 255.0D, (double)c.getGreen() / 255.0D, (double)c.getBlue() / 255.0D);
               class_243 mix = this.mix(colors, ncolors, (double)c.getAlpha() / 255.0D);
               int r = (int)(mix.field_1352 * 255.0D);
               int g = (int)(mix.field_1351 * 255.0D);
               int b = (int)(mix.field_1350 * 255.0D);
               this.field_4133.method_4305(i, j, -16777216 | r << 16 | g << 8 | b);
            }
         }
      }

   }

   private class_243 mix(class_243 first, class_243 second, double factor) {
      return new class_243(first.field_1352 * (1.0D - factor) + second.field_1352 * factor, first.field_1351 * (1.0D - factor) + second.field_1351 * factor, first.field_1350 * (1.0D - factor) + first.field_1350 * factor);
   }
}
