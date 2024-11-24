package net.shoreline.client.mixin.chunk.light;

import net.minecraft.class_3572;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.chunk.light.RenderSkylightEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_3572.class})
public class MixinChunkSkylightProvider {
   @Inject(
      method = {"method_51531"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRecalculateLevel(long blockPos, long l, int lightLevel, CallbackInfo ci) {
      RenderSkylightEvent renderSkylightEvent = new RenderSkylightEvent();
      Shoreline.EVENT_HANDLER.dispatch(renderSkylightEvent);
      if (renderSkylightEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
