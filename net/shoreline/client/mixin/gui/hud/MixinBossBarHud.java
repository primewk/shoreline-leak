package net.shoreline.client.mixin.gui.hud;

import net.minecraft.class_332;
import net.minecraft.class_337;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.gui.hud.RenderOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_337.class})
public class MixinBossBarHud {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRender(class_332 context, CallbackInfo ci) {
      RenderOverlayEvent.BossBar renderOverlayEvent = new RenderOverlayEvent.BossBar(context);
      Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
      if (renderOverlayEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
