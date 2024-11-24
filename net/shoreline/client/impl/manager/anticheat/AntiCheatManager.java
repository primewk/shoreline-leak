package net.shoreline.client.impl.manager.anticheat;

import java.util.Arrays;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_6373;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.util.Globals;

public final class AntiCheatManager implements Globals {
   private SetbackData lastSetback;
   private final int[] transactions = new int[4];
   private int index;
   private boolean isGrim;

   public AntiCheatManager() {
      Shoreline.EVENT_HANDLER.subscribe(this);
      Arrays.fill(this.transactions, -1);
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      class_2596 var4 = event.getPacket();
      if (var4 instanceof class_6373) {
         class_6373 packet = (class_6373)var4;
         if (this.index > 3) {
            return;
         }

         int uid = packet.method_36950();
         this.transactions[this.index] = uid;
         ++this.index;
         if (this.index == 4) {
            this.grimCheck();
         }
      } else {
         var4 = event.getPacket();
         if (var4 instanceof class_2708) {
            class_2708 packet = (class_2708)var4;
            this.lastSetback = new SetbackData(new class_243(packet.method_11734(), packet.method_11735(), packet.method_11738()), System.currentTimeMillis(), packet.method_11737());
         }
      }

   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      Arrays.fill(this.transactions, -1);
      this.index = 0;
      this.isGrim = false;
   }

   private void grimCheck() {
      for(int i = 0; i < 4 && this.transactions[i] == -i; ++i) {
      }

      this.isGrim = true;
      Shoreline.LOGGER.info("Server is running GrimAC.");
   }

   public boolean isGrim() {
      return this.isGrim;
   }

   public boolean hasPassed(long timeMS) {
      return this.lastSetback != null && this.lastSetback.timeSince() >= timeMS;
   }
}
