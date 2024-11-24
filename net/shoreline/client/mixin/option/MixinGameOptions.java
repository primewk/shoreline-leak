package net.shoreline.client.mixin.option;

import com.mojang.serialization.Codec;
import java.io.File;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_7172;
import net.minecraft.class_7172.class_7174;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_315.class})
public class MixinGameOptions {
   @Mutable
   @Shadow
   @Final
   private class_7172<Integer> field_1826;

   @Inject(
      method = {"<init>"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/option/GameOptions;load()V",
   shift = Shift.BEFORE
)}
   )
   private void hookInit(class_310 client, File optionsFile, CallbackInfo ci) {
      this.field_1826 = new class_7172("options.fov", class_7172.method_42399(), (optionText, value) -> {
         class_2561 var10000;
         switch(value) {
         case 70:
            var10000 = class_315.method_41783(optionText, class_2561.method_43471("options.fov.min"));
            break;
         case 110:
            var10000 = class_315.method_41783(optionText, class_2561.method_43471("options.fov.max"));
            break;
         default:
            var10000 = class_315.method_41782(optionText, value);
         }

         return var10000;
      }, new class_7174(30, 180), Codec.DOUBLE.xmap((value) -> {
         return (int)(value * 40.0D + 70.0D);
      }, (value) -> {
         return ((double)value - 70.0D) / 40.0D;
      }), 70, (value) -> {
         class_310.method_1551().field_1769.method_3292();
      });
   }
}
