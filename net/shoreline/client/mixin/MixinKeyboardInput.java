package net.shoreline.client.mixin;

import net.minecraft.class_743;
import net.minecraft.class_744;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.impl.event.keyboard.KeyboardTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_743.class})
public class MixinKeyboardInput {
   @Inject(
      method = {"tick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookTick$Pre(boolean slowDown, float slowDownFactor, CallbackInfo info) {
      KeyboardTickEvent event = new KeyboardTickEvent((class_744)this);
      event.setStage(EventStage.PRE);
      Shoreline.EVENT_HANDLER.dispatch(event);
      if (event.isCanceled()) {
         info.cancel();
      }

   }

   @Inject(
      method = {"tick"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/input/KeyboardInput;sneaking:Z",
   shift = Shift.BEFORE
)},
      cancellable = true
   )
   private void hookTick$Post(boolean slowDown, float f, CallbackInfo ci) {
      KeyboardTickEvent keyboardTickEvent = new KeyboardTickEvent((class_744)this);
      keyboardTickEvent.setStage(EventStage.POST);
      Shoreline.EVENT_HANDLER.dispatch(keyboardTickEvent);
      if (keyboardTickEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
