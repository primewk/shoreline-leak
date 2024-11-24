package net.shoreline.client.mixin.network;

import net.minecraft.class_1268;
import net.minecraft.class_1313;
import net.minecraft.class_243;
import net.minecraft.class_2848;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_437;
import net.minecraft.class_634;
import net.minecraft.class_742;
import net.minecraft.class_744;
import net.minecraft.class_746;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2828.class_2831;
import net.minecraft.class_2828.class_5911;
import net.minecraft.class_2848.class_2849;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.impl.event.entity.SwingEvent;
import net.shoreline.client.impl.event.entity.player.PlayerMoveEvent;
import net.shoreline.client.impl.event.network.MountJumpStrengthEvent;
import net.shoreline.client.impl.event.network.MovementPacketsEvent;
import net.shoreline.client.impl.event.network.MovementSlowdownEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.impl.event.network.PushOutOfBlocksEvent;
import net.shoreline.client.impl.event.network.SetCurrentHandEvent;
import net.shoreline.client.impl.event.network.SprintCancelEvent;
import net.shoreline.client.impl.event.network.TickMovementEvent;
import net.shoreline.client.impl.imixin.IClientPlayerEntity;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_746.class})
public abstract class MixinClientPlayerEntity extends class_742 implements Globals, IClientPlayerEntity {
   @Shadow
   @Final
   public class_634 field_3944;
   @Shadow
   public double field_3926;
   @Shadow
   public double field_3940;
   @Shadow
   public double field_3924;
   @Shadow
   public class_744 field_3913;
   @Shadow
   @Final
   protected class_310 field_3937;
   @Shadow
   private boolean field_3936;
   @Shadow
   private float field_3941;
   @Shadow
   private float field_3925;
   @Shadow
   private boolean field_3920;
   @Shadow
   private int field_3923;
   @Shadow
   private boolean field_3927;
   @Unique
   private boolean ticking;

   public MixinClientPlayerEntity() {
      super(class_310.method_1551().field_1687, class_310.method_1551().field_1724.method_7334());
   }

   @Shadow
   protected abstract void method_46742();

   @Shadow
   public abstract boolean method_5715();

   @Shadow
   protected abstract boolean method_3134();

   @Shadow
   protected abstract void method_3148(float var1, float var2);

   @Shadow
   public abstract void method_5773();

   @Shadow
   protected abstract void method_3136();

   @Inject(
      method = {"sendMovementPackets"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookSendMovementPackets(CallbackInfo ci) {
      PlayerUpdateEvent playerUpdateEvent = new PlayerUpdateEvent();
      playerUpdateEvent.setStage(EventStage.PRE);
      Shoreline.EVENT_HANDLER.dispatch(playerUpdateEvent);
      MovementPacketsEvent movementPacketsEvent = new MovementPacketsEvent(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828());
      Shoreline.EVENT_HANDLER.dispatch(movementPacketsEvent);
      double x = movementPacketsEvent.getX();
      double y = movementPacketsEvent.getY();
      double z = movementPacketsEvent.getZ();
      float yaw = movementPacketsEvent.getYaw();
      float pitch = movementPacketsEvent.getPitch();
      boolean ground = movementPacketsEvent.getOnGround();
      if (movementPacketsEvent.isCanceled()) {
         ci.cancel();
         this.method_46742();
         boolean bl = this.method_5715();
         if (bl != this.field_3936) {
            class_2849 mode = bl ? class_2849.field_12979 : class_2849.field_12984;
            this.field_3944.method_52787(new class_2848(this, mode));
            this.field_3936 = bl;
         }

         if (this.method_3134()) {
            double d = x - this.field_3926;
            double e = y - this.field_3940;
            double f = z - this.field_3924;
            double g = (double)(yaw - this.field_3941);
            double h = (double)(pitch - this.field_3925);
            ++this.field_3923;
            boolean bl2 = class_3532.method_41190(d, e, f) > class_3532.method_33723(2.0E-4D) || this.field_3923 >= 20;
            boolean bl3 = g != 0.0D || h != 0.0D;
            if (this.method_5765()) {
               class_243 vec3d = this.method_18798();
               this.field_3944.method_52787(new class_2830(vec3d.field_1352, -999.0D, vec3d.field_1350, this.method_36454(), this.method_36455(), ground));
               bl2 = false;
            } else if (bl2 && bl3) {
               this.field_3944.method_52787(new class_2830(x, y, z, yaw, pitch, ground));
            } else if (bl2) {
               this.field_3944.method_52787(new class_2829(x, y, z, ground));
            } else if (bl3) {
               this.field_3944.method_52787(new class_2831(yaw, pitch, ground));
            } else if (this.field_3920 != this.method_24828()) {
               this.field_3944.method_52787(new class_5911(ground));
            }

            if (bl2) {
               this.field_3926 = x;
               this.field_3940 = y;
               this.field_3924 = z;
               this.field_3923 = 0;
            }

            if (bl3) {
               this.field_3941 = yaw;
               this.field_3925 = pitch;
            }

            this.field_3920 = ground;
            this.field_3927 = (Boolean)this.field_3937.field_1690.method_42423().method_41753();
         }
      }

      playerUpdateEvent.setStage(EventStage.POST);
      Shoreline.EVENT_HANDLER.dispatch(playerUpdateEvent);
   }

   @Inject(
      method = {"tick"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V",
   shift = Shift.BEFORE,
   ordinal = 0
)}
   )
   private void hookTickPre(CallbackInfo ci) {
      PlayerTickEvent playerTickEvent = new PlayerTickEvent();
      Shoreline.EVENT_HANDLER.dispatch(playerTickEvent);
      Managers.ROTATION.onUpdate();
   }

   @Inject(
      method = {"tick"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V",
   ordinal = 0,
   shift = Shift.AFTER
)}
   )
   private void hookTick(CallbackInfo ci) {
      if (!this.ticking) {
         TickMovementEvent tickMovementEvent = new TickMovementEvent();
         Shoreline.EVENT_HANDLER.dispatch(tickMovementEvent);
         if (tickMovementEvent.isCanceled()) {
            for(int i = 0; i < tickMovementEvent.getIterations(); ++i) {
               this.ticking = true;
               this.method_5773();
               this.ticking = false;
               this.method_3136();
            }
         }

      }
   }

