package net.shoreline.client.mixin.block;

import net.minecraft.class_2248;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.block.BlockSlipperinessEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_2248.class})
public class MixinBlock {
   @Inject(
      method = {"getSlipperiness"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void hookGetSlipperiness(CallbackInfoReturnable<Float> cir) {
      BlockSlipperinessEvent blockSlipperinessEvent = new BlockSlipperinessEvent((class_2248)this, cir.getReturnValueF());
      Shoreline.EVENT_HANDLER.dispatch(blockSlipperinessEvent);
      if (blockSlipperinessEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(blockSlipperinessEvent.getSlipperiness());
      }

   }
}
