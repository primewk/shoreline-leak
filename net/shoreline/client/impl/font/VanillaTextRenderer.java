package net.shoreline.client.impl.font;

import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2583;
import net.minecraft.class_289;
import net.minecraft.class_377;
import net.minecraft.class_379;
import net.minecraft.class_382;
import net.minecraft.class_384;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_5223;
import net.minecraft.class_5224;
import net.minecraft.class_5251;
import net.minecraft.class_327.class_6415;
import net.minecraft.class_382.class_328;
import net.minecraft.class_4597.class_4598;
import net.shoreline.client.mixin.accessor.AccessorTextRenderer;
import net.shoreline.client.util.Globals;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

/** @deprecated */
@Deprecated
public class VanillaTextRenderer implements Globals {
   public void drawWithShadow(class_4587 matrices, String text, float x, float y, int color) {
      this.draw(matrices, text, x + 1.0F, y + 1.0F, color, true);
      this.draw(matrices, text, x, y, color, false);
   }

   public void draw(class_4587 matrices, String text, float x, float y, int color, boolean shadow) {
      this.draw(text, x, y, color, matrices.method_23760().method_23761(), shadow);
   }

   private void draw(String text, float x, float y, int color, Matrix4f matrix, boolean shadow) {
      if (text != null) {
         class_4598 immediate = class_4597.method_22991(class_289.method_1348().method_1349());
         this.draw(text, x, y, color, shadow, matrix, immediate, class_6415.field_33993, 0, 15728880);
         immediate.method_22993();
      }
   }

   public void draw(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, class_4597 vertexConsumers, class_6415 layerType, int backgroundColor, int light) {
      this.drawInternal(text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
   }

   private void drawInternal(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, class_4597 vertexConsumers, class_6415 layerType, int backgroundColor, int light) {
      Matrix4f matrix4f = new Matrix4f(matrix);
      this.drawLayer(text, x, y, color, shadow, matrix4f, vertexConsumers, layerType, backgroundColor, light);
   }

   private void drawLayer(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, class_4597 vertexConsumerProvider, class_6415 layerType, int underlineColor, int light) {
      VanillaTextRenderer.Drawer drawer = new VanillaTextRenderer.Drawer(vertexConsumerProvider, x, y, color, shadow, matrix, layerType, light);
      class_5223.method_27479(text, class_2583.field_24360, drawer);
      drawer.drawLayer();
   }

   public static class Drawer implements class_5224 {
      final class_4597 vertexConsumers;
      private final float brightnessMultiplier;
      private final float red;
      private final float green;
      private final float blue;
      private final float alpha;
      private final Matrix4f matrix;
      private final class_6415 layerType;
      private final int light;
      float x;
      float y;
      @Nullable
      private List<class_328> rectangles;

      public Drawer(class_4597 vertexConsumers, float x, float y, int color, boolean shadow, Matrix4f matrix, class_6415 layerType, int light) {
         this.vertexConsumers = vertexConsumers;
         this.x = x;
         this.y = y;
         this.brightnessMultiplier = shadow ? 0.25F : 1.0F;
         this.red = (float)(color >> 16 & 255) / 255.0F * this.brightnessMultiplier;
         this.green = (float)(color >> 8 & 255) / 255.0F * this.brightnessMultiplier;
         this.blue = (float)(color & 255) / 255.0F * this.brightnessMultiplier;
         this.alpha = (float)(color >> 24 & 255) / 255.0F;
         this.matrix = matrix;
         this.layerType = layerType;
         this.light = light;
      }

      public boolean accept(int i, class_2583 style, int j) {
         class_377 fontStorage = ((AccessorTextRenderer)Globals.mc.field_1772).hookGetFontStorage(style.method_27708());
         class_379 glyph = fontStorage.method_2011(j, ((AccessorTextRenderer)Globals.mc.field_1772).hookGetValidateAdvance());
         class_382 glyphRenderer = style.method_10987() && j != 32 ? fontStorage.method_2013(glyph) : fontStorage.method_2014(j);
         boolean bl = style.method_10984();
         float f = this.alpha;
         class_5251 textColor = style.method_10973();
         float l;
         float h;
         float g;
         if (textColor != null) {
            int k = textColor.method_27716();
            g = (float)(k >> 16 & 255) / 255.0F * this.brightnessMultiplier;
            h = (float)(k >> 8 & 255) / 255.0F * this.brightnessMultiplier;
            l = (float)(k & 255) / 255.0F * this.brightnessMultiplier;
         } else {
            g = this.red;
            h = this.green;
            l = this.blue;
         }

         float m;
         if (!(glyphRenderer instanceof class_384)) {
            m = bl ? glyph.method_16799() : 0.0F;
            class_4588 vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.method_24045(this.layerType));
            ((AccessorTextRenderer)Globals.mc.field_1772).hookDrawGlyph(glyphRenderer, bl, style.method_10966(), m, this.x, this.y, this.matrix, vertexConsumer, g, h, l, f, this.light);
         }

         m = glyph.method_16798(bl);
         this.x += m;
         return true;
      }

      public void drawLayer() {
         if (this.rectangles != null) {
            class_382 glyphRenderer = ((AccessorTextRenderer)Globals.mc.field_1772).hookGetFontStorage(class_2583.field_24359).method_22943();
            class_4588 vertexConsumer = this.vertexConsumers.getBuffer(glyphRenderer.method_24045(this.layerType));
            Iterator var3 = this.rectangles.iterator();

            while(var3.hasNext()) {
               class_328 rectangle = (class_328)var3.next();
               glyphRenderer.method_22944(rectangle, this.matrix, vertexConsumer, this.light);
            }
         }

      }
   }
}
