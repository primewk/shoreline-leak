package net.shoreline.client.mixin.sound;

import java.util.Map;
import net.minecraft.class_1113;
import net.minecraft.class_1140;
import net.minecraft.class_4235.class_4236;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1140.class})
public class MixinSoundSystem {
   @Shadow
   private boolean field_5563;
   @Shadow
   private int field_5550;
   @Shadow
   @Final
   private Map<class_1113, Integer> field_18952;
   @Shadow
   @Final
   private Map<class_1113, class_4236> field_18950;

   @Inject(
      method = {"isPlaying"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isPlaying(class_1113 sound, CallbackInfoReturnable<Boolean> cir) {
      cir.cancel();
      Integer i = (Integer)this.field_18952.get(sound);
      if (!this.field_5563) {
         cir.setReturnValue(false);
      }

      if (i != null && i <= this.field_5550) {
         cir.setReturnValue(true);
      }

      cir.setReturnValue(this.field_18950.containsKey(sound));
   }
}
