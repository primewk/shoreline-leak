package net.shoreline.client.mixin.entity;

import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.entity.ConsumeItemEvent;
import net.shoreline.client.impl.event.entity.JumpDelayEvent;
import net.shoreline.client.impl.event.entity.JumpRotationEvent;
import net.shoreline.client.impl.event.entity.LevitationEvent;
import net.shoreline.client.impl.event.entity.StatusEffectEvent;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1309.class})
public abstract class MixinLivingEntity extends MixinEntity implements Globals {
   @Shadow
   protected class_1799 field_6277;
   @Shadow
   public int field_6213;
   @Shadow
   private int field_6228;

   @Shadow
   public abstract boolean method_6059(class_1291 var1);

   @Shadow
   public abstract float method_5705(float var1);

   @Shadow
   protected abstract float method_6106();

   @Shadow
   public abstract boolean method_29504();

   @Inject(
      method = {"jump"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookJump$getYaw(CallbackInfo ci) {
      if ((class_1309)this == mc.field_1724) {
         JumpRotationEvent event = new JumpRotationEvent();
         Shoreline.EVENT_HANDLER.dispatch(event);
         if (event.isCanceled()) {
            ci.cancel();
            class_243 vec3d = this.method_18798();
            this.method_18799(new class_243(vec3d.field_1352, (double)this.method_6106(), vec3d.field_1350));
            if (this.method_5624()) {
               float f = event.getYaw() * 0.017453292F;
               this.method_18799(this.method_18798().method_1031((double)(-class_3532.method_15374(f) * 0.2F), 0.0D, (double)(class_3532.method_15362(f) * 0.2F)));
            }

            this.field_6007 = true;
         }

      }
   }

   @Redirect(
      method = {"travel"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"
)
   )
   private boolean hookHasStatusEffect(class_1309 instance, class_1291 effect) {
      if (!instance.equals(mc.field_1724)) {
         return this.method_6059(effect);
      } else {
         LevitationEvent levitationEvent = new LevitationEvent();
         Shoreline.EVENT_HANDLER.dispatch(levitationEvent);
         return !levitationEvent.isCanceled() && this.method_6059(effect);
      }
   }

   @Inject(
      method = {"consumeItem"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/item/ItemStack;finishUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;",
   shift = Shift.AFTER
)}
   )
   private void hookConsumeItem(CallbackInfo ci) {
      if (this == mc.field_1724) {
         ConsumeItemEvent consumeItemEvent = new ConsumeItemEvent(this.field_6277);
         Shoreline.EVENT_HANDLER.dispatch(consumeItemEvent);
      }
   }

   @Inject(
      method = {"tickMovement"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookTickMovement(CallbackInfo ci) {
      JumpDelayEvent jumpDelayEvent = new JumpDelayEvent();
      Shoreline.EVENT_HANDLER.dispatch(jumpDelayEvent);
      if (jumpDelayEvent.isCanceled()) {
         this.field_6228 = 0;
      }

   }

   @Inject(
      method = {"onStatusEffectApplied"},
      at = {@At("HEAD")}
   )
   private void hookAddStatusEffect(class_1293 effect, class_1297 source, CallbackInfo ci) {
      if (this == mc.field_1724) {
         StatusEffectEvent.Add statusEffectEvent = new StatusEffectEvent.Add(effect);
         Shoreline.EVENT_HANDLER.dispatch(statusEffectEvent);
      }
   }

   @Inject(
      method = {"onStatusEffectRemoved"},
      at = {@At("HEAD")}
   )
   private void hookRemoveStatusEffect(class_1293 effect, CallbackInfo ci) {
      if (this == mc.field_1724) {
         StatusEffectEvent.Remove statusEffectEvent = new StatusEffectEvent.Remove(effect);
         Shoreline.EVENT_HANDLER.dispatch(statusEffectEvent);
      }
   }
}
