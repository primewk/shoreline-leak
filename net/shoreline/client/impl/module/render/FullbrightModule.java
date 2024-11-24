package net.shoreline.client.impl.module.render;

import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.config.ConfigUpdateEvent;
import net.shoreline.client.impl.event.network.GameJoinEvent;
import net.shoreline.client.impl.event.render.LightmapGammaEvent;

public class FullbrightModule extends ToggleModule {
   Config<FullbrightModule.Brightness> brightnessConfig;

   public FullbrightModule() {
      super("Fullbright", "Brightens the world", ModuleCategory.RENDER);
      this.brightnessConfig = new EnumConfig("Mode", "Mode for world brightness", FullbrightModule.Brightness.GAMMA, FullbrightModule.Brightness.values());
   }

   public void onEnable() {
      if (mc.field_1724 != null && mc.field_1687 != null && this.brightnessConfig.getValue() == FullbrightModule.Brightness.POTION) {
         mc.field_1724.method_6092(new class_1293(class_1294.field_5925, -1, 0));
      }

   }

   public void onDisable() {
      if (mc.field_1724 != null && mc.field_1687 != null && this.brightnessConfig.getValue() == FullbrightModule.Brightness.POTION) {
         mc.field_1724.method_6016(class_1294.field_5925);
      }

   }

   @EventListener
   public void onGameJoin(GameJoinEvent event) {
      this.onDisable();
      this.onEnable();
   }

   @EventListener
   public void onLightmapGamma(LightmapGammaEvent event) {
      if (this.brightnessConfig.getValue() == FullbrightModule.Brightness.GAMMA) {
         event.cancel();
         event.setGamma(-1);
      }

   }

   @EventListener
   public void onConfigUpdate(ConfigUpdateEvent event) {
      if (mc.field_1724 != null && this.brightnessConfig == event.getConfig() && event.getStage() == EventStage.POST && this.brightnessConfig.getValue() != FullbrightModule.Brightness.POTION) {
         mc.field_1724.method_6016(class_1294.field_5925);
      }

   }

   @EventListener
   public void onTick(TickEvent event) {
      if (this.brightnessConfig.getValue() == FullbrightModule.Brightness.POTION && !mc.field_1724.method_6059(class_1294.field_5925)) {
         mc.field_1724.method_6092(new class_1293(class_1294.field_5925, -1, 0));
      }

   }

   public static enum Brightness {
      GAMMA,
      POTION;

      // $FF: synthetic method
      private static FullbrightModule.Brightness[] $values() {
         return new FullbrightModule.Brightness[]{GAMMA, POTION};
      }
   }
}
