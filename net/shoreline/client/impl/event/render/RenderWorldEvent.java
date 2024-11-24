package net.shoreline.client.impl.event.render;

import net.minecraft.class_4587;
import net.shoreline.client.api.event.Event;

public class RenderWorldEvent extends Event {
   private final class_4587 matrices;
   private final float tickDelta;

   public RenderWorldEvent(class_4587 matrices, float tickDelta) {
      this.matrices = matrices;
      this.tickDelta = tickDelta;
   }

   public class_4587 getMatrices() {
      return this.matrices;
   }

   public float getTickDelta() {
      return this.tickDelta;
   }

   public static class Game extends RenderWorldEvent {
      public Game(class_4587 matrices, float tickDelta) {
         super(matrices, tickDelta);
      }
   }
}
