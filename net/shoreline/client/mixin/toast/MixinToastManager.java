package net.shoreline.client.mixin.toast;

import net.minecraft.class_332;
import net.minecraft.class_374;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.toast.RenderToastEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_374.class})
public class MixinToastManager {
   @Inject(
      method = {"draw"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookDraw(class_332 context, CallbackInfo ci) {
      RenderToastEvent renderToastEvent = new RenderToastEvent();
      Shoreline.EVENT_HANDLER.dispatch(renderToastEvent);
      if (renderToastEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
