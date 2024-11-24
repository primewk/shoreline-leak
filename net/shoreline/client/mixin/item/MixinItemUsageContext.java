package net.shoreline.client.mixin.item;

import net.minecraft.class_1799;
import net.minecraft.class_1838;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1838.class})
public final class MixinItemUsageContext implements Globals {
   @Inject(
      method = {"getStack"},
      at = {@At("RETURN")},
      cancellable = true
   )
   public void hookGetStack(CallbackInfoReturnable<class_1799> info) {
      if (mc.field_1724 != null && ((class_1799)info.getReturnValue()).equals(mc.field_1724.method_6047()) && Managers.INVENTORY.isDesynced()) {
         info.setReturnValue(Managers.INVENTORY.getServerItem());
      }

   }
}
