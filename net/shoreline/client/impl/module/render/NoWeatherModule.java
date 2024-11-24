package net.shoreline.client.impl.module.render;

import net.minecraft.class_2398;
import net.minecraft.class_2596;
import net.minecraft.class_2668;
import net.minecraft.class_4761;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.biome.BiomeEffectsEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.util.string.EnumFormatter;

public class NoWeatherModule extends ToggleModule {
   Config<NoWeatherModule.Weather> weatherConfig;
   private NoWeatherModule.Weather weather;

   public NoWeatherModule() {
      super("NoWeather", "Prevents weather rendering", ModuleCategory.RENDER);
      this.weatherConfig = new EnumConfig("Weather", "The world weather", NoWeatherModule.Weather.CLEAR, NoWeatherModule.Weather.values());
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.weatherConfig.getValue());
   }

   public void onEnable() {
      if (mc.field_1687 != null) {
         if (mc.field_1687.method_8546()) {
            this.weather = NoWeatherModule.Weather.THUNDER;
         } else if (mc.field_1687.method_8419()) {
            this.weather = NoWeatherModule.Weather.RAIN;
         } else {
            this.weather = NoWeatherModule.Weather.CLEAR;
         }

         this.setWeather((NoWeatherModule.Weather)this.weatherConfig.getValue());
      }

   }

   public void onDisable() {
      if (mc.field_1687 != null && this.weather != null) {
         this.setWeather(this.weather);
      }

   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.POST) {
         this.setWeather((NoWeatherModule.Weather)this.weatherConfig.getValue());
      }

   }

   @EventListener
   public void onBiomeEffects(BiomeEffectsEvent event) {
      if (this.weatherConfig.getValue() == NoWeatherModule.Weather.ASH) {
         event.cancel();
         event.setParticleConfig(new class_4761(class_2398.field_23956, 0.118093334F));
      }

   }

   private void setWeather(NoWeatherModule.Weather weather) {
      switch(weather) {
      case CLEAR:
      case ASH:
         mc.field_1687.method_28104().method_157(false);
         mc.field_1687.method_8519(0.0F);
         mc.field_1687.method_8496(0.0F);
         break;
      case RAIN:
         mc.field_1687.method_28104().method_157(true);
         mc.field_1687.method_8519(1.0F);
         mc.field_1687.method_8496(0.0F);
         break;
      case THUNDER:
         mc.field_1687.method_28104().method_157(true);
         mc.field_1687.method_8519(2.0F);
         mc.field_1687.method_8496(1.0F);
      }

   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2668) {
         class_2668 packet = (class_2668)var3;
         if (packet.method_11491() == class_2668.field_25646 || packet.method_11491() == class_2668.field_25647 || packet.method_11491() == class_2668.field_25652 || packet.method_11491() == class_2668.field_25653) {
            event.cancel();
         }
      }

   }

   public static enum Weather {
      CLEAR,
      RAIN,
      THUNDER,
      ASH;

      // $FF: synthetic method
      private static NoWeatherModule.Weather[] $values() {
         return new NoWeatherModule.Weather[]{CLEAR, RAIN, THUNDER, ASH};
      }
   }
}
