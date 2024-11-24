package net.shoreline.client.impl.module.movement;

import net.minecraft.class_1297;
import net.minecraft.class_1501;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.entity.LookDirectionEvent;

public class YawModule extends ToggleModule {
   Config<Boolean> lockConfig = new BooleanConfig("Lock", "Locks the yaw in cardinal direction", false);

   public YawModule() {
      super("Yaw", "Locks player yaw to a cardinal axis", ModuleCategory.MOVEMENT);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         float yaw = (float)Math.round(mc.field_1724.method_36454() / 45.0F) * 45.0F;
         class_1297 vehicle = mc.field_1724.method_5854();
         if (vehicle != null) {
            vehicle.method_36456(yaw);
            if (vehicle instanceof class_1501) {
               class_1501 llama = (class_1501)vehicle;
               llama.method_5847(yaw);
            }

            return;
         }

         mc.field_1724.method_36456(yaw);
         mc.field_1724.method_5847(yaw);
      }

   }

   @EventListener
   public void onLookDirection(LookDirectionEvent event) {
      if ((Boolean)this.lockConfig.getValue()) {
         event.cancel();
      }

   }
}
