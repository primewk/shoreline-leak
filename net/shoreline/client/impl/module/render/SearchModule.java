package net.shoreline.client.impl.module.render;

import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;

public class SearchModule extends ToggleModule {
   public SearchModule() {
      super("Search", "Highlights specified blocks in the world", ModuleCategory.RENDER);
   }
}
