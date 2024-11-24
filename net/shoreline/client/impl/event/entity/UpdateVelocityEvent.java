package net.shoreline.client.impl.event.entity;

import net.minecraft.class_243;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.StageEvent;

@Cancelable
public class UpdateVelocityEvent extends StageEvent {
   private final class_243 movementInput;
   private final float speed;
   private final float yaw;
   private class_243 velocity;

   public UpdateVelocityEvent(class_243 movementInput, float speed, float yaw, class_243 velocity) {
      this.movementInput = movementInput;
      this.speed = speed;
      this.yaw = yaw;
      this.velocity = velocity;
   }

   public class_243 getMovementInput() {
      return this.movementInput;
   }

   public float getSpeed() {
      return this.speed;
   }

   public class_243 getVelocity() {
      return this.velocity;
   }

   public void setVelocity(class_243 velocity) {
      this.velocity = velocity;
   }
}
