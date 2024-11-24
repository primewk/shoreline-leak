package net.shoreline.client.mixin.render.item;

import net.minecraft.class_1007;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import net.minecraft.class_759;
import net.minecraft.class_898;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.item.RenderArmEvent;
import net.shoreline.client.impl.event.render.item.RenderFirstPersonEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_759.class})
public class MixinHeldItemRenderer {
   @Shadow
   @Final
   private class_898 field_4046;
   @Shadow
   @Final
   private class_310 field_4050;

   @Inject(
      method = {"renderArmHoldingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRenderArm(class_4587 matrices, class_4597 vertexConsumers, int light, float equipProgress, float swingProgress, class_1306 arm, CallbackInfo ci) {
      class_1007 playerEntityRenderer = (class_1007)this.field_4046.method_3953(this.field_4050.field_1724);
      RenderArmEvent renderArmEvent = new RenderArmEvent(matrices, vertexConsumers, light, equipProgress, swingProgress, arm, playerEntityRenderer);
      Shoreline.EVENT_HANDLER.dispatch(renderArmEvent);
      if (renderArmEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
)}
   )
   private void hookRenderFirstPersonItem(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
      RenderFirstPersonEvent renderFirstPersonEvent = new RenderFirstPersonEvent(hand, item, equipProgress, matrices);
      Shoreline.EVENT_HANDLER.dispatch(renderFirstPersonEvent);
   }
}
