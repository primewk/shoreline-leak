package net.shoreline.client.impl.module.misc;

import net.minecraft.class_2596;
import net.minecraft.class_2815;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PacketEvent;

public class XCarryModule extends ToggleModule {
   Config<Boolean> inventoryConfig = new BooleanConfig("Inventory", "Prevents server from recieving packets regarding inventory items", true);
   Config<Boolean> armorConfig = new BooleanConfig("Armor", "Prevents server from recieving packets regarding armor items", false);
   Config<Boolean> forceCancelConfig = new BooleanConfig("ForceCancel", "Cancels all close window packets", false);

   public XCarryModule() {
      super("XCarry", "Allow player to carry items in the crafting slots", ModuleCategory.MISCELLANEOUS);
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (mc.field_1724 != null) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2815) {
            class_2815 packet = (class_2815)var3;
            if (packet.method_36168() == mc.field_1724.field_7498.field_7763 || (Boolean)this.forceCancelConfig.getValue()) {
               event.cancel();
            }
         }

      }
   }
}