   @Inject(
      method = {"tickMovement"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/network/ClientPlayerEntity;ticksLeftToDoubleTapSprint:I",
   shift = Shift.AFTER
)}
   )
   private void hookTickMovementPost(CallbackInfo ci) {
      MovementSlowdownEvent movementUpdateEvent = new MovementSlowdownEvent(this.field_3913);
      Shoreline.EVENT_HANDLER.dispatch(movementUpdateEvent);
   }

   @Inject(
      method = {"move"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookMove(class_1313 movementType, class_243 movement, CallbackInfo ci) {
      PlayerMoveEvent playerMoveEvent = new PlayerMoveEvent(movementType, movement);
      Shoreline.EVENT_HANDLER.dispatch(playerMoveEvent);
      if (playerMoveEvent.isCanceled()) {
         ci.cancel();
         double d = this.method_23317();
         double e = this.method_23321();
         super.method_5784(movementType, playerMoveEvent.getMovement());
         this.method_3148((float)(this.method_23317() - d), (float)(this.method_23321() - e));
      }

   }

   @Inject(
      method = {"pushOutOfBlocks"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPushOutOfBlocks(double x, double z, CallbackInfo ci) {
      PushOutOfBlocksEvent pushOutOfBlocksEvent = new PushOutOfBlocksEvent();
      Shoreline.EVENT_HANDLER.dispatch(pushOutOfBlocksEvent);
      if (pushOutOfBlocksEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"setCurrentHand"},
      at = {@At("HEAD")}
   )
   private void hookSetCurrentHand(class_1268 hand, CallbackInfo ci) {
      SetCurrentHandEvent setCurrentHandEvent = new SetCurrentHandEvent(hand);
      Shoreline.EVENT_HANDLER.dispatch(setCurrentHandEvent);
   }

   @Redirect(
      method = {"tickMovement"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V",
   ordinal = 3
)
   )
   private void hookSetSprinting(class_746 instance, boolean b) {
      SprintCancelEvent sprintEvent = new SprintCancelEvent();
      Shoreline.EVENT_HANDLER.dispatch(sprintEvent);
      if (sprintEvent.isCanceled()) {
         instance.method_5728(true);
      } else {
         instance.method_5728(b);
      }

   }

   @Redirect(
      method = {"updateNausea"},
      at = @At(
   value = "FIELD",
   target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"
)
   )
   private class_437 hookCurrentScreen(class_310 instance) {
      return null;
   }

   @Inject(
      method = {"getMountJumpStrength"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetMountJumpStrength(CallbackInfoReturnable<Float> cir) {
      MountJumpStrengthEvent mountJumpStrengthEvent = new MountJumpStrengthEvent();
      Shoreline.EVENT_HANDLER.dispatch(mountJumpStrengthEvent);
      if (mountJumpStrengthEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(mountJumpStrengthEvent.getJumpStrength());
      }

   }

   @Inject(
      method = {"swingHand"},
      at = {@At("RETURN")}
   )
   private void hookSwingHand(class_1268 hand, CallbackInfo ci) {
      SwingEvent swingEvent = new SwingEvent(hand);
      Shoreline.EVENT_HANDLER.dispatch(swingEvent);
   }

   public float getLastSpoofedYaw() {
      return this.field_3941;
   }

   public float getLastSpoofedPitch() {
      return this.field_3925;
   }
}
