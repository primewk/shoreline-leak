package net.shoreline.client.impl.event.render.entity;

import net.minecraft.class_1309;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_583;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class RenderLivingEntityEvent extends Event {
   private final class_1309 entity;
   private final class_583<?> model;
   private final class_4587 matrices;
   private final class_4588 vertexConsumer;
   private final int light;
   private final int overlay;
   private final float red;
   private final float green;
   private final float blue;
   private final float alpha;

   public RenderLivingEntityEvent(class_1309 entity, class_583<?> model, class_4587 matrices, class_4588 vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
      this.entity = entity;
      this.model = model;
      this.matrices = matrices;
      this.vertexConsumer = vertexConsumer;
      this.light = light;
      this.overlay = overlay;
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.alpha = alpha;
   }

   public class_1309 getEntity() {
      return this.entity;
   }

   public class_583<?> getModel() {
      return this.model;
   }

   public class_4587 getMatrices() {
      return this.matrices;
   }

   public class_4588 getVertexConsumerProvider() {
      return this.vertexConsumer;
   }

   public int getLight() {
      return this.light;
   }

   public int getOverlay() {
      return this.overlay;
   }

   public float getRed() {
      return this.red;
   }

   public float getGreen() {
      return this.green;
   }

   public float getBlue() {
      return this.blue;
   }

   public float getAlpha() {
      return this.alpha;
   }
}
