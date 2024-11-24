package net.shoreline.client.mixin;

import net.minecraft.class_309;
import net.minecraft.class_310;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.keyboard.KeyboardInputEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_309.class})
public class MixinKeyboard {
   @Shadow
   @Final
   private class_310 field_1678;

   @Inject(
      method = {"onKey"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookOnKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
      if (this.field_1678.method_22683().method_4490() == window) {
         KeyboardInputEvent keyboardInputEvent = new KeyboardInputEvent(key, action);
         Shoreline.EVENT_HANDLER.dispatch(keyboardInputEvent);
         if (keyboardInputEvent.isCanceled()) {
            ci.cancel();
         }
      }

   }
}
