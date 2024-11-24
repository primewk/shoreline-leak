package net.shoreline.client.mixin.render;

import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_5912;
import net.minecraft.class_757;
import net.minecraft.class_239.class_240;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.network.ReachEvent;
import net.shoreline.client.impl.event.render.BobViewEvent;
import net.shoreline.client.impl.event.render.FovEvent;
import net.shoreline.client.impl.event.render.HurtCamEvent;
import net.shoreline.client.impl.event.render.RenderBlockOutlineEvent;
import net.shoreline.client.impl.event.render.RenderFloatingItemEvent;
import net.shoreline.client.impl.event.render.RenderNauseaEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.impl.event.render.TargetEntityEvent;
import net.shoreline.client.impl.event.world.UpdateCrosshairTargetEvent;
import net.shoreline.client.init.Programs;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({class_757.class})
public class MixinGameRenderer implements Globals {
   @Shadow
   @Final
   class_310 field_4015;
   @Shadow
   private float field_3999;
   @Shadow
   private float field_4019;

   @Inject(
      method = {"renderWorld"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
   ordinal = 1
)}
   )
   private void hookRenderWorld(float tickDelta, long limitTime, class_4587 matrices, CallbackInfo ci) {
      RenderWorldEvent.Game renderWorldEvent = new RenderWorldEvent.Game(matrices, tickDelta);
      Shoreline.EVENT_HANDLER.dispatch(renderWorldEvent);
   }

   @Inject(
      method = {"updateTargetedEntity"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V",
   shift = Shift.AFTER
)}
   )
   private void hookUpdateTargetedEntity$1(float tickDelta, CallbackInfo info) {
      UpdateCrosshairTargetEvent event = new UpdateCrosshairTargetEvent(tickDelta, this.field_4015.method_1560());
      Shoreline.EVENT_HANDLER.dispatch(event);
   }

   @Inject(
      method = {"tiltViewWhenHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookTiltViewWhenHurt(class_4587 matrices, float tickDelta, CallbackInfo ci) {
      HurtCamEvent hurtCamEvent = new HurtCamEvent();
      Shoreline.EVENT_HANDLER.dispatch(hurtCamEvent);
      if (hurtCamEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"showFloatingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookShowFloatingItem(class_1799 floatingItem, CallbackInfo ci) {
      RenderFloatingItemEvent renderFloatingItemEvent = new RenderFloatingItemEvent(floatingItem);
      Shoreline.EVENT_HANDLER.dispatch(renderFloatingItemEvent);
      if (renderFloatingItemEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"renderNausea"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRenderNausea(class_332 context, float distortionStrength, CallbackInfo ci) {
      RenderNauseaEvent renderNauseaEvent = new RenderNauseaEvent();
      Shoreline.EVENT_HANDLER.dispatch(renderNauseaEvent);
      if (renderNauseaEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"shouldRenderBlockOutline"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookShouldRenderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
      RenderBlockOutlineEvent renderBlockOutlineEvent = new RenderBlockOutlineEvent();
      Shoreline.EVENT_HANDLER.dispatch(renderBlockOutlineEvent);
      if (renderBlockOutlineEvent.isCanceled()) {
         cir.setReturnValue(false);
         cir.cancel();
      }

   }

   @Inject(
      method = {"updateTargetedEntity"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"
)},
      cancellable = true
   )
   private void hookUpdateTargetedEntity$2(float tickDelta, CallbackInfo info) {
      TargetEntityEvent targetEntityEvent = new TargetEntityEvent();
      Shoreline.EVENT_HANDLER.dispatch(targetEntityEvent);
      if (targetEntityEvent.isCanceled() && this.field_4015.field_1765.method_17783() == class_240.field_1332) {
         this.field_4015.method_16011().method_15407();
         info.cancel();
      }

   }

   @ModifyConstant(
      method = {"updateTargetedEntity"},
      constant = {@Constant(
   doubleValue = 9.0D
)}
   )
   private double updateTargetedEntityModifySquaredMaxReach(double d) {
      ReachEvent reachEvent = new ReachEvent();
      Shoreline.EVENT_HANDLER.dispatch(reachEvent);
      double reach = (double)reachEvent.getReach() + 3.0D;
      return reachEvent.isCanceled() ? reach * reach : 9.0D;
   }

   @Inject(
      method = {"bobView"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookBobView(class_4587 matrices, float tickDelta, CallbackInfo ci) {
      BobViewEvent bobViewEvent = new BobViewEvent();
      Shoreline.EVENT_HANDLER.dispatch(bobViewEvent);
      if (bobViewEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"getFov"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetFov(class_4184 camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
      FovEvent fovEvent = new FovEvent();
      Shoreline.EVENT_HANDLER.dispatch(fovEvent);
      if (fovEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(fovEvent.getFov() * (double)class_3532.method_16439(tickDelta, this.field_3999, this.field_4019));
      }

   }

   @Inject(
      method = {"loadPrograms"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
   ordinal = 0
)},
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void initPrograms(class_5912 factory, CallbackInfo ci) {
      Programs.initPrograms();
   }
}
