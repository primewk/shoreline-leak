package net.shoreline.client.impl.module.movement;

import net.minecraft.class_1937;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2830;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.mixin.accessor.AccessorPlayerMoveC2SPacket;
import net.shoreline.client.util.string.EnumFormatter;

public class NoFallModule extends ToggleModule {
   Config<NoFallModule.NoFallMode> modeConfig;

   public NoFallModule() {
      super("NoFall", "Prevents all fall damage", ModuleCategory.MOVEMENT);
      this.modeConfig = new EnumConfig("Mode", "The mode to prevent fall damage", NoFallModule.NoFallMode.ANTI, NoFallModule.NoFallMode.values());
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
   }

   @EventListener
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (event.getStage() == EventStage.PRE && this.checkFalling()) {
         if (this.modeConfig.getValue() == NoFallModule.NoFallMode.LATENCY) {
            if (mc.field_1687.method_27983() == class_1937.field_25180) {
               Managers.NETWORK.sendPacket(new class_2829(mc.field_1724.method_23317(), 0.0D, mc.field_1724.method_23321(), true));
            } else {
               Managers.NETWORK.sendPacket(new class_2829(0.0D, 64.0D, 0.0D, true));
            }

            mc.field_1724.field_6017 = 0.0F;
         } else if (this.modeConfig.getValue() == NoFallModule.NoFallMode.GRIM) {
            Managers.NETWORK.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-9D, mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), true));
            mc.field_1724.method_38785();
         }

      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (mc.field_1724 != null && this.checkFalling()) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2828) {
            class_2828 packet = (class_2828)var3;
            if (this.modeConfig.getValue() == NoFallModule.NoFallMode.PACKET) {
               ((AccessorPlayerMoveC2SPacket)packet).hookSetOnGround(true);
            } else if (this.modeConfig.getValue() == NoFallModule.NoFallMode.ANTI) {
               double y = packet.method_12268(mc.field_1724.method_23318());
               ((AccessorPlayerMoveC2SPacket)packet).hookSetY(y + 0.10000000149011612D);
            }
         }

      }
   }

   private boolean checkFalling() {
      return mc.field_1724.field_6017 > (float)mc.field_1724.method_5850() && !mc.field_1724.method_24828() && !mc.field_1724.method_6128() && !Modules.FLIGHT.isEnabled() && !Modules.PACKET_FLY.isEnabled();
   }

   public static enum NoFallMode {
      ANTI,
      LATENCY,
      PACKET,
      GRIM;

      // $FF: synthetic method
      private static NoFallModule.NoFallMode[] $values() {
         return new NoFallModule.NoFallMode[]{ANTI, LATENCY, PACKET, GRIM};
      }
   }
}
