package net.shoreline.client.impl.event.gui.click;

import net.shoreline.client.api.event.Event;
import net.shoreline.client.api.module.ToggleModule;

public class ToggleGuiEvent extends Event {
   private final ToggleModule module;

   public ToggleGuiEvent(ToggleModule module) {
      this.module = module;
   }

   public ToggleModule getModule() {
      return this.module;
   }

   public boolean isEnabled() {
      return this.module.isEnabled();
   }
}
