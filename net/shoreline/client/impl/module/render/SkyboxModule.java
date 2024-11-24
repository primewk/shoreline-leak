package net.shoreline.client.impl.module.render;

import java.awt.Color;
import net.minecraft.class_2761;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.world.SkyboxEvent;

public class SkyboxModule extends ToggleModule {
   Config<Integer> dayTimeConfig = new NumberConfig("WorldTime", "The world time of day", 0, 6000, 24000);
   Config<Boolean> skyConfig = new BooleanConfig("Sky", "Changes the world skybox color", true);
   Config<Color> skyColorConfig = new ColorConfig("SkyColor", "The color for the world skybox", new Color(255, 0, 0), false, true, () -> {
      return (Boolean)this.skyConfig.getValue();
   });
   Config<Boolean> cloudConfig = new BooleanConfig("Cloud", "Changes the world cloud color", false);
   Config<Color> cloudColorConfig = new ColorConfig("CloudColor", "The color for the world clouds", new Color(255, 0, 0), false, true, () -> {
      return (Boolean)this.cloudConfig.getValue();
   });
   Config<Boolean> fogConfig = new BooleanConfig("Fog", "Changes the world fog color", false);
   Config<Color> fogColorConfig = new ColorConfig("FogColor", "The color for the world fog", new Color(255, 0, 0), false, true, () -> {
      return (Boolean)this.fogConfig.getValue();
   });

   public SkyboxModule() {
      super("Skybox", "Changes the rendering of the world skybox", ModuleCategory.RENDER);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.POST) {
         mc.field_1687.method_8435((long)(Integer)this.dayTimeConfig.getValue());
      }

   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (event.getPacket() instanceof class_2761) {
         event.cancel();
      }

   }

   @EventListener
   public void onSkyboxSky(SkyboxEvent.Sky event) {
      if ((Boolean)this.skyConfig.getValue()) {
         event.cancel();
         event.setColor((Color)this.skyColorConfig.getValue());
      }

   }

   @EventListener
   public void onSkyboxCloud(SkyboxEvent.Cloud event) {
      if ((Boolean)this.cloudConfig.getValue()) {
         event.cancel();
         event.setColor((Color)this.cloudColorConfig.getValue());
      }

   }

   @EventListener
   public void onSkyboxFog(SkyboxEvent.Fog event) {
      if ((Boolean)this.fogConfig.getValue()) {
         event.cancel();
         event.setColor((Color)this.fogColorConfig.getValue());
      }

   }
}
