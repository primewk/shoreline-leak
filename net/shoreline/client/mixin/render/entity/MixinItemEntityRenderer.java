package net.shoreline.client.mixin.render.entity;

import net.minecraft.class_1542;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_916;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.entity.RenderItemEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_916.class})
public class MixinItemEntityRenderer {
   @Inject(
      method = {"render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRender(class_1542 itemEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      RenderItemEvent renderItemEvent = new RenderItemEvent(itemEntity);
      Shoreline.EVENT_HANDLER.dispatch(renderItemEvent);
      if (renderItemEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
