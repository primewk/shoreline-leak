package net.shoreline.client.impl.event.entity;

import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class VelocityMultiplierEvent extends Event {
   private final class_2680 state;

   public VelocityMultiplierEvent(class_2680 state) {
      this.state = state;
   }

   public class_2248 getBlock() {
      return this.state.method_26204();
   }
}
