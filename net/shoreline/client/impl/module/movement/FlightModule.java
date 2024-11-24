package net.shoreline.client.impl.module.movement;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.config.ConfigUpdateEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;
import net.shoreline.client.util.player.MovementUtil;
import net.shoreline.client.util.string.EnumFormatter;

public class FlightModule extends ToggleModule {
   Config<FlightModule.FlightMode> modeConfig;
   Config<Float> speedConfig;
   Config<Float> vspeedConfig;
   Config<Boolean> antiKickConfig;
   Config<Boolean> accelerateConfig;
   Config<Float> accelerateSpeedConfig;
   Config<Float> maxSpeedConfig;
   private double speed;
   private final Timer antiKickTimer;
   private final Timer antiKick2Timer;

   public FlightModule() {
      super("Flight", "Allows the player to fly in survival", ModuleCategory.MOVEMENT);
      this.modeConfig = new EnumConfig("Mode", "The mode for vanilla flight", FlightModule.FlightMode.NORMAL, FlightModule.FlightMode.values());
      this.speedConfig = new NumberConfig("Speed", "The horizontal flight speed", 0.1F, 2.5F, 10.0F);
      this.vspeedConfig = new NumberConfig("VerticalSpeed", "The vertical flight speed", 0.1F, 1.0F, 5.0F);
      this.antiKickConfig = new BooleanConfig("AntiKick", "Prevents vanilla flight detection", true);
      this.accelerateConfig = new BooleanConfig("Accelerate", "Accelerate as you fly", false);
      this.accelerateSpeedConfig = new NumberConfig("AccelerateSpeed", "Speed to accelerate as", 0.01F, 0.2F, 1.0F, () -> {
         return (Boolean)this.accelerateConfig.getValue();
      });
      this.maxSpeedConfig = new NumberConfig("MaxSpeed", "Max speed to acceleratee to", 1.0F, 5.0F, 10.0F, () -> {
         return (Boolean)this.accelerateConfig.getValue();
      });
      this.antiKickTimer = new CacheTimer();
      this.antiKick2Timer = new CacheTimer();
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
   }

   public void onEnable() {
      this.antiKickTimer.reset();
      this.antiKick2Timer.reset();
      if (this.modeConfig.getValue() == FlightModule.FlightMode.VANILLA) {
         this.enableVanillaFly();
      }

      this.speed = 0.0D;
   }

   public void onDisable() {
      if (this.modeConfig.getValue() == FlightModule.FlightMode.VANILLA) {
         this.disableVanillaFly();
      }

   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      if ((Boolean)this.accelerateConfig.getValue()) {
         if (!MovementUtil.isInputtingMovement() || mc.field_1724.field_5976) {
            this.speed = 0.0D;
         }

         this.speed += (double)(Float)this.accelerateSpeedConfig.getValue();
         if (this.speed > (double)(Float)this.maxSpeedConfig.getValue()) {
            this.speed = (double)(Float)this.maxSpeedConfig.getValue();
         }
      } else {
         this.speed = (double)(Float)this.speedConfig.getValue();
      }

      if (((FlightModule.FlightMode)this.modeConfig.getValue()).equals(FlightModule.FlightMode.VANILLA)) {
         mc.field_1724.method_31549().method_7248((float)(this.speed * 0.05000000074505806D));
      } else {
         mc.field_1724.method_31549().method_7248(0.05F);
      }

      if (this.antiKickTimer.passed(3900) && (Boolean)this.antiKickConfig.getValue()) {
         Managers.MOVEMENT.setMotionY(-0.04D);
         this.antiKickTimer.reset();
      } else if (this.antiKick2Timer.passed(4000) && (Boolean)this.antiKickConfig.getValue()) {
         Managers.MOVEMENT.setMotionY(0.04D);
         this.antiKick2Timer.reset();
      } else if (this.modeConfig.getValue() == FlightModule.FlightMode.NORMAL) {
         Managers.MOVEMENT.setMotionY(0.0D);
         if (mc.field_1690.field_1903.method_1434()) {
            Managers.MOVEMENT.setMotionY((double)(Float)this.vspeedConfig.getValue());
         } else if (mc.field_1690.field_1832.method_1434()) {
            Managers.MOVEMENT.setMotionY((double)(-(Float)this.vspeedConfig.getValue()));
         }
      }

      if (this.modeConfig.getValue() == FlightModule.FlightMode.NORMAL) {
         this.speed = Math.max(this.speed, 0.2872999906539917D);
         float forward = mc.field_1724.field_3913.field_3905;
         float strafe = mc.field_1724.field_3913.field_3907;
         float yaw = mc.field_1724.method_36454();
         if (forward == 0.0F && strafe == 0.0F) {
            Managers.MOVEMENT.setMotionXZ(0.0D, 0.0D);
            return;
         }

         double rx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
         double rz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
         Managers.MOVEMENT.setMotionXZ((double)forward * this.speed * rx + (double)strafe * this.speed * rz, (double)forward * this.speed * rz - (double)strafe * this.speed * rx);
      }

   }

   @EventListener
   public void onConfigUpdate(ConfigUpdateEvent event) {
      if (event.getConfig() == this.modeConfig && event.getStage() == EventStage.POST) {
         if (this.modeConfig.getValue() == FlightModule.FlightMode.VANILLA) {
            this.enableVanillaFly();
         } else {
            this.disableVanillaFly();
         }
      }

   }

   private void enableVanillaFly() {
      mc.field_1724.method_31549().field_7478 = true;
      mc.field_1724.method_31549().field_7479 = true;
   }

   private void disableVanillaFly() {
      if (!mc.field_1724.method_7337()) {
         mc.field_1724.method_31549().field_7478 = false;
      }

      mc.field_1724.method_31549().field_7479 = false;
      mc.field_1724.method_31549().method_7248(0.05F);
   }

   public static enum FlightMode {
      NORMAL,
      VANILLA;

      // $FF: synthetic method
      private static FlightModule.FlightMode[] $values() {
         return new FlightModule.FlightMode[]{NORMAL, VANILLA};
      }
   }
}
