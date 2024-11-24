package net.shoreline.client.impl.module.movement;

import net.minecraft.class_1294;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.SprintCancelEvent;
import net.shoreline.client.util.player.MovementUtil;
import net.shoreline.client.util.string.EnumFormatter;

public class SprintModule extends ToggleModule {
   Config<SprintModule.SprintMode> modeConfig;

   public SprintModule() {
      super("Sprint", "Automatically sprints", ModuleCategory.MOVEMENT);
      this.modeConfig = new EnumConfig("Mode", "Sprinting mode. Rage allows for multi-directional sprinting.", SprintModule.SprintMode.LEGIT, SprintModule.SprintMode.values());
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (MovementUtil.isInputtingMovement() && !mc.field_1724.method_5715() && !mc.field_1724.method_3144() && !mc.field_1724.method_5799() && !mc.field_1724.method_5771() && !mc.field_1724.method_21754() && !mc.field_1724.method_6059(class_1294.field_5919) && (float)mc.field_1724.method_7344().method_7586() > 6.0F) {
            switch((SprintModule.SprintMode)this.modeConfig.getValue()) {
            case LEGIT:
               if (mc.field_1724.field_3913.method_20622() && (!mc.field_1724.field_5976 || mc.field_1724.field_34927)) {
                  mc.field_1724.method_5728(true);
               }
               break;
            case RAGE:
               mc.field_1724.method_5728(true);
            }
         }

      }
   }

   @EventListener
   public void onSprintCancel(SprintCancelEvent event) {
      if (MovementUtil.isInputtingMovement() && !mc.field_1724.method_5715() && !mc.field_1724.method_3144() && !mc.field_1724.method_5799() && !mc.field_1724.method_5771() && !mc.field_1724.method_21754() && !mc.field_1724.method_6059(class_1294.field_5919) && (float)mc.field_1724.method_7344().method_7586() > 6.0F && this.modeConfig.getValue() == SprintModule.SprintMode.RAGE) {
         event.cancel();
      }

   }

   public static enum SprintMode {
      LEGIT,
      RAGE;

      // $FF: synthetic method
      private static SprintModule.SprintMode[] $values() {
         return new SprintModule.SprintMode[]{LEGIT, RAGE};
      }
   }
}
