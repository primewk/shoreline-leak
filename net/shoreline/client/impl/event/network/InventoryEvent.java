package net.shoreline.client.impl.event.network;

import net.minecraft.class_2649;
import net.shoreline.client.api.event.Event;

public class InventoryEvent extends Event {
   private final class_2649 packet;

   public InventoryEvent(class_2649 packet) {
      this.packet = packet;
   }

   public class_2649 getPacket() {
      return this.packet;
   }
}
