package net.shoreline.client.impl.module.movement;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;

public class AutoWalkModule extends ToggleModule {
   Config<Boolean> lockConfig = new BooleanConfig("Lock", "Stops movement when sneaking or jumping", false);

   public AutoWalkModule() {
      super("AutoWalk", "Automatically moves forward", ModuleCategory.MOVEMENT);
   }

   public void onDisable() {
      mc.field_1690.field_1894.method_23481(false);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         mc.field_1690.field_1894.method_23481(!mc.field_1690.field_1832.method_1434() && (!(Boolean)this.lockConfig.getValue() || !mc.field_1690.field_1903.method_1434() && mc.field_1724.method_24828()));
      }

   }
}
