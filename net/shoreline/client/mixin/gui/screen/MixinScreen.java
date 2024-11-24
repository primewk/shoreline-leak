package net.shoreline.client.mixin.gui.screen;

import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_437;
import net.minecraft.class_6379;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_437.class})
public abstract class MixinScreen {
   @Shadow
   public int field_22789;
   @Shadow
   public int field_22790;
   @Shadow
   @Final
   private List<class_4068> field_33816;
   @Shadow
   @Final
   protected class_2561 field_22785;
   @Shadow
   @Nullable
   protected class_310 field_22787;

   @Shadow
   protected abstract <T extends class_364 & class_4068 & class_6379> T method_37063(T var1);

   @Shadow
   public void method_25393() {
   }

   @Unique
   public List<class_4068> getDrawables() {
      return this.field_33816;
   }
}
