package net.shoreline.client.impl.event.network;

import net.minecraft.class_2547;
import net.minecraft.class_2596;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;
import net.shoreline.client.init.Managers;

public class PacketEvent extends Event {
   private final class_2596<?> packet;

   public PacketEvent(class_2596<?> packet) {
      this.packet = packet;
   }

   public class_2596<?> getPacket() {
      return this.packet;
   }

   @Cancelable
   public static class Outbound extends PacketEvent {
      private final boolean cached;

      public Outbound(class_2596<?> packet) {
         super(packet);
         this.cached = Managers.NETWORK.isCached(packet);
      }

      public boolean isClientPacket() {
         return this.cached;
      }
   }

   @Cancelable
   public static class Inbound extends PacketEvent {
      private final class_2547 packetListener;

      public Inbound(class_2547 packetListener, class_2596<?> packet) {
         super(packet);
         this.packetListener = packetListener;
      }

      public class_2547 getPacketListener() {
         return this.packetListener;
      }
   }
}
