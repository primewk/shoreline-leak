package net.shoreline.client.mixin.accessor;

import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_377;
import net.minecraft.class_382;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_327.class_6415;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_327.class})
public interface AccessorTextRenderer {
   @Accessor("validateAdvance")
   boolean hookGetValidateAdvance();

   @Invoker("getFontStorage")
   class_377 hookGetFontStorage(class_2960 var1);

   @Invoker("drawGlyph")
   void hookDrawGlyph(class_382 var1, boolean var2, boolean var3, float var4, float var5, float var6, Matrix4f var7, class_4588 var8, float var9, float var10, float var11, float var12, int var13);

   @Invoker("drawLayer")
   float hookDrawLayer(String var1, float var2, float var3, int var4, boolean var5, Matrix4f var6, class_4597 var7, class_6415 var8, int var9, int var10);
}
