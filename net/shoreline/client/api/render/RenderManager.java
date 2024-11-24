package net.shoreline.client.api.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_4597.class_4598;
import net.minecraft.class_5253.class_5254;
import net.shoreline.client.init.Fonts;
import net.shoreline.client.init.Modules;
import net.shoreline.client.mixin.accessor.AccessorWorldRenderer;
import net.shoreline.client.util.Globals;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class RenderManager implements Globals {
   public static final class_289 TESSELLATOR = RenderSystem.renderThreadTesselator();
   public static final class_287 BUFFER;

   public static void post(Runnable callback) {
      RenderBuffers.post(callback);
   }

   public static void renderBox(class_4587 matrices, class_2338 p, int color) {
      renderBox(matrices, new class_238(p), color);
   }

   public static void renderBox(class_4587 matrices, class_238 box, int color) {
      if (isFrustumVisible(box)) {
         matrices.method_22903();
         drawBox(matrices, box, color);
         matrices.method_22909();
      }
   }

   public static void drawBox(class_4587 matrices, class_238 box, int color) {
      drawBox(matrices, box.field_1323, box.field_1322, box.field_1321, box.field_1320, box.field_1325, box.field_1324, color);
   }

   public static void drawBox(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
      Matrix4f matrix4f = matrices.method_23760().method_23761();
      RenderBuffers.QUADS.begin(matrix4f);
      RenderBuffers.QUADS.color(color);
      RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x2, y1, z1).vertex(x2, y1, z2).vertex(x1, y1, z2);
      RenderBuffers.QUADS.vertex(x1, y2, z1).vertex(x1, y2, z2).vertex(x2, y2, z2).vertex(x2, y2, z1);
      RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x1, y2, z1).vertex(x2, y2, z1).vertex(x2, y1, z1);
      RenderBuffers.QUADS.vertex(x2, y1, z1).vertex(x2, y2, z1).vertex(x2, y2, z2).vertex(x2, y1, z2);
      RenderBuffers.QUADS.vertex(x1, y1, z2).vertex(x2, y1, z2).vertex(x2, y2, z2).vertex(x1, y2, z2);
      RenderBuffers.QUADS.vertex(x1, y1, z1).vertex(x1, y1, z2).vertex(x1, y2, z2).vertex(x1, y2, z1);
      RenderBuffers.QUADS.end();
   }

   public static void renderBoundingBox(class_4587 matrices, class_2338 p, float width, int color) {
      renderBoundingBox(matrices, new class_238(p), width, color);
   }

   public static void renderBoundingBox(class_4587 matrices, class_238 box, float width, int color) {
      if (isFrustumVisible(box)) {
         matrices.method_22903();
         RenderSystem.lineWidth(width);
         drawBoundingBox(matrices, box, color);
         matrices.method_22909();
      }
   }

   public static void drawBoundingBox(class_4587 matrices, class_238 box, int color) {
      drawBoundingBox(matrices, box.field_1323, box.field_1322, box.field_1321, box.field_1320, box.field_1325, box.field_1324, color);
   }

   public static void drawBoundingBox(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
      Matrix4f matrix4f = matrices.method_23760().method_23761();
      RenderBuffers.LINES.begin(matrix4f);
      RenderBuffers.LINES.color(color);
      RenderBuffers.LINES.vertex(x1, y1, z1).vertex(x2, y1, z1);
      RenderBuffers.LINES.vertex(x2, y1, z1).vertex(x2, y1, z2);
      RenderBuffers.LINES.vertex(x2, y1, z2).vertex(x1, y1, z2);
      RenderBuffers.LINES.vertex(x1, y1, z2).vertex(x1, y1, z1);
      RenderBuffers.LINES.vertex(x1, y1, z1).vertex(x1, y2, z1);
      RenderBuffers.LINES.vertex(x2, y1, z1).vertex(x2, y2, z1);
      RenderBuffers.LINES.vertex(x2, y1, z2).vertex(x2, y2, z2);
      RenderBuffers.LINES.vertex(x1, y1, z2).vertex(x1, y2, z2);
      RenderBuffers.LINES.vertex(x1, y2, z1).vertex(x2, y2, z1);
      RenderBuffers.LINES.vertex(x2, y2, z1).vertex(x2, y2, z2);
      RenderBuffers.LINES.vertex(x2, y2, z2).vertex(x1, y2, z2);
      RenderBuffers.LINES.vertex(x1, y2, z2).vertex(x1, y2, z1);
      RenderBuffers.LINES.end();
   }

   public static void renderLine(class_4587 matrices, class_243 s, class_243 d, float width, int color) {
      renderLine(matrices, s.field_1352, s.field_1351, s.field_1350, d.field_1352, d.field_1351, d.field_1350, width, color);
   }

   public static void renderLine(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, float width, int color) {
      matrices.method_22903();
      RenderSystem.lineWidth(width);
      drawLine(matrices, x1, y1, z1, x2, y2, z2, color);
      matrices.method_22909();
   }

   public static void drawLine(class_4587 matrices, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
      Matrix4f matrix4f = matrices.method_23760().method_23761();
      RenderBuffers.LINES.begin(matrix4f);
      RenderBuffers.LINES.color(color);
      RenderBuffers.LINES.vertex(x1, y1, z1);
      RenderBuffers.LINES.vertex(x2, y2, z2);
      RenderBuffers.LINES.end();
   }

   public static void renderSign(class_4587 matrices, String text, class_243 pos) {
      renderSign(matrices, text, pos.method_10216(), pos.method_10214(), pos.method_10215());
   }

   public static void renderSign(class_4587 matrices, String text, double x1, double x2, double x3) {
      double dist = Math.sqrt(mc.field_1724.method_5649(x1, x2, x3));
      float scaling = 0.0018F + Modules.NAMETAGS.getScaling() * (float)dist;
      if (dist <= 8.0D) {
         scaling = 0.0245F;
      }

      class_4184 camera = mc.field_1773.method_19418();
      class_243 pos = camera.method_19326();
      class_4587 matrixStack = new class_4587();
      matrixStack.method_22903();
      matrixStack.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      matrixStack.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
      matrixStack.method_22904(x1 - pos.method_10216(), x2 - pos.method_10214(), x3 - pos.method_10215());
      matrixStack.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
      matrixStack.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      matrixStack.method_22905(-scaling, -scaling, -1.0F);
      GL11.glDepthFunc(519);
      class_4598 vertexConsumers = class_4597.method_22991(class_289.method_1348().method_1349());
      float hwidth = (float)mc.field_1772.method_1727(text) / 2.0F;
      Fonts.VANILLA.drawWithShadow(matrixStack, text, -hwidth, 0.0F, -1);
      vertexConsumers.method_22993();
      RenderSystem.disableBlend();
      GL11.glDepthFunc(515);
      matrixStack.method_22909();
   }

   public static boolean isFrustumVisible(class_238 box) {
      return ((AccessorWorldRenderer)mc.field_1769).getFrustum().method_23093(box);
   }

   public static void rect(class_4587 matrices, double x1, double y1, double x2, double y2, int color) {
      rect(matrices, x1, y1, x2, y2, 0.0D, color);
   }

   public static void rect(class_4587 matrices, double x1, double y1, double x2, double y2, double z, int color) {
      x2 += x1;
      y2 += y1;
      Matrix4f matrix4f = matrices.method_23760().method_23761();
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
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      BUFFER.method_1328(class_5596.field_27382, class_290.field_1576);
      BUFFER.method_22918(matrix4f, (float)x1, (float)y1, (float)z).method_22915(g, h, j, f).method_1344();
      BUFFER.method_22918(matrix4f, (float)x1, (float)y2, (float)z).method_22915(g, h, j, f).method_1344();
      BUFFER.method_22918(matrix4f, (float)x2, (float)y2, (float)z).method_22915(g, h, j, f).method_1344();
      BUFFER.method_22918(matrix4f, (float)x2, (float)y1, (float)z).method_22915(g, h, j, f).method_1344();
      class_286.method_43433(BUFFER.method_1326());
      RenderSystem.disableBlend();
   }

   public static void renderText(class_332 context, String text, float x, float y, int color) {
      context.method_51433(mc.field_1772, text, (int)x, (int)y, color, true);
   }

   public static int textWidth(String text) {
      return mc.field_1772.method_1727(text);
   }

   static {
      BUFFER = TESSELLATOR.method_1349();
   }
}
