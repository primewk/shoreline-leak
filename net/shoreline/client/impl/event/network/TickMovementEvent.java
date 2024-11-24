package net.shoreline.client.impl.event.network;

import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class TickMovementEvent extends Event {
   private int iterations;

   public int getIterations() {
      return this.iterations;
   }

   public void setIterations(int iterations) {
      this.iterations = iterations;
   }
}
