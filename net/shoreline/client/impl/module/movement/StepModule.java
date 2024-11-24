package net.shoreline.client.impl.module.movement;

import net.minecraft.class_1297;
import net.minecraft.class_1498;
import net.minecraft.class_1500;
import net.minecraft.class_1501;
import net.minecraft.class_2708;
import net.minecraft.class_2828.class_2829;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;
import net.shoreline.client.util.string.EnumFormatter;

public class StepModule extends ToggleModule {
   Config<StepModule.StepMode> modeConfig;
   Config<Float> heightConfig;
   Config<Boolean> useTimerConfig;
   Config<Boolean> strictConfig;
   Config<Boolean> entityStepConfig;
   private boolean cancelTimer;
   private final Timer stepTimer;

   public StepModule() {
      super("Step", "Allows the player to step up blocks", ModuleCategory.MOVEMENT);
      this.modeConfig = new EnumConfig("Mode", "Step mode", StepModule.StepMode.NORMAL, StepModule.StepMode.values());
      this.heightConfig = new NumberConfig("Height", "The maximum height for stepping up blocks", 1.0F, 2.5F, 10.0F);
      this.useTimerConfig = new BooleanConfig("UseTimer", "Slows down packets by applying timer when stepping", true, () -> {
         return this.modeConfig.getValue() == StepModule.StepMode.NORMAL;
      });
      this.strictConfig = new BooleanConfig("Strict", "Confirms the step height for NCP servers", false, () -> {
         return (Float)this.heightConfig.getValue() <= 2.5F;
      });
      this.entityStepConfig = new BooleanConfig("EntityStep", "Allows entities to step up blocks", false);
      this.stepTimer = new CacheTimer();
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
   }

   public void onDisable() {
      if (mc.field_1724 != null) {
         this.setStepHeight(this.isAbstractHorse(mc.field_1724.method_5854()) ? 1.0F : 0.6F);
         Managers.TICK.setClientTick(1.0F);
      }
   }

   @EventListener
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (this.modeConfig.getValue() == StepModule.StepMode.NORMAL) {
            double stepHeight = mc.field_1724.method_23318() - mc.field_1724.field_6036;
            if (stepHeight <= 0.5D || stepHeight > (double)(Float)this.heightConfig.getValue()) {
               return;
            }

            double[] offs = this.getStepOffsets(stepHeight);
            if ((Boolean)this.useTimerConfig.getValue()) {
               Managers.TICK.setClientTick(stepHeight > 1.0D ? 0.15F : 0.35F);
               this.cancelTimer = true;
            }

            double[] var5 = offs;
            int var6 = offs.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               double off = var5[var7];
               Managers.NETWORK.sendPacket(new class_2829(mc.field_1724.field_6014, mc.field_1724.field_6036 + off, mc.field_1724.field_5969, false));
            }

            this.stepTimer.reset();
         }

      }
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (!mc.field_1724.method_5799() && !mc.field_1724.method_5771() && !mc.field_1724.method_6128()) {
            if (this.cancelTimer && mc.field_1724.method_24828()) {
               Managers.TICK.setClientTick(1.0F);
               this.cancelTimer = false;
            }

            if (mc.field_1724.method_24828() && this.stepTimer.passed(200)) {
               this.setStepHeight((Float)this.heightConfig.getValue());
            } else {
               this.setStepHeight(this.isAbstractHorse(mc.field_1724.method_5854()) ? 1.0F : 0.6F);
            }

         } else {
            Managers.TICK.setClientTick(1.0F);
            this.setStepHeight(this.isAbstractHorse(mc.field_1724.method_5854()) ? 1.0F : 0.6F);
         }
      }
   }

   @EventListener
   public void onPacketInbound(PacketEvent event) {
      if (event.getPacket() instanceof class_2708) {
         this.disable();
      }

   }

   private void setStepHeight(float stepHeight) {
      if ((Boolean)this.entityStepConfig.getValue() && mc.field_1724.method_5854() != null) {
         mc.field_1724.method_5854().method_49477(stepHeight);
      } else {
         mc.field_1724.method_49477(stepHeight);
      }

   }

   private double[] getStepOffsets(double stepHeight) {
      double[] offsets = new double[0];
      if ((Boolean)this.strictConfig.getValue()) {
         if (stepHeight > 1.1661D) {
            offsets = new double[]{0.42D, 0.7532D, 1.001D, 1.1661D, stepHeight};
         } else if (stepHeight > 1.015D) {
            offsets = new double[]{0.42D, 0.7532D, 1.001D, stepHeight};
         } else if (stepHeight > 0.6D) {
            offsets = new double[]{0.42D * stepHeight, 0.7532D * stepHeight, stepHeight};
         }

         return offsets;
      } else {
         if (stepHeight > 2.019D) {
            offsets = new double[]{0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.919D};
         } else if (stepHeight > 1.5D) {
            offsets = new double[]{0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D};
         } else if (stepHeight > 1.015D) {
            offsets = new double[]{0.42D, 0.7532D, 1.01D, 1.093D, 1.015D};
         } else if (stepHeight > 0.6D) {
            offsets = new double[]{0.42D * stepHeight, 0.7532D * stepHeight};
         }

         return offsets;
      }
   }

   private boolean isAbstractHorse(class_1297 e) {
      return e instanceof class_1498 || e instanceof class_1501 || e instanceof class_1500;
   }

   public static enum StepMode {
      VANILLA,
      NORMAL,
      A_A_C;

      // $FF: synthetic method
      private static StepModule.StepMode[] $values() {
         return new StepModule.StepMode[]{VANILLA, NORMAL, A_A_C};
      }
   }
}
