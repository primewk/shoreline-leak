package net.shoreline.client.mixin.render.entity;

import net.minecraft.class_1687;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_966;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.entity.RenderWitherSkullEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_966.class})
public class MixinWitherSkullEntityRenderer {
   @Inject(
      method = {"render(Lnet/minecraft/entity/projectile/WitherSkullEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRender(class_1687 witherSkullEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      RenderWitherSkullEvent renderWitherSkullEvent = new RenderWitherSkullEvent();
      Shoreline.EVENT_HANDLER.dispatch(renderWitherSkullEvent);
      if (renderWitherSkullEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
