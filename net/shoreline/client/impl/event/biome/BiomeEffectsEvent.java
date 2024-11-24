package net.shoreline.client.impl.event.biome;

import net.minecraft.class_4761;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class BiomeEffectsEvent extends Event {
   private class_4761 particleConfig;

   public class_4761 getParticleConfig() {
      return this.particleConfig;
   }

   public void setParticleConfig(class_4761 particleConfig) {
      this.particleConfig = particleConfig;
   }
}
