package net.shoreline.client.api.module;

import net.shoreline.client.Shoreline;

public class ConcurrentModule extends Module {
   public ConcurrentModule(String name, String desc, ModuleCategory category) {
      super(name, desc, category);
      Shoreline.EVENT_HANDLER.subscribe(this);
   }
}
