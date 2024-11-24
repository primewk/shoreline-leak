package net.shoreline.client.impl.gui.click.component;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.BiConsumer;
import net.minecraft.class_1041;
import net.minecraft.class_1058;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_757;
import net.minecraft.class_8030;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_5253.class_5254;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.gui.click.ClickGuiScreen;
import net.shoreline.client.util.Globals;
import org.joml.Matrix4f;

public abstract class Component implements Drawable, Globals {
   protected float x;
   protected float y;
   protected float width;
   protected float height;

   public abstract void render(class_332 var1, float var2, float var3, float var4);

   protected void rect(class_332 context, int color) {
      this.fill(context, (double)this.x, (double)this.y, (double)this.width, (double)this.height, color);
   }

   protected void rectGradient(class_332 context, int color1, int color2) {
      this.fillGradient(context, (double)this.x, (double)this.y, (double)(this.x + this.width), (double)(this.y + this.height), color1, color2);
   }

   protected void scale(class_332 context, float scale) {
   }

   protected void drawRoundedRect(class_332 context, double x1, double y1, double x2, double y2, int color) {
      this.drawRoundedRect(context, x1, y1, x2, y2, 0.0D, color);
   }

   protected void drawRoundedRect(class_332 context, double x1, double y1, double x2, double y2, double z, int color) {
      this.fill(context, x1, y1, x2, y2, z, color);
   }

   protected void drawCircle(class_332 context, double x, double y, double radius, int color) {
      this.drawCircle(context, x, y, 0.0D, radius, color);
   }

   protected void drawCircle(class_332 context, double x, double y, double z, double radius, int color) {
   }

   protected void drawHorizontalLine(class_332 context, double x1, double x2, double y, int color) {
      if (x2 < x1) {
         double i = x1;
         x1 = x2;
         x2 = i;
      }

      this.fill(context, x1, y, x1 + x2 + 1.0D, y + 1.0D, color);
   }

   protected void drawVerticalLine(class_332 context, double x, double y1, double y2, int color) {
      if (y2 < y1) {
         double i = y1;
         y1 = y2;
         y2 = i;
      }

      this.fill(context, x, y1 + 1.0D, x + 1.0D, y1 + y2, color);
   }

   public void fill(class_332 context, double x1, double y1, double x2, double y2, int color) {
      this.fill(context, x1, y1, x2, y2, 0.0D, color);
   }

   public void fill(class_332 context, double x1, double y1, double x2, double y2, double z, int color) {
      x2 += x1;
      y2 += y1;
      Matrix4f matrix4f = context.method_51448().method_23760().method_23761();
      double i;
      if (x1 < x2) {
         i = x1;
         x1 = x2;
         x2 = i;
      }

      if (y1 < y2) {
         i = y1;
         y1 = y2;
         y2 = i;
      }

      float f = (float)class_5254.method_27762(color) / 255.0F;
      float g = (float)class_5254.method_27765(color) / 255.0F;
      float h = (float)class_5254.method_27766(color) / 255.0F;
      float j = (float)class_5254.method_27767(color) / 255.0F;
      class_287 buffer = RenderManager.BUFFER;
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      buffer.method_1328(class_5596.field_27382, class_290.field_1576);
      buffer.method_22918(matrix4f, (float)x1, (float)y1, (float)z).method_22915(g, h, j, f).method_1344();
      buffer.method_22918(matrix4f, (float)x1, (float)y2, (float)z).method_22915(g, h, j, f).method_1344();
      buffer.method_22918(matrix4f, (float)x2, (float)y2, (float)z).method_22915(g, h, j, f).method_1344();
      buffer.method_22918(matrix4f, (float)x2, (float)y1, (float)z).method_22915(g, h, j, f).method_1344();
      class_286.method_43433(buffer.method_1326());
      RenderSystem.disableBlend();
   }

   protected void fillGradient(class_332 context, double startX, double startY, double endX, double endY, int colorStart, int colorEnd) {
      this.fillGradient(context, startX, startY, endX, endY, colorStart, colorEnd, 0);
   }

