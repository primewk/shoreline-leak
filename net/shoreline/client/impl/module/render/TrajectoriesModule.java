package net.shoreline.client.impl.module.render;

import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;

public class TrajectoriesModule extends ToggleModule {
   public TrajectoriesModule() {
      super("Trajectories", "Renders the trajectory path of projectiles", ModuleCategory.RENDER);
   }
}
