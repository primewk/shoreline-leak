package net.shoreline.client.mixin.entity;

import net.minecraft.class_1297;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_5712;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.camera.EntityCameraPositionEvent;
import net.shoreline.client.impl.event.entity.EntityGameEvent;
import net.shoreline.client.impl.event.entity.EntityRotationVectorEvent;
import net.shoreline.client.impl.event.entity.LookDirectionEvent;
import net.shoreline.client.impl.event.entity.SetBBEvent;
import net.shoreline.client.impl.event.entity.SlowMovementEvent;
import net.shoreline.client.impl.event.entity.UpdateVelocityEvent;
import net.shoreline.client.impl.event.entity.VelocityMultiplierEvent;
import net.shoreline.client.impl.event.entity.decoration.TeamColorEvent;
import net.shoreline.client.impl.event.entity.player.PushEntityEvent;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1297.class})
public abstract class MixinEntity implements Globals {
   @Shadow
   public boolean field_6007;

   @Shadow
   private static class_243 method_18795(class_243 movementInput, float speed, float yaw) {
      return null;
   }

   @Shadow
   public abstract class_238 method_5829();

   @Shadow
   public abstract class_243 method_18798();

   @Shadow
   public abstract void method_18799(class_243 var1);

   @Shadow
   public abstract boolean method_5624();

   @Inject(
      method = {"getRotationVec"},
      at = {@At("RETURN")},
      cancellable = true
   )
   public void hookGetCameraPosVec(float tickDelta, CallbackInfoReturnable<class_243> info) {
      EntityRotationVectorEvent event = new EntityRotationVectorEvent(tickDelta, (class_1297)this, (class_243)info.getReturnValue());
      Shoreline.EVENT_HANDLER.dispatch(event);
      info.setReturnValue(event.getPosition());
   }

   @Inject(
      method = {"adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"},
      at = {@At("HEAD")}
   )
   public void hookMove(class_243 movement, CallbackInfoReturnable<class_243> cir) {
      if (this == mc.field_1724) {
         ;
      }
   }

   @Inject(
      method = {"slowMovement"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookSlowMovement(class_2680 state, class_243 multiplier, CallbackInfo ci) {
      if (this == mc.field_1724) {
         SlowMovementEvent slowMovementEvent = new SlowMovementEvent(state);
         Shoreline.EVENT_HANDLER.dispatch(slowMovementEvent);
         if (slowMovementEvent.isCanceled()) {
            ci.cancel();
         }

      }
   }

   @Redirect(
      method = {"getVelocityMultiplier"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/ block/Block;"
)
   )
   private class_2248 hookGetVelocityMultiplier(class_2680 instance) {
      if (this != mc.field_1724) {
         return instance.method_26204();
      } else {
         VelocityMultiplierEvent velocityMultiplierEvent = new VelocityMultiplierEvent(instance);
         Shoreline.EVENT_HANDLER.dispatch(velocityMultiplierEvent);
         return velocityMultiplierEvent.isCanceled() ? class_2246.field_10566 : instance.method_26204();
      }
   }

   @Inject(
      method = {"updateVelocity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookUpdateVelocity(float speed, class_243 movementInput, CallbackInfo ci) {
      if (this == mc.field_1724) {
         UpdateVelocityEvent updateVelocityEvent = new UpdateVelocityEvent(movementInput, speed, mc.field_1724.method_36454(), method_18795(movementInput, speed, mc.field_1724.method_36454()));
         Shoreline.EVENT_HANDLER.dispatch(updateVelocityEvent);
         if (updateVelocityEvent.isCanceled()) {
            ci.cancel();
            mc.field_1724.method_18799(mc.field_1724.method_18798().method_1019(updateVelocityEvent.getVelocity()));
         }
      }

   }

   @Inject(
      method = {"pushAwayFrom"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookPushAwayFrom(class_1297 entity, CallbackInfo ci) {
      PushEntityEvent pushEntityEvent = new PushEntityEvent((class_1297)this, entity);
      Shoreline.EVENT_HANDLER.dispatch(pushEntityEvent);
      if (pushEntityEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"getTeamColorValue"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetTeamColorValue(CallbackInfoReturnable<Integer> cir) {
      TeamColorEvent teamColorEvent = new TeamColorEvent((class_1297)this);
      Shoreline.EVENT_HANDLER.dispatch(teamColorEvent);
      if (teamColorEvent.isCanceled()) {
         cir.setReturnValue(teamColorEvent.getColor());
         cir.cancel();
      }

   }

   @Inject(
      method = {"changeLookDirection"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
      if (this == mc.field_1724) {
         LookDirectionEvent lookDirectionEvent = new LookDirectionEvent((class_1297)this, cursorDeltaX, cursorDeltaY);
         Shoreline.EVENT_HANDLER.dispatch(lookDirectionEvent);
         if (lookDirectionEvent.isCanceled()) {
            ci.cancel();
         }
      }

   }

   @Inject(
      method = {"emitGameEvent(Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/entity/Entity;)V"},
      at = {@At("HEAD")}
   )
   private void hookEmitGameEvent(class_5712 event, class_1297 entity, CallbackInfo ci) {
      EntityGameEvent entityGameEvent = new EntityGameEvent(event, entity);
      Shoreline.EVENT_HANDLER.dispatch(entityGameEvent);
   }

   @Inject(
      method = {"getCameraPosVec"},
      at = {@At("RETURN")},
      cancellable = true
   )
   public void hookCameraPositionVec(float tickDelta, CallbackInfoReturnable<class_243> cir) {
      EntityCameraPositionEvent cameraPositionEvent = new EntityCameraPositionEvent((class_243)cir.getReturnValue(), (class_1297)this, tickDelta);
      Shoreline.EVENT_HANDLER.dispatch(cameraPositionEvent);
      cir.setReturnValue(cameraPositionEvent.getPosition());
   }

   @Inject(
      method = {"setBoundingBox"},
      at = {@At("HEAD")}
   )
   private void hookSetBoundingBox(class_238 boundingBox, CallbackInfo ci) {
      if (this == mc.field_1724) {
         SetBBEvent setBBEvent = new SetBBEvent(boundingBox);
         Shoreline.EVENT_HANDLER.dispatch(setBBEvent);
      }

   }
}
