package net.shoreline.client.impl.event.entity;

import net.minecraft.class_1297;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class LookDirectionEvent extends Event {
   private final class_1297 entity;
   private final double cursorDeltaX;
   private final double cursorDeltaY;

   public LookDirectionEvent(class_1297 entity, double cursorDeltaX, double cursorDeltaY) {
      this.entity = entity;
      this.cursorDeltaX = cursorDeltaX;
      this.cursorDeltaY = cursorDeltaY;
   }

   public class_1297 getEntity() {
      return this.entity;
   }

   public double getCursorDeltaX() {
      return this.cursorDeltaX;
   }

   public double getCursorDeltaY() {
      return this.cursorDeltaY;
   }
}
