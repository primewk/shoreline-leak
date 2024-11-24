package net.shoreline.client.mixin.gui.hud;

import net.minecraft.class_1058;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4603;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.gui.hud.RenderOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4603.class})
public class MixinInGameOverlayRenderer {
   @Inject(
      method = {"renderFireOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void hookRenderFireOverlay(class_310 client, class_4587 matrices, CallbackInfo ci) {
      RenderOverlayEvent.Fire renderOverlayEvent = new RenderOverlayEvent.Fire((class_332)null);
      Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
      if (renderOverlayEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"renderUnderwaterOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void hookRenderUnderwaterOverlay(class_310 client, class_4587 matrices, CallbackInfo ci) {
      RenderOverlayEvent.Water renderOverlayEvent = new RenderOverlayEvent.Water((class_332)null);
      Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
      if (renderOverlayEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"renderInWallOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void hookRenderFireOverlay(class_1058 sprite, class_4587 matrices, CallbackInfo ci) {
      RenderOverlayEvent.Block renderOverlayEvent = new RenderOverlayEvent.Block((class_332)null);
      Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
      if (renderOverlayEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
