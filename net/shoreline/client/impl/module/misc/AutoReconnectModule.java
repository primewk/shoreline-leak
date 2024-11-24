package net.shoreline.client.impl.module.misc;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;

public class AutoReconnectModule extends ToggleModule {
   Config<Integer> delayConfig = new NumberConfig("Delay", "The delay between reconnects to a server", 0, 5, 100);

   public AutoReconnectModule() {
      super("AutoReconnect", "Automatically reconnects to a server immediately after disconnecting", ModuleCategory.MISCELLANEOUS);
   }

   public int getDelay() {
      return (Integer)this.delayConfig.getValue();
   }
}
