package net.shoreline.client.mixin.world;

import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3726;
import net.minecraft.class_5329;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.world.BlockCollisionEvent;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({class_5329.class})
public class MixinBlockCollisionSpliterator implements Globals {
   @Redirect(
      method = {"computeNext"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/block/BlockState;getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"
)
   )
   private class_265 hookGetCollisionShape(class_2680 instance, class_1922 blockView, class_2338 blockPos, class_3726 shapeContext) {
      class_265 voxelShape = instance.method_26194(blockView, blockPos, shapeContext);
      if (blockView != mc.field_1687) {
         return voxelShape;
      } else {
         BlockCollisionEvent blockCollisionEvent = new BlockCollisionEvent(voxelShape, blockPos, instance);
         Shoreline.EVENT_HANDLER.dispatch(blockCollisionEvent);
         return blockCollisionEvent.isCanceled() ? blockCollisionEvent.getVoxelShape() : voxelShape;
      }
   }
}
