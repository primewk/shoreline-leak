package net.shoreline.client.impl.event.entity;

import net.minecraft.class_1268;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class SwingEvent extends Event {
   private final class_1268 hand;

   public SwingEvent(class_1268 hand) {
      this.hand = hand;
   }

   public class_1268 getHand() {
      return this.hand;
   }
}
