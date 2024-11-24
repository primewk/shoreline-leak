package net.shoreline.client.impl.event.entity.player;

import net.minecraft.class_1313;
import net.minecraft.class_243;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class PlayerMoveEvent extends Event {
   private final class_1313 type;
   private double x;
   private double y;
   private double z;

   public PlayerMoveEvent(class_1313 type, class_243 movement) {
      this.type = type;
      this.x = movement.method_10216();
      this.y = movement.method_10214();
      this.z = movement.method_10215();
   }

   public class_1313 getType() {
      return this.type;
   }

   public class_243 getMovement() {
      return new class_243(this.x, this.y, this.z);
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(double z) {
      this.z = z;
   }
}
