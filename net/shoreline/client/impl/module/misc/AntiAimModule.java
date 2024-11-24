package net.shoreline.client.impl.module.misc;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.impl.event.network.PlayerTickEvent;

public class AntiAimModule extends RotationModule {
   Config<AntiAimModule.YawMode> yawModeConfig;
   Config<AntiAimModule.PitchMode> pitchModeConfig;
   Config<Float> yawAddConfig;
   Config<Float> pitchAddConfig;
   Config<Float> spinSpeedConfig;
   Config<Integer> flipTicksConfig;
   private float yaw;
   private float pitch;
   private float prevYaw;
   private float prevPitch;

   public AntiAimModule() {
      super("AntiAim", "Makes it harder to accurately aim at the player", ModuleCategory.MISCELLANEOUS, 50);
      this.yawModeConfig = new EnumConfig("Yaw", "The mode for the rotation yaw spin ", AntiAimModule.YawMode.SPIN, AntiAimModule.YawMode.values());
      this.pitchModeConfig = new EnumConfig("Pitch", "The mode for the rotation pitch spin", AntiAimModule.PitchMode.DOWN, AntiAimModule.PitchMode.values());
      this.yawAddConfig = new NumberConfig("YawAdd", "The yaw to add during each rotation", -180.0F, 20.0F, 180.0F);
      this.pitchAddConfig = new NumberConfig("CustomPitch", "The pitch to add during each rotation", -90.0F, 20.0F, 90.0F);
      this.spinSpeedConfig = new NumberConfig("SpinSpeed", "The yaw speed to rotate", 1.0F, 16.0F, 40.0F);
      this.flipTicksConfig = new NumberConfig("FlipTicks", "The number of ticks to wait between jitter", 2, 2, 20);
   }

   public void onEnable() {
      if (mc.field_1724 != null) {
         this.prevYaw = mc.field_1724.method_36454();
         this.prevPitch = mc.field_1724.method_36455();
      }
   }

   @EventListener
   public void onPlayerUpdate(PlayerTickEvent event) {
      if (!mc.field_1690.field_1886.method_1434() && !mc.field_1690.field_1904.method_1434()) {
         float var10001;
         float jitter;
         switch((AntiAimModule.YawMode)this.yawModeConfig.getValue()) {
         case OFF:
            var10001 = mc.field_1724.method_36454();
            break;
         case STATIC:
            var10001 = mc.field_1724.method_36454() + (Float)this.yawAddConfig.getValue();
            break;
         case ZERO:
            var10001 = this.prevYaw;
            break;
         case SPIN:
            jitter = this.yaw + (Float)this.spinSpeedConfig.getValue();
            var10001 = jitter > 360.0F ? jitter - 360.0F : jitter;
            break;
         case JITTER:
            var10001 = mc.field_1724.method_36454() + (mc.field_1724.field_6012 % (Integer)this.flipTicksConfig.getValue() == 0 ? (Float)this.yawAddConfig.getValue() : -(Float)this.yawAddConfig.getValue());
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         this.yaw = var10001;
         switch((AntiAimModule.PitchMode)this.pitchModeConfig.getValue()) {
         case OFF:
            var10001 = mc.field_1724.method_36455();
            break;
         case STATIC:
            var10001 = (Float)this.pitchAddConfig.getValue();
            break;
         case ZERO:
            var10001 = this.prevPitch;
            break;
         case UP:
            var10001 = -90.0F;
            break;
         case DOWN:
            var10001 = 90.0F;
            break;
         case JITTER:
            jitter = this.pitch + 30.0F;
            var10001 = jitter > 90.0F ? -90.0F : (jitter < -90.0F ? 90.0F : jitter);
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         this.pitch = var10001;
         this.setRotation(this.yaw, this.pitch);
      }
   }

   public static enum YawMode {
      OFF,
      STATIC,
      ZERO,
      SPIN,
      JITTER;

      // $FF: synthetic method
      private static AntiAimModule.YawMode[] $values() {
         return new AntiAimModule.YawMode[]{OFF, STATIC, ZERO, SPIN, JITTER};
      }
   }

   public static enum PitchMode {
      OFF,
      STATIC,
      ZERO,
      UP,
      DOWN,
      JITTER;

      // $FF: synthetic method
      private static AntiAimModule.PitchMode[] $values() {
         return new AntiAimModule.PitchMode[]{OFF, STATIC, ZERO, UP, DOWN, JITTER};
      }
   }
}
