package net.shoreline.client.mixin.entity.projectile;

import net.minecraft.class_1657;
import net.minecraft.class_1671;
import net.minecraft.class_2398;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.entity.projectile.RemoveFireworkEvent;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1671.class})
public class MixinFireworkRocketEntity implements Globals {
   @Shadow
   private int field_7613;

   @Inject(
      method = {"tick"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;updateRotation()V",
   shift = Shift.AFTER
)},
      cancellable = true
   )
   private void hookTickPre(CallbackInfo ci) {
      class_1671 rocketEntity = (class_1671)this;
      RemoveFireworkEvent removeFireworkEvent = new RemoveFireworkEvent(rocketEntity);
      Shoreline.EVENT_HANDLER.dispatch(removeFireworkEvent);
      if (removeFireworkEvent.isCanceled()) {
         ci.cancel();
         if (this.field_7613 == 0 && !rocketEntity.method_5701()) {
            mc.field_1687.method_43128((class_1657)null, rocketEntity.method_23317(), rocketEntity.method_23318(), rocketEntity.method_23321(), class_3417.field_14702, class_3419.field_15256, 3.0F, 1.0F);
         }

         ++this.field_7613;
         if (mc.field_1687.field_9236 && this.field_7613 % 2 < 2) {
            mc.field_1687.method_8406(class_2398.field_11248, rocketEntity.method_23317(), rocketEntity.method_23318(), rocketEntity.method_23321(), mc.field_1687.field_9229.method_43059() * 0.05D, -rocketEntity.method_18798().field_1351 * 0.5D, mc.field_1687.field_9229.method_43059() * 0.05D);
         }
      }

   }
}
