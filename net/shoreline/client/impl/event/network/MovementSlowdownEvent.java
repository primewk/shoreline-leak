package net.shoreline.client.impl.event.network;

import net.minecraft.class_744;
import net.shoreline.client.api.event.Event;

public class MovementSlowdownEvent extends Event {
   public final class_744 input;

   public MovementSlowdownEvent(class_744 input) {
      this.input = input;
   }

   public class_744 getInput() {
      return this.input;
   }
}
