package net.shoreline.client.mixin.block;

import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2490;
import net.minecraft.class_2680;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.block.SteppedOnSlimeBlockEvent;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2490.class})
public class MixinSlimeBlock implements Globals {
   @Inject(
      method = {"onSteppedOn"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookOnSteppedOn(class_1937 world, class_2338 pos, class_2680 state, class_1297 entity, CallbackInfo ci) {
      SteppedOnSlimeBlockEvent steppedOnSlimeBlockEvent = new SteppedOnSlimeBlockEvent();
      Shoreline.EVENT_HANDLER.dispatch(steppedOnSlimeBlockEvent);
      if (steppedOnSlimeBlockEvent.isCanceled() && entity == mc.field_1724) {
         ci.cancel();
      }

   }
}
