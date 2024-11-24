package net.shoreline.client.impl.event.camera;

import net.minecraft.class_241;
import net.shoreline.client.api.event.Event;

public class CameraRotationEvent extends Event {
   private float yaw;
   private float pitch;
   private final float tickDelta;

   public CameraRotationEvent(float yaw, float pitch, float tickDelta) {
      this.yaw = yaw;
      this.pitch = pitch;
      this.tickDelta = tickDelta;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public void setRotation(class_241 rotation) {
      this.yaw = rotation.field_1343;
      this.pitch = rotation.field_1342;
   }

   public float getTickDelta() {
      return this.tickDelta;
   }
}
