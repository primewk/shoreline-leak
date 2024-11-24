package net.shoreline.client.mixin.render;

import net.minecraft.class_332;
import net.minecraft.class_4184;
import net.minecraft.class_5636;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.camera.CameraPositionEvent;
import net.shoreline.client.impl.event.camera.CameraRotationEvent;
import net.shoreline.client.impl.event.gui.hud.RenderOverlayEvent;
import net.shoreline.client.impl.event.render.CameraClipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_4184.class})
public abstract class MixinCamera {
   @Shadow
   private float field_47549;

   @Shadow
   protected abstract void method_19327(double var1, double var3, double var5);

   @Shadow
   protected abstract void method_19325(float var1, float var2);

   @Inject(
      method = {"getSubmersionType"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetSubmersionType(CallbackInfoReturnable<class_5636> cir) {
      RenderOverlayEvent.Water renderOverlayEvent = new RenderOverlayEvent.Water((class_332)null);
      Shoreline.EVENT_HANDLER.dispatch(renderOverlayEvent);
      if (renderOverlayEvent.isCanceled()) {
         cir.setReturnValue(class_5636.field_27888);
         cir.cancel();
      }

   }

   @Inject(
      method = {"clipToSpace"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> cir) {
      CameraClipEvent cameraClipEvent = new CameraClipEvent(desiredCameraDistance);
      Shoreline.EVENT_HANDLER.dispatch(cameraClipEvent);
      if (cameraClipEvent.isCanceled()) {
         cir.setReturnValue(cameraClipEvent.getDistance());
         cir.cancel();
      }

   }

   @Redirect(
      method = {"update"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"
)
   )
   private void hookUpdatePosition(class_4184 instance, double x, double y, double z) {
      CameraPositionEvent cameraPositionEvent = new CameraPositionEvent(x, y, z, this.field_47549);
      Shoreline.EVENT_HANDLER.dispatch(cameraPositionEvent);
      this.method_19327(cameraPositionEvent.getX(), cameraPositionEvent.getY(), cameraPositionEvent.getZ());
   }

   @Redirect(
      method = {"update"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"
)
   )
   private void hookUpdateRotation(class_4184 instance, float yaw, float pitch) {
      CameraRotationEvent cameraRotationEvent = new CameraRotationEvent(yaw, pitch, this.field_47549);
      Shoreline.EVENT_HANDLER.dispatch(cameraRotationEvent);
      this.method_19325(cameraRotationEvent.getYaw(), cameraRotationEvent.getPitch());
   }
}
