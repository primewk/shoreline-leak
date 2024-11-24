package net.shoreline.client.impl.module.movement;

import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PlayerTickEvent;

public class ParkourModule extends ToggleModule {
   private boolean override;

   public ParkourModule() {
      super("Parkour", "Automatically jumps at the edge of blocks", ModuleCategory.MOVEMENT);
   }

   protected void onDisable() {
      super.onDisable();
      if (this.override) {
         this.override = false;
         mc.field_1690.field_1903.method_23481(false);
      }

   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      if (mc.field_1724.method_24828() && !mc.field_1724.method_5715() && mc.field_1687.method_18026(mc.field_1724.method_5829().method_989(0.0D, -0.5D, 0.0D).method_1009(-0.001D, 0.0D, -0.001D))) {
         mc.field_1690.field_1903.method_23481(true);
         this.override = true;
      } else if (this.override) {
         this.override = false;
         mc.field_1690.field_1903.method_23481(false);
      }

   }
}
