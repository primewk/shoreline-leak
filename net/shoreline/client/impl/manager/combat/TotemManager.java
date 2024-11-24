package net.shoreline.client.impl.manager.combat;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.class_1297;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.impl.event.entity.EntityDeathEvent;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.util.Globals;

public class TotemManager implements Globals {
   private final ConcurrentMap<UUID, Integer> totems = new ConcurrentHashMap();

   public TotemManager() {
      Shoreline.EVENT_HANDLER.subscribe(this);
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1687 != null) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2663) {
            class_2663 packet = (class_2663)var3;
            if (packet.method_11470() == 35) {
               class_1297 entity = packet.method_11469(mc.field_1687);
               if (entity != null && entity.method_5805()) {
                  this.totems.put(entity.method_5667(), this.totems.containsKey(entity.method_5667()) ? (Integer)this.totems.get(entity.method_5667()) + 1 : 1);
               }
            }
         }
      }

   }

   @EventListener(
      priority = Integer.MIN_VALUE
   )
   public void onRemoveEntity(EntityDeathEvent event) {
      if (event.getEntity() != mc.field_1724) {
         this.totems.remove(event.getEntity().method_5667());
      }
   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      this.totems.clear();
   }

   public int getTotems(class_1297 entity) {
      return (Integer)this.totems.getOrDefault(entity.method_5667(), 0);
   }
}
