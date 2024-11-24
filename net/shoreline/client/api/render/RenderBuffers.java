package net.shoreline.client.api.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_5253.class_5254;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class RenderBuffers {
   public static final RenderBuffers.Buffer QUADS;
   public static final RenderBuffers.Buffer LINES;
   private static final List<Runnable> postRenderCallbacks;
   private static boolean isSetup;

   public static void preRender() {
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      RenderSystem.enableCull();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableDepthTest();
      isSetup = true;
   }

   public static void postRender() {
      QUADS.draw();
      LINES.draw();
      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
      RenderSystem.disableCull();
      GL11.glDisable(2848);
      isSetup = false;
      Iterator var0 = postRenderCallbacks.iterator();

      while(var0.hasNext()) {
         Runnable callback = (Runnable)var0.next();
         callback.run();
      }

      postRenderCallbacks.clear();
   }

   public static void post(Runnable callback) {
      if (isSetup) {
         postRenderCallbacks.add(callback);
      } else {
         callback.run();
      }

   }

   static {
      QUADS = new RenderBuffers.Buffer(class_5596.field_27382, class_290.field_1576);
      LINES = new RenderBuffers.Buffer(class_5596.field_29344, class_290.field_1576);
      postRenderCallbacks = new ArrayList();
      isSetup = false;
   }

   public static class Buffer {
      public final class_287 buffer = new class_287(2048);
      private final class_5596 drawMode;
      private final class_293 vertexFormat;
      private Matrix4f positionMatrix;

      public Buffer(class_5596 drawMode, class_293 vertexFormat) {
         this.drawMode = drawMode;
         this.vertexFormat = vertexFormat;
      }

      public void begin(Matrix4f positionMatrix) {
         this.positionMatrix = positionMatrix;
         if (!this.buffer.method_22893()) {
            this.buffer.method_1328(this.drawMode, this.vertexFormat);
         }

      }

      public void end() {
         if (!RenderBuffers.isSetup) {
            this.draw();
         }

      }

      public RenderBuffers.Buffer vertex(double x, double y, double z) {
         return this.vertex((float)x, (float)y, (float)z);
      }

      public RenderBuffers.Buffer vertex(float x, float y, float z) {
         this.buffer.method_22918(this.positionMatrix, x, y, z).method_1344();
         return this;
      }

      public void color(int color) {
         this.buffer.method_22901(class_5254.method_27765(color), class_5254.method_27766(color), class_5254.method_27767(color), class_5254.method_27762(color));
      }

      public void draw() {
         if (this.buffer.method_22893()) {
            if (this.buffer.method_43574()) {
               this.buffer.method_1343();
            } else {
               RenderSystem.setShader(class_757::method_34540);
               class_286.method_43433(this.buffer.method_1326());
            }
         }

      }
   }
}
