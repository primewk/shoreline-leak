package net.shoreline.client.impl.module.client;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;

public class FontModule extends ToggleModule {
   Config<Boolean> shadowConfig = new BooleanConfig("Shadow", "Renders text with a shadow background", true);

   public FontModule() {
      super("Font", "Changes the client text to custom font rendering", ModuleCategory.CLIENT);
   }

   public boolean getShadow() {
      return (Boolean)this.shadowConfig.getValue();
   }
}
