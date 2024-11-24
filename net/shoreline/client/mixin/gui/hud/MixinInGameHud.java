package net.shoreline.client.mixin.gui.hud;

import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.gui.hud.RenderOverlayEvent;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_329.class})
public class MixinInGameHud implements Globals {
   @Shadow
   @Final
   private static class_2960 field_2019;
   @Shadow
   @Final
   private static class_2960 field_27960;

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   private void hookRender(class_332 context, float tickDelta, CallbackInfo ci) {
      RenderOverlayEvent.Post renderOverlayEvent = new RenderOverlayEvent.Post(context, tickDelta);
      Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
   }

   @Inject(
      method = {"renderStatusEffectOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRenderStatusEffectOverlay(class_332 context, CallbackInfo ci) {
      RenderOverlayEvent.StatusEffect renderOverlayEvent = new RenderOverlayEvent.StatusEffect(context);
      Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
      if (renderOverlayEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"renderSpyglassOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRenderSpyglassOverlay(class_332 context, float scale, CallbackInfo ci) {
      RenderOverlayEvent.Spyglass renderOverlayEvent = new RenderOverlayEvent.Spyglass(context);
      Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
      if (renderOverlayEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"renderOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRenderOverlay(class_332 context, class_2960 texture, float opacity, CallbackInfo ci) {
      if (texture.method_12832().equals(field_2019.method_12832())) {
         RenderOverlayEvent.Pumpkin renderOverlayEvent = new RenderOverlayEvent.Pumpkin(context);
         Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
         if (renderOverlayEvent.isCanceled()) {
            ci.cancel();
         }
      } else if (texture.method_12832().equals(field_27960.method_12832())) {
         RenderOverlayEvent.Frostbite renderOverlayEvent = new RenderOverlayEvent.Frostbite(context);
         Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
         if (renderOverlayEvent.isCanceled()) {
            ci.cancel();
         }
      }

   }

   @Redirect(
      method = {"renderHeldItemTooltip"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"
)
   )
   private int hookRenderHeldItemTooltip(class_332 instance, class_327 textRenderer, class_2561 text, int x, int y, int color) {
      RenderOverlayEvent.ItemName renderOverlayEvent = new RenderOverlayEvent.ItemName(instance);
      Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
      if (renderOverlayEvent.isCanceled()) {
         return renderOverlayEvent.isUpdateXY() ? instance.method_51439(mc.field_1772, text, renderOverlayEvent.getX(), renderOverlayEvent.getY(), color, true) : 0;
      } else {
         return instance.method_51439(mc.field_1772, text, x, y, color, true);
      }
   }
}
