package net.shoreline.client.mixin.render.block;

import net.minecraft.class_2605;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_557;
import net.minecraft.class_828;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.block.RenderTileEntityEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_828.class})
public class MixinEnchantingTableBlockEntityRenderer {
   @Shadow
   @Final
   private class_557 field_4370;

   @Inject(
      method = {"render(Lnet/minecraft/block/entity/EnchantingTableBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/entity/model/BookModel;renderBook(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V",
   shift = Shift.BEFORE
)},
      cancellable = true
   )
   private void hookRender(class_2605 enchantingTableBlockEntity, float f, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, int j, CallbackInfo ci) {
      RenderTileEntityEvent.EnchantingTableBook renderTileEntityEvent = new RenderTileEntityEvent.EnchantingTableBook();
      Shoreline.EVENT_HANDLER.dispatch(renderTileEntityEvent);
      if (renderTileEntityEvent.isCanceled()) {
         ci.cancel();
         matrixStack.method_22909();
      }

   }
}