   protected void fillGradient(class_332 context, double startX, double startY, double endX, double endY, int colorStart, int colorEnd, int z) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_289 tessellator = class_289.method_1348();
      class_287 buffer = tessellator.method_1349();
      buffer.method_1328(class_5596.field_27382, class_290.field_1576);
      this.fillGradient(context.method_51448().method_23760().method_23761(), buffer, startX, startY, endX, endY, (double)z, colorStart, colorEnd);
      tessellator.method_1350();
      RenderSystem.disableBlend();
   }

   protected void fillGradient(Matrix4f matrix, class_287 builder, double startX, double startY, double endX, double endY, double z, int colorStart, int colorEnd) {
      float f = (float)class_5254.method_27762(colorStart) / 255.0F;
      float g = (float)class_5254.method_27765(colorStart) / 255.0F;
      float h = (float)class_5254.method_27766(colorStart) / 255.0F;
      float i = (float)class_5254.method_27767(colorStart) / 255.0F;
      float j = (float)class_5254.method_27762(colorEnd) / 255.0F;
      float k = (float)class_5254.method_27765(colorEnd) / 255.0F;
      float l = (float)class_5254.method_27766(colorEnd) / 255.0F;
      float m = (float)class_5254.method_27767(colorEnd) / 255.0F;
      builder.method_22918(matrix, (float)startX, (float)startY, (float)z).method_22915(k, l, m, j).method_1344();
      builder.method_22918(matrix, (float)startX, (float)endY, (float)z).method_22915(k, l, m, j).method_1344();
      builder.method_22918(matrix, (float)endX, (float)endY, (float)z).method_22915(g, h, i, f).method_1344();
      builder.method_22918(matrix, (float)endX, (float)startY, (float)z).method_22915(g, h, i, f).method_1344();
   }

   protected void fillGradientQuad(class_332 context, float x1, float y1, float x2, float y2, int startColor, int endColor, boolean sideways) {
      float f = (float)(startColor >> 24 & 255) / 255.0F;
      float f1 = (float)(startColor >> 16 & 255) / 255.0F;
      float f2 = (float)(startColor >> 8 & 255) / 255.0F;
      float f3 = (float)(startColor & 255) / 255.0F;
      float f4 = (float)(endColor >> 24 & 255) / 255.0F;
      float f5 = (float)(endColor >> 16 & 255) / 255.0F;
      float f6 = (float)(endColor >> 8 & 255) / 255.0F;
      float f7 = (float)(endColor & 255) / 255.0F;
      class_287 bufferBuilder = class_289.method_1348().method_1349();
      Matrix4f posMatrix = context.method_51448().method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(class_757::method_34540);
      bufferBuilder.method_1328(class_5596.field_27382, class_290.field_1576);
      if (sideways) {
         bufferBuilder.method_22918(posMatrix, x1, y1, 0.0F).method_22915(f1, f2, f3, f).method_1344();
         bufferBuilder.method_22918(posMatrix, x1, y2, 0.0F).method_22915(f1, f2, f3, f).method_1344();
         bufferBuilder.method_22918(posMatrix, x2, y2, 0.0F).method_22915(f5, f6, f7, f4).method_1344();
         bufferBuilder.method_22918(posMatrix, x2, y1, 0.0F).method_22915(f5, f6, f7, f4).method_1344();
      } else {
         bufferBuilder.method_22918(posMatrix, x2, y1, 0.0F).method_22915(f1, f2, f3, f).method_1344();
         bufferBuilder.method_22918(posMatrix, x1, y1, 0.0F).method_22915(f1, f2, f3, f).method_1344();
         bufferBuilder.method_22918(posMatrix, x1, y2, 0.0F).method_22915(f5, f6, f7, f4).method_1344();
         bufferBuilder.method_22918(posMatrix, x2, y2, 0.0F).method_22915(f5, f6, f7, f4).method_1344();
      }

      class_286.method_43433(bufferBuilder.method_1326());
      RenderSystem.disableBlend();
   }

   public void drawWithOutline(int x, int y, BiConsumer<Integer, Integer> renderAction) {
      RenderSystem.blendFuncSeparate(class_4535.ZERO, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA);
      renderAction.accept(x + 1, y);
      renderAction.accept(x - 1, y);
      renderAction.accept(x, y + 1);
      renderAction.accept(x, y - 1);
      RenderSystem.defaultBlendFunc();
      renderAction.accept(x, y);
   }

   public void drawSprite(class_332 context, int x, int y, int z, int width, int height, class_1058 sprite) {
      this.drawTexturedQuad(context.method_51448().method_23760().method_23761(), x, x + width, y, y + height, z, sprite.method_4594(), sprite.method_4577(), sprite.method_4593(), sprite.method_4575());
   }

   public void drawSprite(class_332 context, int x, int y, int z, int width, int height, class_1058 sprite, float red, float green, float blue, float alpha) {
      this.drawTexturedQuad(context.method_51448().method_23760().method_23761(), x, x + width, y, y + height, z, sprite.method_4594(), sprite.method_4577(), sprite.method_4593(), sprite.method_4575(), red, green, blue, alpha);
   }

   public void drawBorder(class_332 context, int x, int y, int width, int height, int color) {
      this.fill(context, (double)x, (double)y, (double)(x + width), (double)(y + 1), color);
      this.fill(context, (double)x, (double)(y + height - 1), (double)(x + width), (double)(y + height), color);
      this.fill(context, (double)x, (double)(y + 1), (double)(x + 1), (double)(y + height - 1), color);
      this.fill(context, (double)(x + width - 1), (double)(y + 1), (double)(x + width), (double)(y + height - 1), color);
   }

   public void drawTexture(class_332 context, int x, int y, int u, int v, int width, int height) {
      this.drawTexture(context, x, y, 0, (float)u, (float)v, width, height, 256, 256);
   }

   public void drawTexture(class_332 context, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight) {
      this.drawTexture(context, x, x + width, y, y + height, z, width, height, u, v, textureWidth, textureHeight);
   }

   public void drawTexture(class_332 context, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
      this.drawTexture(context, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
   }

   public void drawTexture(class_332 context, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
      this.drawTexture(context, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
   }

   private void drawTexture(class_332 context, int x0, int x1, int y0, int y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
      this.drawTexturedQuad(context.method_51448().method_23760().method_23761(), x0, x1, y0, y1, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
   }

   private void drawTexturedQuad(Matrix4f matrix, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
      RenderSystem.setShader(class_757::method_34542);
      class_287 buffer = class_289.method_1348().method_1349();
      buffer.method_1328(class_5596.field_27382, class_290.field_1585);
      buffer.method_22918(matrix, (float)x0, (float)y0, (float)z).method_22913(u0, v0).method_1344();
      buffer.method_22918(matrix, (float)x0, (float)y1, (float)z).method_22913(u0, v1).method_1344();
      buffer.method_22918(matrix, (float)x1, (float)y1, (float)z).method_22913(u1, v1).method_1344();
      buffer.method_22918(matrix, (float)x1, (float)y0, (float)z).method_22913(u1, v0).method_1344();
      class_286.method_43433(buffer.method_1326());
   }

   private void drawTexturedQuad(Matrix4f matrix, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1, float red, float green, float blue, float alpha) {
      RenderSystem.setShader(class_757::method_34541);
      RenderSystem.enableBlend();
      class_287 buffer = class_289.method_1348().method_1349();
      buffer.method_1328(class_5596.field_27382, class_290.field_20887);
      buffer.method_22918(matrix, (float)x0, (float)y0, (float)z).method_22915(red, green, blue, alpha).method_22913(u0, v0).method_1344();
      buffer.method_22918(matrix, (float)x0, (float)y1, (float)z).method_22915(red, green, blue, alpha).method_22913(u0, v1).method_1344();
      buffer.method_22918(matrix, (float)x1, (float)y1, (float)z).method_22915(red, green, blue, alpha).method_22913(u1, v1).method_1344();
      buffer.method_22918(matrix, (float)x1, (float)y0, (float)z).method_22915(red, green, blue, alpha).method_22913(u1, v0).method_1344();
      class_286.method_43433(buffer.method_1326());
      RenderSystem.disableBlend();
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getHeight() {
      return this.height;
   }

   public void setHeight(float height) {
      this.height = height;
   }

   public float getWidth() {
      return this.width;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public boolean isWithin(double xval, double yval) {
      return this.isWithin((float)xval, (float)yval);
   }

   public boolean isWithin(float xval, float yval) {
      return this.isMouseOver((double)xval, (double)yval, (double)this.x, (double)this.y, (double)this.width, (double)this.height);
   }

   public boolean isMouseOver(double mx, double my, double x1, double y1, double x2, double y2) {
      return mx >= x1 && mx <= x1 + x2 && my >= y1 && my <= y1 + y2;
   }

   public void setPos(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public void setDimensions(float width, float height) {
      this.setWidth(width);
      this.setHeight(height);
   }

   public void enableScissor(int x1, int y1, int x2, int y2) {
      this.setScissor(ClickGuiScreen.SCISSOR_STACK.push(new class_8030(x1, y1, x2 - x1, y2 - y1)));
   }

   public void disableScissor() {
      this.setScissor(ClickGuiScreen.SCISSOR_STACK.pop());
   }

   private void setScissor(class_8030 rect) {
      if (rect != null) {
         class_1041 window = mc.method_22683();
         int i = window.method_4506();
         double d = window.method_4495();
         double e = (double)rect.method_49620() * d;
         double f = (double)i - (double)rect.method_49619() * d;
         double g = (double)rect.comp_1196() * d;
         double h = (double)rect.comp_1197() * d;
         RenderSystem.enableScissor((int)e, (int)f, Math.max(0, (int)g), Math.max(0, (int)h));
      } else {
         RenderSystem.disableScissor();
      }

   }
}
