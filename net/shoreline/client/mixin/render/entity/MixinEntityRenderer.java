package net.shoreline.client.mixin.render.entity;

import net.minecraft.class_1297;
import net.minecraft.class_2561;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_897;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.entity.RenderLabelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_897.class})
public abstract class MixinEntityRenderer {
   @Inject(
      method = {"renderLabelIfPresent"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void hookRenderLabelIfPresent(class_1297 entity, class_2561 text, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
      RenderLabelEvent renderLabelEvent = new RenderLabelEvent(entity);
      Shoreline.EVENT_HANDLER.dispatch(renderLabelEvent);
      if (renderLabelEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
