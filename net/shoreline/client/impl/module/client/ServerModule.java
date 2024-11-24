package net.shoreline.client.impl.module.client;

import net.minecraft.class_2596;
import net.minecraft.class_2668;
import net.minecraft.class_2720;
import net.minecraft.class_2856;
import net.minecraft.class_2856.class_2857;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ConcurrentModule;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.init.Managers;

public final class ServerModule extends ConcurrentModule {
   Config<Boolean> packetKickConfig = new BooleanConfig("NoPacketKick", "Prevents thrown exceptions from kicking you", true);
   Config<Boolean> demoConfig = new BooleanConfig("NoDemo", "Prevents servers from forcing you to a demo screen", true);
   Config<Boolean> resourcePackConfig = new BooleanConfig("NoResourcePack", "Prevents server from forcing resource pack", false);

   public ServerModule() {
      super("Server", "Prevents servers actions on player", ModuleCategory.CLIENT);
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2668) {
         class_2668 packet = (class_2668)var3;
         if (packet.method_11491() == class_2668.field_25650 && !mc.method_1530() && (Boolean)this.demoConfig.getValue()) {
            Shoreline.info("Server attempted to use Demo mode features on you!");
            event.cancel();
         }
      }

      if (event.getPacket() instanceof class_2720 && (Boolean)this.resourcePackConfig.getValue()) {
         event.cancel();
         Managers.NETWORK.sendPacket(new class_2856(mc.field_1724.method_5667(), class_2857.field_13018));
      }

   }

   public boolean isPacketKick() {
      return (Boolean)this.packetKickConfig.getValue();
   }
}
