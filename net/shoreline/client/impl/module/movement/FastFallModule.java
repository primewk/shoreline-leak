package net.shoreline.client.impl.module.movement;

import net.minecraft.class_238;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.entity.player.PlayerMoveEvent;
import net.shoreline.client.impl.event.network.TickMovementEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;

public class FastFallModule extends ToggleModule {
   Config<Float> heightConfig = new NumberConfig("Height", "The maximum fall height", 1.0F, 3.0F, 10.0F);
   Config<FastFallModule.FallMode> fallModeConfig;
   Config<Integer> shiftTicksConfig;
   private boolean prevOnGround;
   private boolean cancelFallMovement;
   private int fallTicks;
   private final Timer fallTimer;

   public FastFallModule() {
      super("FastFall", "Falls down blocks faster", ModuleCategory.MOVEMENT);
      this.fallModeConfig = new EnumConfig("Mode", "The mode for falling down blocks", FastFallModule.FallMode.STEP, FastFallModule.FallMode.values());
      this.shiftTicksConfig = new NumberConfig("ShiftTicks", "Number of ticks to shift ahead", 1, 3, 5, () -> {
         return this.fallModeConfig.getValue() == FastFallModule.FallMode.SHIFT;
      });
      this.fallTimer = new CacheTimer();
   }

   public void onDisable() {
      this.cancelFallMovement = false;
      this.fallTicks = 0;
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         this.prevOnGround = mc.field_1724.method_24828();
         if (this.fallModeConfig.getValue() == FastFallModule.FallMode.STEP) {
            if (!mc.field_1724.method_3144() && !mc.field_1724.method_6128() && !mc.field_1724.method_21754() && !mc.field_1724.method_5771() && !mc.field_1724.method_5799() && !mc.field_1724.field_3913.field_3904 && !mc.field_1724.field_3913.field_3903) {
               if (!Modules.SPEED.isEnabled() && !Modules.LONG_JUMP.isEnabled() && !Modules.FLIGHT.isEnabled() && !Modules.PACKET_FLY.isEnabled()) {
                  if (mc.field_1724.method_24828() && this.isNearestBlockWithinHeight((double)(Float)this.heightConfig.getValue())) {
                     Managers.MOVEMENT.setMotionY(-3.0D);
                  }

                  return;
               }

               return;
            }

            return;
         }
      }

   }

   @EventListener
   public void onTickMovement(TickMovementEvent event) {
      if (this.fallModeConfig.getValue() == FastFallModule.FallMode.SHIFT) {
         if (mc.field_1724.method_3144() || mc.field_1724.method_6128() || mc.field_1724.method_21754() || mc.field_1724.method_5771() || mc.field_1724.method_5799() || mc.field_1724.field_3913.field_3904 || mc.field_1724.field_3913.field_3903) {
            return;
         }

         if (!Managers.ANTICHEAT.hasPassed(1000L) || !this.fallTimer.passed(1000) || Modules.SPEED.isEnabled() || Modules.LONG_JUMP.isEnabled() || Modules.FLIGHT.isEnabled() || Modules.PACKET_FLY.isEnabled()) {
            return;
         }

         if (mc.field_1724.method_18798().field_1351 < 0.0D && this.prevOnGround && !mc.field_1724.method_24828() && this.isNearestBlockWithinHeight((double)(Float)this.heightConfig.getValue() + 0.01D)) {
            this.fallTimer.reset();
            event.cancel();
            event.setIterations((Integer)this.shiftTicksConfig.getValue());
            this.cancelFallMovement = true;
            this.fallTicks = 0;
         }
      }

   }

   @EventListener
   public void onPlayerMove(PlayerMoveEvent event) {
      if (!Modules.FLIGHT.isEnabled() && !Modules.PACKET_FLY.isEnabled()) {
         if (this.cancelFallMovement && this.fallModeConfig.getValue() == FastFallModule.FallMode.SHIFT) {
            event.setX(0.0D);
            event.setZ(0.0D);
            Managers.MOVEMENT.setMotionXZ(0.0D, 0.0D);
            ++this.fallTicks;
            if (this.fallTicks > (Integer)this.shiftTicksConfig.getValue()) {
               this.cancelFallMovement = false;
               this.fallTicks = 0;
            }
         }

      }
   }

   private boolean isNearestBlockWithinHeight(double height) {
      class_238 bb = mc.field_1724.method_5829();

      for(double i = 0.0D; i < height + 0.5D; i += 0.01D) {
         if (!mc.field_1687.method_8587(mc.field_1724, bb.method_989(0.0D, -i, 0.0D))) {
            return true;
         }
      }

      return false;
   }

   public static enum FallMode {
      STEP,
      SHIFT;

      // $FF: synthetic method
      private static FastFallModule.FallMode[] $values() {
         return new FastFallModule.FallMode[]{STEP, SHIFT};
      }
   }
}
