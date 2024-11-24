package net.shoreline.client.mixin.render;

import net.minecraft.class_243;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_761;
import net.minecraft.class_765;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.render.RenderBuffers;
import net.shoreline.client.impl.event.PerspectiveEvent;
import net.shoreline.client.impl.event.render.RenderWorldBorderEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.util.Globals;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_761.class})
public class MixinWorldRenderer implements Globals {
   @Inject(
      method = {"render"},
      at = {@At("RETURN")}
   )
   private void hookRender(class_4587 matrices, float tickDelta, long limitTime, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, class_765 lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
      class_243 pos = mc.method_31975().field_4344.method_19326();
      matrices.method_22904(-pos.field_1352, -pos.field_1351, -pos.field_1350);
      RenderBuffers.preRender();
      RenderWorldEvent renderWorldEvent = new RenderWorldEvent(matrices, tickDelta);
      Shoreline.EVENT_HANDLER.dispatch(renderWorldEvent);
      RenderBuffers.postRender();
   }

   @Inject(
      method = {"renderWorldBorder"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRenderWorldBorder(class_4184 camera, CallbackInfo ci) {
      RenderWorldBorderEvent renderWorldBorderEvent = new RenderWorldBorderEvent();
      Shoreline.EVENT_HANDLER.dispatch(renderWorldBorderEvent);
      if (renderWorldBorderEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Redirect(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/Camera;isThirdPerson()Z"
)
   )
   public boolean hookRender(class_4184 instance) {
      PerspectiveEvent perspectiveEvent = new PerspectiveEvent(instance);
      Shoreline.EVENT_HANDLER.dispatch(perspectiveEvent);
      return perspectiveEvent.isCanceled() ? true : instance.method_19333();
   }
}
