package net.shoreline.client.mixin.world;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_638;
import net.minecraft.class_1297.class_5529;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.world.AddEntityEvent;
import net.shoreline.client.impl.event.world.RemoveEntityEvent;
import net.shoreline.client.impl.event.world.SkyboxEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_638.class})
public abstract class MixinClientWorld {
   @Shadow
   @Nullable
   public abstract class_1297 method_8469(int var1);

   @Inject(
      method = {"addEntity"},
      at = {@At("HEAD")}
   )
   private void hookAddEntity(class_1297 entity, CallbackInfo ci) {
      AddEntityEvent addEntityEvent = new AddEntityEvent(entity);
      Shoreline.EVENT_HANDLER.dispatch(addEntityEvent);
   }

   @Inject(
      method = {"removeEntity"},
      at = {@At("HEAD")}
   )
   private void hookRemoveEntity(int entityId, class_5529 removalReason, CallbackInfo ci) {
      class_1297 entity = this.method_8469(entityId);
      if (entity != null) {
         RemoveEntityEvent addEntityEvent = new RemoveEntityEvent(entity, removalReason);
         Shoreline.EVENT_HANDLER.dispatch(addEntityEvent);
      }
   }

   @Inject(
      method = {"getSkyColor"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetSkyColor(class_243 cameraPos, float tickDelta, CallbackInfoReturnable<class_243> cir) {
      SkyboxEvent.Sky skyboxEvent = new SkyboxEvent.Sky();
      Shoreline.EVENT_HANDLER.dispatch(skyboxEvent);
      if (skyboxEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(skyboxEvent.getColorVec());
      }

   }

   @Inject(
      method = {"getCloudsColor"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetCloudsColor(float tickDelta, CallbackInfoReturnable<class_243> cir) {
      SkyboxEvent.Cloud skyboxEvent = new SkyboxEvent.Cloud();
      Shoreline.EVENT_HANDLER.dispatch(skyboxEvent);
      if (skyboxEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(skyboxEvent.getColorVec());
      }

   }
}
