package net.shoreline.client.impl.manager.player;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2371;
import net.minecraft.class_2596;
import net.minecraft.class_2735;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2868;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.Globals;

public class InventoryManager implements Globals {
   private int slot;

   public InventoryManager() {
      Shoreline.EVENT_HANDLER.subscribe(this);
   }

   @EventListener
   public void onPacketOutBound(PacketEvent.Outbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2868) {
         class_2868 packet = (class_2868)var3;
         int packetSlot = packet.method_12442();
         if (!class_1661.method_7380(packetSlot) || this.slot == packetSlot) {
            event.setCanceled(true);
            return;
         }

         this.slot = packetSlot;
      }

   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2735) {
         class_2735 packet = (class_2735)var3;
         this.slot = packet.method_11803();
      }

   }

   public void setSlot(int barSlot) {
      if (this.slot != barSlot && class_1661.method_7380(barSlot)) {
         this.setSlotForced(barSlot);
      }

   }

   public void setClientSlot(int barSlot) {
      if (mc.field_1724.method_31548().field_7545 != barSlot && class_1661.method_7380(barSlot)) {
         mc.field_1724.method_31548().field_7545 = barSlot;
         this.setSlotForced(barSlot);
      }

   }

   public void setSlotForced(int barSlot) {
      Managers.NETWORK.sendPacket(new class_2868(barSlot));
   }

   public void syncToClient() {
      if (this.isDesynced()) {
         this.setSlotForced(mc.field_1724.method_31548().field_7545);
      }

   }

   public boolean isDesynced() {
      return mc.field_1724.method_31548().field_7545 != this.slot;
   }

   public void closeScreen() {
      Managers.NETWORK.sendPacket(new class_2815(mc.field_1724.field_7512.field_7763));
   }

   public void pickupSlot(int slot) {
      this.click(slot, 0, class_1713.field_7790);
   }

   public void quickMove(int slot) {
      this.click(slot, 0, class_1713.field_7794);
   }

   public void throwSlot(int slot) {
      this.click(slot, 0, class_1713.field_7795);
   }

   private void click(int slot, int button, class_1713 type) {
      class_1703 screenHandler = mc.field_1724.field_7512;
      class_2371<class_1735> defaultedList = screenHandler.field_7761;
      int i = defaultedList.size();
      ArrayList<class_1799> list = Lists.newArrayListWithCapacity(i);
      Iterator var8 = defaultedList.iterator();

      while(var8.hasNext()) {
         class_1735 slot1 = (class_1735)var8.next();
         list.add(slot1.method_7677().method_7972());
      }

      screenHandler.method_7593(slot, button, type, mc.field_1724);
      Int2ObjectOpenHashMap<class_1799> int2ObjectMap = new Int2ObjectOpenHashMap();

      for(int j = 0; j < i; ++j) {
         class_1799 itemStack = (class_1799)list.get(j);
         class_1799 itemStack2;
         if (!class_1799.method_7973(itemStack, itemStack2 = ((class_1735)defaultedList.get(j)).method_7677())) {
            int2ObjectMap.put(j, itemStack2.method_7972());
         }
      }

      mc.field_1724.field_3944.method_52787(new class_2813(screenHandler.field_7763, screenHandler.method_37421(), slot, button, type, screenHandler.method_34255().method_7972(), int2ObjectMap));
   }

   public int count(class_1792 item) {
      class_1799 offhandStack = mc.field_1724.method_6079();
      int itemCount = offhandStack.method_7909() == item ? offhandStack.method_7947() : 0;

      for(int i = 0; i < 36; ++i) {
         class_1799 slot = mc.field_1724.method_31548().method_5438(i);
         if (slot.method_7909() == item) {
            itemCount += slot.method_7947();
         }
      }

      return itemCount;
   }

   public int getServerSlot() {
      return this.slot;
   }

   public int getClientSlot() {
      return mc.field_1724.method_31548().field_7545;
   }

   public class_1799 getServerItem() {
      return mc.field_1724 != null && this.getServerSlot() != -1 ? mc.field_1724.method_31548().method_5438(this.getServerSlot()) : null;
   }
}
