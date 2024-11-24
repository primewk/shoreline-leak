package net.shoreline.client.mixin.network;

import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1271;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1934;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import net.minecraft.class_2886;
import net.minecraft.class_3965;
import net.minecraft.class_636;
import net.minecraft.class_638;
import net.minecraft.class_7204;
import net.minecraft.class_746;
import net.minecraft.class_2828.class_2830;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.network.AttackBlockEvent;
import net.shoreline.client.impl.event.network.BreakBlockEvent;
import net.shoreline.client.impl.event.network.InteractBlockEvent;
import net.shoreline.client.impl.event.network.ReachEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.Globals;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_636.class})
public abstract class MixinClientPlayerInteractionManager implements Globals {
   @Shadow
   private class_1934 field_3719;

   @Shadow
   protected abstract void method_2911();

   @Shadow
   protected abstract void method_41931(class_638 var1, class_7204 var2);

   @Inject(
      method = {"attackBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookAttackBlock(class_2338 pos, class_2350 direction, CallbackInfoReturnable<Boolean> cir) {
      class_2680 state = mc.field_1687.method_8320(pos);
      AttackBlockEvent attackBlockEvent = new AttackBlockEvent(pos, state, direction);
      Shoreline.EVENT_HANDLER.dispatch(attackBlockEvent);
      if (attackBlockEvent.isCanceled()) {
         cir.cancel();
      }

   }

   @Inject(
      method = {"getReachDistance"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetReachDistance(CallbackInfoReturnable<Float> cir) {
      ReachEvent reachEvent = new ReachEvent();
      Shoreline.EVENT_HANDLER.dispatch(reachEvent);
      if (reachEvent.isCanceled()) {
         cir.cancel();
         float reach = this.field_3719.method_8386() ? 5.0F : 4.5F;
         cir.setReturnValue(reach + reachEvent.getReach());
      }

   }

   @Inject(
      method = {"interactBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookInteractBlock(class_746 player, class_1268 hand, class_3965 hitResult, CallbackInfoReturnable<class_1269> cir) {
      InteractBlockEvent interactBlockEvent = new InteractBlockEvent(player, hand, hitResult);
      Shoreline.EVENT_HANDLER.dispatch(interactBlockEvent);
      if (interactBlockEvent.isCanceled()) {
         cir.setReturnValue(class_1269.field_5812);
         cir.cancel();
      }

   }

   @Inject(
      method = {"breakBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookBreakBlock(class_2338 pos, CallbackInfoReturnable<Boolean> cir) {
      BreakBlockEvent breakBlockEvent = new BreakBlockEvent(pos);
      Shoreline.EVENT_HANDLER.dispatch(breakBlockEvent);
      if (breakBlockEvent.isCanceled()) {
         cir.setReturnValue(false);
         cir.cancel();
      }

   }

   @Inject(
      method = {"interactItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void hookInteractItem(class_1657 player, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
      cir.cancel();
      if (this.field_3719 == class_1934.field_9219) {
         cir.setReturnValue(class_1269.field_5811);
      }

      this.method_2911();
      float yaw = mc.field_1724.method_36454();
      float pitch = mc.field_1724.method_36455();
      if (Managers.ROTATION.isRotating()) {
         yaw = Managers.ROTATION.getRotationYaw();
         pitch = Managers.ROTATION.getRotationPitch();
      }

      if (!Modules.NO_SLOW.isEnabled() || !Modules.NO_SLOW.getStrafeFix()) {
         mc.field_1724.field_3944.method_52787(new class_2830(player.method_23317(), player.method_23318(), player.method_23321(), yaw, pitch, player.method_24828()));
      }

      MutableObject mutableObject = new MutableObject();
      this.method_41931(mc.field_1687, (sequence) -> {
         class_2886 playerInteractItemC2SPacket = new class_2886(hand, sequence);
         class_1799 itemStack = player.method_5998(hand);
         if (player.method_7357().method_7904(itemStack.method_7909())) {
            mutableObject.setValue(class_1269.field_5811);
            return playerInteractItemC2SPacket;
         } else {
            class_1271<class_1799> typedActionResult = itemStack.method_7913(mc.field_1687, player, hand);
            class_1799 itemStack2 = (class_1799)typedActionResult.method_5466();
            if (itemStack2 != itemStack) {
               player.method_6122(hand, itemStack2);
            }

            mutableObject.setValue(typedActionResult.method_5467());
            return playerInteractItemC2SPacket;
         }
      });
      cir.setReturnValue((class_1269)mutableObject.getValue());
   }

   @Redirect(
      method = {"interactBlockInternal"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/ClientPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"
)
   )
   private class_1799 hookRedirectInteractBlockInternal$getStackInHand(class_746 entity, class_1268 hand) {
      if (hand.equals(class_1268.field_5810)) {
         return entity.method_5998(hand);
      } else {
         return Managers.INVENTORY.isDesynced() ? Managers.INVENTORY.getServerItem() : entity.method_5998(class_1268.field_5808);
      }
   }

   @Redirect(
      method = {"interactBlockInternal"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/item/ItemStack;isEmpty()Z",
   ordinal = 0
)
   )
   private boolean hookRedirectInteractBlockInternal$getMainHandStack(class_1799 instance) {
      return Managers.INVENTORY.isDesynced() ? Managers.INVENTORY.getServerItem().method_7960() : instance.method_7960();
   }
}
