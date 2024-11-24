package net.shoreline.client.impl.module.client;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.module.ConcurrentModule;
import net.shoreline.client.api.module.ModuleCategory;

public class ChatModule extends ConcurrentModule {
   Config<Boolean> debugConfig = new BooleanConfig("ChatDebug", "Allows client debug messages to be printed in the chat", false);

   public ChatModule() {
      super("Chat", "Manages the client chat", ModuleCategory.CLIENT);
   }
}
