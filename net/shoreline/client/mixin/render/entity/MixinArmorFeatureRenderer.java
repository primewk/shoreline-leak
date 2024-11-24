package net.shoreline.client.mixin.render.entity;

import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_572;
import net.minecraft.class_970;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.entity.RenderArmorEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_970.class})
public class MixinArmorFeatureRenderer {
   @Inject(
      method = {"renderArmor"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRenderArmor(class_4587 matrices, class_4597 vertexConsumers, class_1309 entity, class_1304 armorSlot, int light, class_572<?> model, CallbackInfo ci) {
      RenderArmorEvent renderArmorEvent = new RenderArmorEvent(entity);
      Shoreline.EVENT_HANDLER.dispatch(renderArmorEvent);
      if (renderArmorEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
