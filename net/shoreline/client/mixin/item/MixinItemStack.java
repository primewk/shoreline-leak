package net.shoreline.client.mixin.item;

import net.minecraft.class_1799;
import net.minecraft.class_1935;
import net.minecraft.class_2487;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.item.DurabilityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1799.class})
public abstract class MixinItemStack {
   @Shadow
   public abstract int method_7919();

   @Shadow
   public abstract class_2487 method_7948();

   @Inject(
      method = {"<init>(Lnet/minecraft/item/ItemConvertible;I)V"},
      at = {@At("RETURN")}
   )
   private void hookInitItem(class_1935 item, int count, CallbackInfo ci) {
      if (Shoreline.EVENT_HANDLER != null) {
         DurabilityEvent durabilityEvent = new DurabilityEvent(this.method_7919());
         Shoreline.EVENT_HANDLER.dispatch(durabilityEvent);
         if (durabilityEvent.isCanceled()) {
            this.method_7948().method_10569("Damage", durabilityEvent.getDamage());
         }

      }
   }

   @Inject(
      method = {"<init>(Lnet/minecraft/nbt/NbtCompound;)V"},
      at = {@At("RETURN")}
   )
   private void hookInitNbt(class_2487 nbt, CallbackInfo ci) {
      if (Shoreline.EVENT_HANDLER != null) {
         DurabilityEvent durabilityEvent = new DurabilityEvent(nbt.method_10550("Damage"));
         Shoreline.EVENT_HANDLER.dispatch(durabilityEvent);
         if (durabilityEvent.isCanceled()) {
            this.method_7948().method_10569("Damage", durabilityEvent.getDamage());
         }

      }
   }
}
