package net.shoreline.client.mixin.item;

import net.minecraft.class_1268;
import net.minecraft.class_1271;
import net.minecraft.class_1309;
import net.minecraft.class_1313;
import net.minecraft.class_1657;
import net.minecraft.class_1685;
import net.minecraft.class_1799;
import net.minecraft.class_1835;
import net.minecraft.class_1890;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3468;
import net.minecraft.class_3532;
import net.minecraft.class_1665.class_1666;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.item.TridentPullbackEvent;
import net.shoreline.client.impl.event.item.TridentWaterEvent;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1835.class})
public abstract class MixinTridentItem implements Globals {
   @Shadow
   public abstract int method_7881(class_1799 var1);

   @Inject(
      method = {"use"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookUse(class_1937 world, class_1657 user, class_1268 hand, CallbackInfoReturnable<class_1271<class_1799>> cir) {
      TridentWaterEvent tridentWaterEvent = new TridentWaterEvent();
      Shoreline.EVENT_HANDLER.dispatch(tridentWaterEvent);
      if (tridentWaterEvent.isCanceled()) {
         cir.cancel();
         class_1799 itemStack = user.method_5998(hand);
         if (itemStack.method_7919() >= itemStack.method_7936() - 1) {
            cir.setReturnValue(class_1271.method_22431(itemStack));
            return;
         }

         user.method_6019(hand);
         cir.setReturnValue(class_1271.method_22428(itemStack));
      }

   }

   @Inject(
      method = {"onStoppedUsing"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookOnStoppedUsing(class_1799 stack, class_1937 world, class_1309 user, int remainingUseTicks, CallbackInfo ci) {
      if (user instanceof class_1657) {
         int i = this.method_7881(stack) - remainingUseTicks;
         TridentPullbackEvent tridentPullbackEvent = new TridentPullbackEvent();
         Shoreline.EVENT_HANDLER.dispatch(tridentPullbackEvent);
         if (tridentPullbackEvent.isCanceled() || i >= 10) {
            TridentWaterEvent tridentWaterEvent = new TridentWaterEvent();
            Shoreline.EVENT_HANDLER.dispatch(tridentWaterEvent);
            if (tridentWaterEvent.isCanceled()) {
               ci.cancel();
               class_1657 playerEntity = (class_1657)user;
               int j = class_1890.method_8202(stack);
               if (!mc.field_1687.field_9236) {
                  stack.method_7956(1, playerEntity, (p) -> {
                     p.method_20236(user.method_6058());
                  });
                  if (j == 0) {
                     class_1685 tridentEntity = new class_1685(world, playerEntity, stack);
                     tridentEntity.method_24919(playerEntity, playerEntity.method_36455(), playerEntity.method_36454(), 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
                     if (playerEntity.method_31549().field_7477) {
                        tridentEntity.field_7572 = class_1666.field_7594;
                     }

                     world.method_8649(tridentEntity);
                     world.method_43129((class_1657)null, tridentEntity, class_3417.field_15001, class_3419.field_15248, 1.0F, 1.0F);
                     if (!playerEntity.method_31549().field_7477) {
                        playerEntity.method_31548().method_7378(stack);
                     }
                  }
               }

               playerEntity.method_7259(class_3468.field_15372.method_14956((class_1835)this));
               if (j > 0) {
                  float f = playerEntity.method_36454();
                  float g = playerEntity.method_36455();
                  float h = -class_3532.method_15374(f * 0.017453292F) * class_3532.method_15362(g * 0.017453292F);
                  float k = -class_3532.method_15374(g * 0.017453292F);
                  float l = class_3532.method_15362(f * 0.017453292F) * class_3532.method_15362(g * 0.017453292F);
                  float m = class_3532.method_15355(h * h + k * k + l * l);
                  float n = 3.0F * ((1.0F + (float)j) / 4.0F);
                  playerEntity.method_5762((double)(h * (n / m)), (double)(k * (n / m)), (double)(l * (n / m)));
                  playerEntity.method_40126(20);
                  if (playerEntity.method_24828()) {
                     float o = 1.1999999F;
                     playerEntity.method_5784(class_1313.field_6308, new class_243(0.0D, 1.1999999284744263D, 0.0D));
                  }

                  class_3414 soundEvent = j >= 3 ? class_3417.field_14717 : (j == 2 ? class_3417.field_14806 : class_3417.field_14606);
                  world.method_43129((class_1657)null, playerEntity, soundEvent, class_3419.field_15248, 1.0F, 1.0F);
               }
            }

         }
      }
   }
}
