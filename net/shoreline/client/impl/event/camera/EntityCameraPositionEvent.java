package net.shoreline.client.impl.event.camera;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.shoreline.client.api.event.Event;

public class EntityCameraPositionEvent extends Event {
   private class_243 position;
   private final float tickDelta;
   private final class_1297 entity;

   public EntityCameraPositionEvent(class_243 position, class_1297 entity, float tickDelta) {
      this.position = position;
      this.tickDelta = tickDelta;
      this.entity = entity;
   }

   public float getTickDelta() {
      return this.tickDelta;
   }

   public class_243 getPosition() {
      return this.position;
   }

   public void setPosition(class_243 position) {
      this.position = position;
   }

   public class_1297 getEntity() {
      return this.entity;
   }
}
