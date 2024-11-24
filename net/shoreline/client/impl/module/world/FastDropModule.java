package net.shoreline.client.impl.module.world;

import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import net.minecraft.class_2846.class_2847;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.init.Managers;

public class FastDropModule extends ToggleModule {
   Config<Integer> delayConfig = new NumberConfig("Delay", "The delay for dropping items", 0, 0, 4);
   private int dropTicks;

   public FastDropModule() {
      super("FastDrop", "Drops items from the hotbar faster", ModuleCategory.WORLD);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (mc.field_1690.field_1869.method_1434() && this.dropTicks > (Integer)this.delayConfig.getValue()) {
            Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12975, class_2338.field_10980, class_2350.field_11033));
            this.dropTicks = 0;
         }

         ++this.dropTicks;
      }
   }
}
