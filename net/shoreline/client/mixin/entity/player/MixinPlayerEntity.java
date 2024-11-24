package net.shoreline.client.mixin.entity.player;

import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1313;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.impl.event.entity.player.PlayerJumpEvent;
import net.shoreline.client.impl.event.entity.player.PushFluidsEvent;
import net.shoreline.client.impl.event.entity.player.TravelEvent;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1657.class})
public abstract class MixinPlayerEntity extends class_1309 implements Globals {
   protected MixinPlayerEntity(class_1299<? extends class_1309> entityType, class_1937 world) {
      super(entityType, world);
   }

   @Shadow
   public abstract void method_6091(class_243 var1);

   @Inject(
      method = {"travel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookTravelHead(class_243 movementInput, CallbackInfo ci) {
      TravelEvent travelEvent = new TravelEvent(movementInput);
      travelEvent.setStage(EventStage.PRE);
      Shoreline.EVENT_HANDLER.dispatch(travelEvent);
      if (travelEvent.isCanceled()) {
         this.method_5784(class_1313.field_6308, this.method_18798());
         ci.cancel();
      }

   }

   @Inject(
      method = {"travel"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void hookTravelTail(class_243 movementInput, CallbackInfo ci) {
      TravelEvent travelEvent = new TravelEvent(movementInput);
      travelEvent.setStage(EventStage.POST);
      Shoreline.EVENT_HANDLER.dispatch(travelEvent);
   }

   @Inject(
      method = {"isPushedByFluids"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookIsPushedByFluids(CallbackInfoReturnable<Boolean> cir) {
      if (this == mc.field_1724) {
         PushFluidsEvent pushFluidsEvent = new PushFluidsEvent();
         Shoreline.EVENT_HANDLER.dispatch(pushFluidsEvent);
         if (pushFluidsEvent.isCanceled()) {
            cir.setReturnValue(false);
            cir.cancel();
         }

      }
   }

   @Inject(
      method = {"jump"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookJumpPre(CallbackInfo ci) {
      if (this == mc.field_1724) {
         PlayerJumpEvent playerJumpEvent = new PlayerJumpEvent();
         playerJumpEvent.setStage(EventStage.PRE);
         Shoreline.EVENT_HANDLER.dispatch(playerJumpEvent);
         if (playerJumpEvent.isCanceled()) {
            ci.cancel();
         }

      }
   }

   @Inject(
      method = {"jump"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void hookJumpPost(CallbackInfo ci) {
      if (this == mc.field_1724) {
         PlayerJumpEvent playerJumpEvent = new PlayerJumpEvent();
         playerJumpEvent.setStage(EventStage.POST);
         Shoreline.EVENT_HANDLER.dispatch(playerJumpEvent);
      }
   }
}
