package net.shoreline.client.impl.module.combat;

import net.minecraft.class_1297;
import net.minecraft.class_3966;
import net.minecraft.class_239.class_240;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.imixin.IMinecraftClient;
import net.shoreline.client.init.Managers;
import net.shoreline.client.mixin.accessor.AccessorMinecraftClient;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;

public class TriggerModule extends ToggleModule {
   Config<TriggerModule.TriggerMode> modeConfig;
   Config<Float> attackSpeedConfig;
   Config<Float> randomSpeedConfig;
   private final Timer triggerTimer;

   public TriggerModule() {
      super("Trigger", "Automatically attacks entities in the crosshair", ModuleCategory.COMBAT);
      this.modeConfig = new EnumConfig("Mode", "The mode for activating the trigger bot", TriggerModule.TriggerMode.MOUSE_BUTTON, TriggerModule.TriggerMode.values());
      this.attackSpeedConfig = new NumberConfig("AttackSpeed", "The speed to attack entities", 0.1F, 8.0F, 20.0F);
      this.randomSpeedConfig = new NumberConfig("RandomSpeed", "The speed randomizer for attacks", 0.1F, 2.0F, 10.0F);
      this.triggerTimer = new CacheTimer();
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         boolean var10000;
         switch((TriggerModule.TriggerMode)this.modeConfig.getValue()) {
         case MOUSE_BUTTON:
            var10000 = mc.field_1729.method_1608();
            break;
         case MOUSE_OVER:
            if (mc.field_1765 != null && mc.field_1765.method_17783() == class_240.field_1331) {
               class_3966 entityHit = (class_3966)mc.field_1765;
               class_1297 crosshairEntity = entityHit.method_17782();
               var10000 = !mc.field_1724.method_5722(crosshairEntity) && !Managers.SOCIAL.isFriend(crosshairEntity.method_5477());
            } else {
               var10000 = false;
            }
            break;
         case MOUSE_CLICK:
            var10000 = true;
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         boolean buttonDown = var10000;
         double d = Math.random() * (double)(Float)this.randomSpeedConfig.getValue() * 2.0D - (double)(Float)this.randomSpeedConfig.getValue();
         if (buttonDown && this.triggerTimer.passed(1000.0D - Math.max((double)(Float)this.attackSpeedConfig.getValue() + d, 0.5D) * 50.0D)) {
            ((IMinecraftClient)mc).leftClick();
            ((AccessorMinecraftClient)mc).hookSetAttackCooldown(0);
            this.triggerTimer.reset();
         }

      }
   }

   public static enum TriggerMode {
      MOUSE_BUTTON,
      MOUSE_OVER,
      MOUSE_CLICK;

      // $FF: synthetic method
      private static TriggerModule.TriggerMode[] $values() {
         return new TriggerModule.TriggerMode[]{MOUSE_BUTTON, MOUSE_OVER, MOUSE_CLICK};
      }
   }
}
