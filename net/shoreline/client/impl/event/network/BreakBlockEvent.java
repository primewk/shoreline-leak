package net.shoreline.client.impl.event.network;

import net.minecraft.class_2338;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class BreakBlockEvent extends Event {
   private final class_2338 pos;

   public BreakBlockEvent(class_2338 pos) {
      this.pos = pos;
   }

   public class_2338 getPos() {
      return this.pos;
   }
}
