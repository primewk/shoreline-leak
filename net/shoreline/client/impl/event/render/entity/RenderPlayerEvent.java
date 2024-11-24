package net.shoreline.client.impl.event.render.entity;

import net.minecraft.class_742;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class RenderPlayerEvent extends Event {
   private final class_742 entity;
   private float yaw;
   private float pitch;

   public RenderPlayerEvent(class_742 entity) {
      this.entity = entity;
   }

   public class_742 getEntity() {
      return this.entity;
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
}
