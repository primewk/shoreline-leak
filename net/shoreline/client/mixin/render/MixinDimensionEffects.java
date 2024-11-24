package net.shoreline.client.mixin.render;

import java.awt.Color;
import net.minecraft.class_5294;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.world.SkyboxEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_5294.class})
public class MixinDimensionEffects {
   @Inject(
      method = {"getFogColorOverride"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetFogColorOverride(float skyAngle, float tickDelta, CallbackInfoReturnable<float[]> cir) {
      SkyboxEvent.Fog skyboxEvent = new SkyboxEvent.Fog(tickDelta);
      Shoreline.EVENT_HANDLER.dispatch(skyboxEvent);
      if (skyboxEvent.isCanceled()) {
         Color color = skyboxEvent.getColor();
         cir.cancel();
         cir.setReturnValue(new float[]{(float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F});
      }

   }
}
