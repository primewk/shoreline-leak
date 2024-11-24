package net.shoreline.client.impl.module.movement;

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
import net.shoreline.client.impl.event.item.TridentPullbackEvent;
import net.shoreline.client.impl.event.item.TridentWaterEvent;
import net.shoreline.client.init.Managers;

public class TridentFlyModule extends ToggleModule {
   Config<Boolean> allowNoWaterConfig = new BooleanConfig("AllowNoWater", "Allows you to fly using tridents even without water", true);
   Config<Boolean> instantConfig = new BooleanConfig("Instant", "Removes the pullback of the trident", true);
   Config<Boolean> flyConfig = new BooleanConfig("Spam", "Automatically uses riptide", false);
   Config<Integer> ticksConfig = new NumberConfig("Ticks", "The ticks between riptide boost", 0, 3, 20, () -> {
      return (Boolean)this.flyConfig.getValue();
   });

   public TridentFlyModule() {
      super("TridentFly", "Allows you to fly using tridents", ModuleCategory.MOVEMENT);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() != EventStage.PRE && (Boolean)this.flyConfig.getValue()) {
         if (mc.field_1724.method_6047().method_7909() == class_1802.field_8547 && mc.field_1724.method_6048() >= (Integer)this.ticksConfig.getValue()) {
            Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
            mc.field_1724.method_6075();
         }

      }
   }

   @EventListener
   public void onTridentPullback(TridentPullbackEvent event) {
      if ((Boolean)this.instantConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onTridentWaterCheck(TridentWaterEvent event) {
      if ((Boolean)this.allowNoWaterConfig.getValue()) {
         event.cancel();
      }

   }
}
