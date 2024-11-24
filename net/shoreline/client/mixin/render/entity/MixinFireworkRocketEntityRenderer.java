package net.shoreline.client.mixin.render.entity;

import net.minecraft.class_1671;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_903;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.entity.RenderFireworkRocketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_903.class})
public class MixinFireworkRocketEntityRenderer {
   @Inject(
      method = {"render(Lnet/minecraft/entity/projectile/FireworkRocketEntity;FFLnet/minecraft/client/util/math/ MatrixStack;Lnet/minecraft/client/render/ VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRender(class_1671 fireworkRocketEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      RenderFireworkRocketEvent renderFireworkRocketEvent = new RenderFireworkRocketEvent();
      Shoreline.EVENT_HANDLER.dispatch(renderFireworkRocketEvent);
      if (renderFireworkRocketEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
