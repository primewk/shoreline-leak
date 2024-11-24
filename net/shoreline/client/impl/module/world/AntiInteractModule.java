package net.shoreline.client.impl.module.world;

import java.util.Arrays;
import java.util.List;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2885;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.InteractBlockEvent;
import net.shoreline.client.impl.event.network.PacketEvent;

public class AntiInteractModule extends ToggleModule {
   List<class_2248> blacklist;

   public AntiInteractModule() {
      super("AntiInteract", "Prevents player from interacting with certain objects", ModuleCategory.WORLD);
      this.blacklist = Arrays.asList(class_2246.field_10443, class_2246.field_10535);
   }

   @EventListener
   public void onInteractBlock(InteractBlockEvent event) {
      class_2338 pos = event.getHitResult().method_17777();
      class_2680 state = mc.field_1687.method_8320(pos);
      if (this.blacklist.contains(state.method_26204())) {
         event.cancel();
      }

   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2885) {
            class_2885 packet = (class_2885)var3;
            class_2338 pos = packet.method_12543().method_17777();
            class_2680 state = mc.field_1687.method_8320(pos);
            if (this.blacklist.contains(state.method_26204())) {
               event.cancel();
            }
         }

      }
   }
}
