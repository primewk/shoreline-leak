package net.shoreline.client.impl.event.entity;

import net.shoreline.client.api.event.Event;

public final class JumpRotationEvent extends Event {
   private float yaw;

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }
}
