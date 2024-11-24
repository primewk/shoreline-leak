package net.shoreline.client.impl.event.world;

import java.awt.Color;
import net.minecraft.class_243;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

public class SkyboxEvent extends Event {
   private Color color;

   public Color getColor() {
      return this.color;
   }

   public class_243 getColorVec() {
      return new class_243((double)this.color.getRed() / 255.0D, (double)this.color.getGreen() / 255.0D, (double)this.color.getBlue() / 255.0D);
   }

   public int getRGB() {
      return this.color.getRGB();
   }

   public void setColor(Color color) {
      this.color = color;
   }

   @Cancelable
   public static class Fog extends SkyboxEvent {
      private final float tickDelta;

      public Fog(float tickDelta) {
         this.tickDelta = tickDelta;
      }

      public float getTickDelta() {
         return this.tickDelta;
      }
   }

   @Cancelable
   public static class Cloud extends SkyboxEvent {
   }

   @Cancelable
   public static class Sky extends SkyboxEvent {
   }
}
