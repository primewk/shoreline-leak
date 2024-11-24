package net.shoreline.client.mixin.render.entity;

import net.minecraft.class_1511;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_630;
import net.minecraft.class_892;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.entity.RenderCrystalEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_892.class})
public class MixinEndCrystalEntityRenderer {
   @Shadow
   @Final
   private class_630 field_21003;
   @Shadow
   @Final
   private class_630 field_21004;

   @Inject(
      method = {"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRender(class_1511 endCrystalEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      RenderCrystalEvent renderCrystalEvent = new RenderCrystalEvent(endCrystalEntity, f, g, matrixStack, i, this.field_21003, this.field_21004);
      Shoreline.EVENT_HANDLER.dispatch(renderCrystalEvent);
      if (renderCrystalEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
