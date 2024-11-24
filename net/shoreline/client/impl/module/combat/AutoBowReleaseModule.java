package net.shoreline.client.impl.module.combat;

import net.minecraft.class_1268;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import net.minecraft.class_2846.class_2847;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;

public class AutoBowReleaseModule extends ToggleModule {
   Config<Boolean> crossbowConfig = new BooleanConfig("Crossbow", "Automatically releases crossbow when fully charged", false);
   Config<Integer> ticksConfig = new NumberConfig("Ticks", "Ticks before releasing the bow charge", 3, 5, 20);
   Config<Boolean> tpsSyncConfig = new BooleanConfig("TPS-Sync", "Sync bow release to server ticks", false);

   public AutoBowReleaseModule() {
      super("AutoBowRelease", "Automatically releases a charged bow", ModuleCategory.COMBAT);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (!Modules.SELF_BOW.isEnabled()) {
         if (event.getStage() == EventStage.POST) {
            class_1799 mainhand = mc.field_1724.method_6047();
            if (mainhand.method_7909() == class_1802.field_8102) {
               float off = (Boolean)this.tpsSyncConfig.getValue() ? 20.0F - Managers.TICK.getTpsAverage() : 0.0F;
               if ((float)mc.field_1724.method_6048() + off >= (float)(Integer)this.ticksConfig.getValue()) {
                  Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
                  mc.field_1724.method_6075();
               }
            } else if ((Boolean)this.crossbowConfig.getValue() && mainhand.method_7909() == class_1802.field_8399 && (float)mc.field_1724.method_6048() / (float)class_1764.method_7775(mc.field_1724.method_6047()) > 1.0F) {
               Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
               mc.field_1724.method_6075();
               mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
            }
         }

      }
   }
}
