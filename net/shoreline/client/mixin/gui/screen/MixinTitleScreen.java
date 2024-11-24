package net.shoreline.client.mixin.gui.screen;

import java.util.Objects;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4185;
import net.minecraft.class_4189;
import net.minecraft.class_426;
import net.minecraft.class_429;
import net.minecraft.class_437;
import net.minecraft.class_4399;
import net.minecraft.class_442;
import net.minecraft.class_7077;
import net.minecraft.class_7919;
import net.minecraft.class_8082;
import net.minecraft.class_8219;
import net.minecraft.class_8519;
import net.minecraft.class_8662;
import net.shoreline.client.impl.gui.account.AccountSelectorScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_442.class})
public abstract class MixinTitleScreen extends class_437 {
   @Shadow
   @Nullable
   private class_8519 field_2586;
   @Shadow
   @Final
   public static class_2561 field_32271;
   @Shadow
   @Nullable
   private class_4399 field_2592;
   @Shadow
   private long field_17772;
   @Shadow
   @Final
   private boolean field_18222;

   @Shadow
   protected abstract void method_2251(int var1, int var2);

   @Shadow
   protected abstract void method_2249(int var1, int var2);

   @Shadow
   protected abstract boolean method_2253();

   public MixinTitleScreen(class_2561 title) {
      super(title);
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   public void hookRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo info) {
      float f = this.field_18222 ? (float)(class_156.method_658() - this.field_17772) / 1000.0F : 1.0F;
      float g = this.field_18222 ? class_3532.method_15363(f - 1.0F, 0.0F, 1.0F) : 1.0F;
      int i = class_3532.method_15386(g * 255.0F) << 24;
      if ((i & -67108864) != 0) {
         class_327 var10001 = this.field_22787.field_1772;
         int var10004 = this.field_22790;
         Objects.requireNonNull(this.field_22787.field_1772);
         context.method_25303(var10001, "Shoreline 1.0 (dev-7-72d9e9a)", 2, var10004 - 9 * 2 - 2, 16777215 | i);
      }
   }

   @Inject(
      method = {"init"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookInit(CallbackInfo ci) {
      ci.cancel();
      if (this.field_2586 == null) {
         this.field_2586 = this.field_22787.method_18095().method_18174();
      }

      int i = this.field_22793.method_27525(field_32271);
      int j = this.field_22789 - i - 2;
      int k = true;
      int l = this.field_22790 / 4 + 48;
      if (this.field_22787.method_1530()) {
         this.method_2251(l, 24);
      } else {
         this.method_2249(l, 24);
      }

      class_8662 textIconButtonWidget = (class_8662)this.method_37063(class_8082.method_48592(20, (button) -> {
         this.field_22787.method_1507(new class_426(this, this.field_22787.field_1690, this.field_22787.method_1526()));
      }, true));
      textIconButtonWidget.method_48229(this.field_22789 / 2 - 124, l + 72 + 24);
      this.method_37063(class_4185.method_46430(class_2561.method_43471("menu.options"), (button) -> {
         this.field_22787.method_1507(new class_429(this, this.field_22787.field_1690));
      }).method_46434(this.field_22789 / 2 - 100, l + 72 + 24, 98, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43471("menu.quit"), (button) -> {
         this.field_22787.method_1592();
      }).method_46434(this.field_22789 / 2 + 2, l + 72 + 24, 98, 20).method_46431());
      class_8662 textIconButtonWidget2 = (class_8662)this.method_37063(class_8082.method_48594(20, (button) -> {
         this.field_22787.method_1507(new class_4189(this, this.field_22787.field_1690));
      }, true));
      textIconButtonWidget2.method_48229(this.field_22789 / 2 + 104, l + 72 + 24);
      this.method_37063(new class_7077(j, this.field_22790 - 10, i, 10, field_32271, (button) -> {
         this.field_22787.method_1507(new class_8219(this));
      }, this.field_22793));
      if (this.field_2592 == null) {
         this.field_2592 = new class_4399();
      }

      if (this.method_2253()) {
         this.field_2592.method_25423(this.field_22787, this.field_22789, this.field_22790);
      }

   }

   @Inject(
      method = {"initWidgetsNormal"},
      at = {@At(
   target = "Lnet/minecraft/client/gui/screen/TitleScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;",
   value = "INVOKE",
   shift = Shift.AFTER,
   ordinal = 2
)}
   )
   public void hookInit(int y, int spacingY, CallbackInfo ci) {
      class_4185 widget = class_4185.method_46430(class_2561.method_30163("Account Manager"), (action) -> {
         this.field_22787.method_1507(new AccountSelectorScreen((class_437)this));
      }).method_46434(this.field_22789 / 2 - 100, y + spacingY * 3, 200, 20).method_46436(class_7919.method_47407(class_2561.method_30163("Allows you to switch your in-game account"))).method_46431();
      widget.field_22763 = true;
      this.method_37063(widget);
   }
}
