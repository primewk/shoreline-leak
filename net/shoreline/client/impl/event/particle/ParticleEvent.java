package net.shoreline.client.impl.event.particle;

import net.minecraft.class_2394;
import net.minecraft.class_2396;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class ParticleEvent extends Event {
   private final class_2394 particle;

   public ParticleEvent(class_2394 particle) {
      this.particle = particle;
   }

   public class_2394 getParticle() {
      return this.particle;
   }

   public class_2396<?> getParticleType() {
      return this.particle.method_10295();
   }

   @Cancelable
   public static class Emitter extends ParticleEvent {
      public Emitter(class_2394 particle) {
         super(particle);
      }
   }
}
