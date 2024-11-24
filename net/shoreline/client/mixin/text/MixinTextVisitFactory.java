package net.shoreline.client.mixin.text;

import net.minecraft.class_124;
import net.minecraft.class_2583;
import net.minecraft.class_5223;
import net.minecraft.class_5224;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.text.TextVisitEvent;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_5223.class})
public abstract class MixinTextVisitFactory implements Globals {
   @Shadow
   private static boolean method_27477(class_2583 style, class_5224 visitor, int index, char c) {
      return false;
   }

   @ModifyArg(
      method = {"visitFormatted(Ljava/lang/String;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
   ordinal = 0
),
      index = 0
   )
   private static String hookVisitFormatted(String text) {
      if (text == null) {
         return "";
      } else if (mc.field_1724 == null) {
         return text;
      } else {
         TextVisitEvent textVisitEvent = new TextVisitEvent(text);
         Shoreline.EVENT_HANDLER.dispatch(textVisitEvent);
         return textVisitEvent.isCanceled() ? textVisitEvent.getText() : text;
      }
   }

   @Inject(
      method = {"visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void hookVisitFormatted$1(String text, int startIndex, class_2583 startingStyle, class_2583 resetStyle, class_5224 visitor, CallbackInfoReturnable<Boolean> cir) {
      cir.cancel();
      int i = text.length();
      class_2583 style = startingStyle;

      for(int j = startIndex; j < i; ++j) {
         char c = text.charAt(j);
         char d;
         if (c == 167) {
            if (j + 1 >= i) {
               break;
            }

            d = text.charAt(j + 1);
            if (d == 's') {
               style = style.method_36139(Modules.COLORS.getRGB());
            } else {
               class_124 formatting = class_124.method_544(d);
               if (formatting != null) {
                  style = formatting == class_124.field_1070 ? resetStyle : style.method_27707(formatting);
               }
            }

            ++j;
         } else if (Character.isHighSurrogate(c)) {
            if (j + 1 >= i) {
               if (!visitor.accept(j, style, 65533)) {
                  cir.setReturnValue(false);
                  return;
               }
               break;
            }

            d = text.charAt(j + 1);
            if (Character.isLowSurrogate(d)) {
               if (!visitor.accept(j, style, Character.toCodePoint(c, d))) {
                  cir.setReturnValue(false);
                  return;
               }

               ++j;
            } else if (!visitor.accept(j, style, 65533)) {
               cir.setReturnValue(false);
               return;
            }
         } else if (!method_27477(style, visitor, j, c)) {
            cir.setReturnValue(false);
            return;
         }
      }

      cir.setReturnValue(true);
   }
}
