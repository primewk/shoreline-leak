package net.shoreline.client.mixin.gui.screen;

import net.minecraft.class_1735;
import net.minecraft.class_332;
import net.minecraft.class_465;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.gui.RenderTooltipEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_465.class})
public class MixinHandledScreen {
   @Shadow
   @Nullable
   protected class_1735 field_2787;

   @Inject(
      method = {"drawMouseoverTooltip"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookDrawMouseoverTooltip(class_332 context, int x, int y, CallbackInfo ci) {
      if (this.field_2787 != null) {
         RenderTooltipEvent renderTooltipEvent = new RenderTooltipEvent(context, this.field_2787.method_7677(), x, y);
         Shoreline.EVENT_HANDLER.dispatch(renderTooltipEvent);
         if (renderTooltipEvent.isCanceled()) {
            ci.cancel();
         }

      }
   }
}
