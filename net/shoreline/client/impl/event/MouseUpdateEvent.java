package net.shoreline.client.impl.event;

import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class MouseUpdateEvent extends Event {
   private final double cursorDeltaX;
   private final double cursorDeltaY;

   public MouseUpdateEvent(double cursorDeltaX, double cursorDeltaY) {
      this.cursorDeltaX = cursorDeltaX;
      this.cursorDeltaY = cursorDeltaY;
   }

   public double getCursorDeltaX() {
      return this.cursorDeltaX;
   }

   public double getCursorDeltaY() {
      return this.cursorDeltaY;
   }
}
