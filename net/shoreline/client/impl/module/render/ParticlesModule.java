package net.shoreline.client.impl.module.render;

import java.awt.Color;
import net.minecraft.class_2398;
import net.minecraft.class_3532;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.particle.ParticleEvent;
import net.shoreline.client.impl.event.particle.TotemParticleEvent;

public class ParticlesModule extends ToggleModule {
   Config<ParticlesModule.TotemParticle> totemConfig;
   Config<Color> totemColorConfig;
   Config<Boolean> fireworkConfig;
   Config<Boolean> potionConfig;
   Config<Boolean> bottleConfig;
   Config<Boolean> portalConfig;

   public ParticlesModule() {
      super("Particles", "Change the rendering of particles", ModuleCategory.RENDER);
      this.totemConfig = new EnumConfig("Totem", "Renders totem particles", ParticlesModule.TotemParticle.OFF, ParticlesModule.TotemParticle.values());
      this.totemColorConfig = new ColorConfig("TotemColor", "Color of the totem particles", new Color(25, 120, 0), false, false, () -> {
         return this.totemConfig.getValue() == ParticlesModule.TotemParticle.COLOR;
      });
      this.fireworkConfig = new BooleanConfig("Firework", "Renders firework particles", false);
      this.potionConfig = new BooleanConfig("Effects", "Renders potion effect particles", true);
      this.bottleConfig = new BooleanConfig("BottleSplash", "Render bottle splash particles", true);
      this.portalConfig = new BooleanConfig("Portal", "Render portal particles", true);
   }

   @EventListener
   public void onParticle(ParticleEvent event) {
      if ((Boolean)this.potionConfig.getValue() && event.getParticleType() == class_2398.field_11226 || (Boolean)this.fireworkConfig.getValue() && event.getParticleType() == class_2398.field_11248 || (Boolean)this.bottleConfig.getValue() && (event.getParticleType() == class_2398.field_11245 || event.getParticleType() == class_2398.field_11213) || (Boolean)this.portalConfig.getValue() && event.getParticleType() == class_2398.field_11214) {
         event.cancel();
      }

   }

   @EventListener
   public void onTotemParticle(TotemParticleEvent event) {
      if (this.totemConfig.getValue() == ParticlesModule.TotemParticle.COLOR) {
         event.cancel();
         Color color = (Color)this.totemColorConfig.getValue();
         float r = (float)color.getRed() / 255.0F;
         float g = (float)color.getGreen() / 255.0F;
         float b = (float)color.getBlue() / 255.0F;
         event.setColor(new Color(class_3532.method_15363(r + RANDOM.nextFloat() * 0.1F, 0.0F, 1.0F), class_3532.method_15363(g + RANDOM.nextFloat() * 0.1F, 0.0F, 1.0F), class_3532.method_15363(b + RANDOM.nextFloat() * 0.1F, 0.0F, 1.0F)));
      }

   }

   @EventListener
   public void onParticleEmitter(ParticleEvent.Emitter event) {
      if (this.totemConfig.getValue() == ParticlesModule.TotemParticle.REMOVE && event.getParticleType() == class_2398.field_11220) {
         event.cancel();
      }

   }

   private static enum TotemParticle {
      OFF,
      REMOVE,
      COLOR;

      // $FF: synthetic method
      private static ParticlesModule.TotemParticle[] $values() {
         return new ParticlesModule.TotemParticle[]{OFF, REMOVE, COLOR};
      }
   }
}
