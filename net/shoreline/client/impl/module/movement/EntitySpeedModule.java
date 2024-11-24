package net.shoreline.client.impl.module.movement;

import java.text.DecimalFormat;
import net.minecraft.class_1268;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2708;
import net.minecraft.class_2752;
import net.minecraft.class_2824;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;

public class EntitySpeedModule extends ToggleModule {
   Config<Float> speedConfig = new NumberConfig("Speed", "The speed of the entity while moving", 0.1F, 0.5F, 4.0F);
   Config<Boolean> antiStuckConfig = new BooleanConfig("AntiStuck", "Prevents entities from getting stuck when moving up", false);
   Config<Boolean> strictConfig = new BooleanConfig("Strict", "The NCP-Updated bypass for speeding up entity movement", false);
   private final Timer entityJumpTimer = new CacheTimer();

   public EntitySpeedModule() {
      super("EntitySpeed", "Increases riding entity speeds", ModuleCategory.MOVEMENT);
   }

   public String getModuleData() {
      DecimalFormat decimal = new DecimalFormat("0.0");
      return decimal.format(this.speedConfig.getValue());
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (mc.field_1724.method_3144() && mc.field_1724.method_49694() != null) {
            double d = Math.cos(Math.toRadians((double)(mc.field_1724.method_36454() + 90.0F)));
            double d2 = Math.sin(Math.toRadians((double)(mc.field_1724.method_36454() + 90.0F)));
            class_2338 pos1 = class_2338.method_49637(mc.field_1724.method_23317() + 2.0D * d, mc.field_1724.method_23318() - 1.0D, mc.field_1724.method_23321() + 2.0D * d2);
            class_2338 pos2 = class_2338.method_49637(mc.field_1724.method_23317() + 2.0D * d, mc.field_1724.method_23318() - 2.0D, mc.field_1724.method_23321() + 2.0D * d2);
            if ((Boolean)this.antiStuckConfig.getValue() && !mc.field_1724.method_49694().method_24828() && !mc.field_1687.method_8320(pos1).method_51366() && !mc.field_1687.method_8320(pos2).method_51366()) {
               this.entityJumpTimer.reset();
               return;
            }

            class_2338 pos3 = class_2338.method_49637(mc.field_1724.method_23317() + 2.0D * d, mc.field_1724.method_23318(), mc.field_1724.method_23321() + 2.0D * d2);
            if ((Boolean)this.antiStuckConfig.getValue() && mc.field_1687.method_8320(pos3).method_51366()) {
               this.entityJumpTimer.reset();
               return;
            }

            class_2338 pos4 = class_2338.method_49637(mc.field_1724.method_23317() + d, mc.field_1724.method_23318() + 1.0D, mc.field_1724.method_23321() + d2);
            if ((Boolean)this.antiStuckConfig.getValue() && mc.field_1687.method_8320(pos4).method_51366()) {
               this.entityJumpTimer.reset();
               return;
            }

            if (mc.field_1724.field_3913.field_3904) {
               this.entityJumpTimer.reset();
            }

            if (this.entityJumpTimer.passed(10000) || !(Boolean)this.antiStuckConfig.getValue()) {
               if (!mc.field_1724.method_49694().method_5799() || mc.field_1724.field_3913.field_3904 || !this.entityJumpTimer.passed(1000)) {
                  if (mc.field_1724.method_49694().method_24828()) {
                     mc.field_1724.method_49694().method_18800(mc.field_1724.method_18798().field_1352, 0.4D, mc.field_1724.method_18798().field_1350);
                  }

                  mc.field_1724.method_49694().method_18800(mc.field_1724.method_18798().field_1352, -0.4D, mc.field_1724.method_18798().field_1350);
               }

               if ((Boolean)this.strictConfig.getValue()) {
                  Managers.NETWORK.sendPacket(class_2824.method_34207(mc.field_1724.method_49694(), false, class_1268.field_5808));
               }

               this.handleEntityMotion((Float)this.speedConfig.getValue(), d, d2);
               this.entityJumpTimer.reset();
            }
         }

      }
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1724 != null && mc.field_1724.method_3144() && !mc.field_1690.field_1832.method_1434() && mc.field_1724.method_49694() != null) {
         if ((Boolean)this.strictConfig.getValue()) {
            if (event.getPacket() instanceof class_2752) {
               event.cancel();
            } else if (event.getPacket() instanceof class_2708) {
               event.cancel();
            }
         }

      }
   }

   private void handleEntityMotion(float entitySpeed, double d, double d2) {
      class_243 motion = mc.field_1724.method_49694().method_18798();
      float forward = mc.field_1724.field_3913.field_3905;
      float strafe = mc.field_1724.field_3913.field_3907;
      if (forward == 0.0F && strafe == 0.0F) {
         mc.field_1724.method_49694().method_18800(0.0D, motion.field_1351, 0.0D);
      } else {
         mc.field_1724.method_49694().method_18800((double)(forward * entitySpeed) * d + (double)(strafe * entitySpeed) * d2, motion.field_1351, (double)(forward * entitySpeed) * d2 - (double)(strafe * entitySpeed) * d);
      }
   }
}
