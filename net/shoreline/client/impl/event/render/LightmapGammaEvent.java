package net.shoreline.client.impl.event.render;

import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class LightmapGammaEvent extends Event {
   private int gamma;

   public LightmapGammaEvent(int gamma) {
      this.gamma = gamma;
   }

   public int getGamma() {
      return this.gamma;
   }

   public void setGamma(int gamma) {
      this.gamma = gamma;
   }
}
