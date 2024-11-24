package net.shoreline.client.mixin;

import net.minecraft.class_312;
import net.minecraft.class_746;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.MouseClickEvent;
import net.shoreline.client.impl.event.MouseUpdateEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_312.class})
public class MixinMouse {
   @Inject(
      method = {"onMouseButton"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
      MouseClickEvent mouseClickEvent = new MouseClickEvent(button, action);
      Shoreline.EVENT_HANDLER.dispatch(mouseClickEvent);
      if (mouseClickEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Redirect(
      method = {"updateMouse"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"
)
   )
   public void onUpdate(class_746 instance, double cursorDeltaX, double cursorDeltaY) {
      MouseUpdateEvent mouseUpdateEvent = new MouseUpdateEvent(cursorDeltaX, cursorDeltaY);
      Shoreline.EVENT_HANDLER.dispatch(mouseUpdateEvent);
      if (!mouseUpdateEvent.isCanceled()) {
         instance.method_5872(cursorDeltaX, cursorDeltaY);
      }

   }
}
