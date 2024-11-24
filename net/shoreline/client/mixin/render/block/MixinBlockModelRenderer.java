package net.shoreline.client.mixin.render.block;

import net.minecraft.class_1087;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_5819;
import net.minecraft.class_778;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.block.RenderBlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_778.class})
public class MixinBlockModelRenderer {
   @Inject(
      method = {"render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLnet/minecraft/util/math/random/Random;JI)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRender(class_1920 world, class_1087 model, class_2680 state, class_2338 pos, class_4587 matrices, class_4588 vertexConsumer, boolean cull, class_5819 random, long seed, int overlay, CallbackInfo ci) {
      RenderBlockEvent renderBlockEvent = new RenderBlockEvent(state, pos);
      Shoreline.EVENT_HANDLER.dispatch(renderBlockEvent);
      if (renderBlockEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
