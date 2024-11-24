package net.shoreline.client.impl.event.entity;

import net.minecraft.class_2680;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class SlowMovementEvent extends Event {
   private final class_2680 state;

   public SlowMovementEvent(class_2680 state) {
      this.state = state;
   }

   public class_2680 getState() {
      return this.state;
   }
}
