package net.shoreline.client.impl.event.world;

import net.minecraft.class_1297;
import net.shoreline.client.api.event.Event;

public final class UpdateCrosshairTargetEvent extends Event {
   private final float tickDelta;
   private final class_1297 cameraEntity;

   public UpdateCrosshairTargetEvent(float tickDelta, class_1297 cameraEntity) {
      this.tickDelta = tickDelta;
      this.cameraEntity = cameraEntity;
   }

   public float getTickDelta() {
      return this.tickDelta;
   }

   public class_1297 getCameraEntity() {
      return this.cameraEntity;
   }
}
