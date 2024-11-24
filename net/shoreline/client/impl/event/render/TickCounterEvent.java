package net.shoreline.client.impl.event.render;

import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class TickCounterEvent extends Event {
   private float ticks;

   public float getTicks() {
      return this.ticks;
   }

   public void setTicks(float ticks) {
      this.ticks = ticks;
   }
}
