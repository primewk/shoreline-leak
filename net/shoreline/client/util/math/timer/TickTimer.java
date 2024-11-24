package net.shoreline.client.util.math.timer;

import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.impl.event.TickEvent;

public class TickTimer implements Timer {
   private long ticks = 0L;

   public TickTimer() {
      Shoreline.EVENT_HANDLER.subscribe(this);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         ++this.ticks;
      }

   }

   public boolean passed(Number time) {
      return this.ticks >= time.longValue();
   }

   public void reset() {
      this.setElapsedTime(0);
   }

   public long getElapsedTime() {
      return this.ticks;
   }

   public void setElapsedTime(Number time) {
      this.ticks = time.longValue();
   }
}
