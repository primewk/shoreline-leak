package net.shoreline.client.impl.module.movement;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.player.MovementUtil;

public class TickShiftModule extends ToggleModule {
   Config<Integer> ticksConfig = new NumberConfig("MaxTicks", "Maximum charge ticks", 1, 20, 40);
   Config<Integer> packetsConfig = new NumberConfig("Packets", "Packets to release from storage every tick", 1, 1, 5);
   Config<Integer> chargeSpeedConfig = new NumberConfig("ChargeSpeed", "The speed to charge the stored packets", 1, 1, 5);
   private int packets;

   public TickShiftModule() {
      super("TickShift", "Exploits NCP to speed up ticks", ModuleCategory.MOVEMENT);
   }

   public String getModuleData() {
      return String.valueOf(this.packets);
   }

   @EventListener
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (!MovementUtil.isMoving() && mc.field_1724.method_24828()) {
            this.packets += (Integer)this.chargeSpeedConfig.getValue();
            if (this.packets > (Integer)this.ticksConfig.getValue()) {
               this.packets = (Integer)this.ticksConfig.getValue();
            }
         } else {
            this.packets -= (Integer)this.packetsConfig.getValue();
            if (this.packets <= 0) {
               this.packets = 0;
               Managers.TICK.setClientTick(1.0F);
               return;
            }

            Managers.TICK.setClientTick((float)(Integer)this.packetsConfig.getValue() + 1.0F);
         }

      }
   }
}
