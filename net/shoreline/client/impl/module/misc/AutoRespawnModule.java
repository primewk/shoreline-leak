package net.shoreline.client.impl.module.misc;

import net.minecraft.class_418;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.ScreenOpenEvent;
import net.shoreline.client.impl.event.TickEvent;

public class AutoRespawnModule extends ToggleModule {
   private boolean respawn;

   public AutoRespawnModule() {
      super("AutoRespawn", "Respawns automatically after a death", ModuleCategory.MISCELLANEOUS);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE && this.respawn && mc.field_1724.method_29504()) {
         mc.field_1724.method_7331();
         this.respawn = false;
      }

   }

   @EventListener
   public void onScreenOpen(ScreenOpenEvent event) {
      if (event.getScreen() instanceof class_418) {
         this.respawn = true;
      }

   }
}
