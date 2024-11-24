package net.shoreline.client.mixin.render;

import net.minecraft.class_317;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.TickCounterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_317.class})
public class MixinRenderTickCounter {
   @Shadow
   private float field_1969;
   @Shadow
   private float field_1970;
   @Shadow
   private long field_1971;
   @Shadow
   private float field_1968;

   @Inject(
      method = {"beginRenderTick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookBeginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
      TickCounterEvent tickCounterEvent = new TickCounterEvent();
      Shoreline.EVENT_HANDLER.dispatch(tickCounterEvent);
      if (tickCounterEvent.isCanceled()) {
         this.field_1969 = (float)(timeMillis - this.field_1971) / this.field_1968 * tickCounterEvent.getTicks();
         this.field_1971 = timeMillis;
         this.field_1970 += this.field_1969;
         int i = (int)this.field_1970;
         this.field_1970 -= (float)i;
         cir.setReturnValue(i);
      }

   }
}
