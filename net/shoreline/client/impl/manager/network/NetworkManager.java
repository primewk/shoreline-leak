package net.shoreline.client.impl.manager.network;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_2596;
import net.minecraft.class_2792;
import net.minecraft.class_639;
import net.minecraft.class_640;
import net.minecraft.class_642;
import net.minecraft.class_7202;
import net.minecraft.class_7204;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.imixin.IClientPlayNetworkHandler;
import net.shoreline.client.mixin.accessor.AccessorClientWorld;
import net.shoreline.client.util.Globals;

public class NetworkManager implements Globals {
   private static final Set<class_2596<?>> PACKET_CACHE = new HashSet();
   private class_639 address;
   private class_642 info;

   public NetworkManager() {
      Shoreline.EVENT_HANDLER.subscribe(this);
   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      PACKET_CACHE.clear();
   }

   public void sendPacket(class_2596<?> p) {
      if (mc.method_1562() != null) {
         PACKET_CACHE.add(p);
         mc.method_1562().method_52787(p);
      }

   }

   public void sendQuietPacket(class_2596<?> p) {
      if (mc.method_1562() != null) {
         PACKET_CACHE.add(p);
         ((IClientPlayNetworkHandler)mc.method_1562()).sendQuietPacket(p);
      }

   }

   public void sendSequencedPacket(class_7204 p) {
      if (mc.field_1687 != null) {
         class_7202 updater = ((AccessorClientWorld)mc.field_1687).hookGetPendingUpdateManager().method_41937();

         try {
            int i = updater.method_41942();
            class_2596<class_2792> packet = p.predict(i);
            this.sendPacket(packet);
         } catch (Throwable var6) {
            var6.printStackTrace();
            if (updater != null) {
               try {
                  updater.close();
               } catch (Throwable var5) {
                  var5.printStackTrace();
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (updater != null) {
            updater.close();
         }
      }

   }

   public int getClientLatency() {
      if (mc.method_1562() != null) {
         class_640 playerEntry = mc.method_1562().method_2871(mc.field_1724.method_7334().getId());
         if (playerEntry != null) {
            return playerEntry.method_2959();
         }
      }

      return 0;
   }

   public class_639 getAddress() {
      return this.address;
   }

   public void setAddress(class_639 address) {
      this.address = address;
   }

   public class_642 getInfo() {
      return this.info;
   }

   public void setInfo(class_642 info) {
      this.info = info;
   }

   public boolean isCrystalPvpCC() {
      if (this.info == null) {
         return false;
      } else {
         return this.info.field_3761.equalsIgnoreCase("us.crystalpvp.cc") || this.info.field_3761.equalsIgnoreCase("crystalpvp.cc");
      }
   }

   public boolean isGrimCC() {
      return this.info != null && this.info.field_3761.equalsIgnoreCase("grim.crystalpvp.cc");
   }

   public boolean isCached(class_2596<?> p) {
      return PACKET_CACHE.contains(p);
   }
}
