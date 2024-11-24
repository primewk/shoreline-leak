package net.shoreline.client.impl.font;

import java.awt.Font;
import net.minecraft.class_4597;
import net.shoreline.client.api.font.FontRenderer;
import net.shoreline.client.api.font.Glyph;
import net.shoreline.client.api.font.GlyphVisitor;
import org.joml.Matrix4f;

public final class AWTFontRenderer implements FontRenderer {
   private final Font font;

   public AWTFontRenderer(Font font, float size) {
      this.font = font.deriveFont(size);
   }

   public void draw(Matrix4f matrix4f, String text, double x, double y, int color, boolean shadow) {
      char[] chars = text.toCharArray();
   }

   public void draw(Matrix4f matrix4f, GlyphVisitor visitor, String text, double x, double y, int color, boolean shadow) {
   }

   public void draw(Matrix4f matrix4f, GlyphVisitor visitor, class_4597 vertexConsumers, String text, double x, double y, int color, boolean shadow) {
   }

   public int drawGlyph(FontRenderer fontRenderer, char c, double x, double y, int color, boolean shadow) {
      return 0;
   }

   public int getStringWidth(String text) {
      return 0;
   }

   public Glyph getGlyph(char c) {
      return null;
   }

   public Glyph[] getGlyphMap() {
      return new Glyph[0];
   }
}
