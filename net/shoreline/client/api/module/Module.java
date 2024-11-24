package net.shoreline.client.api.module;

import net.shoreline.client.api.config.ConfigContainer;
import net.shoreline.client.util.Globals;
import net.shoreline.client.util.chat.ChatUtil;

public class Module extends ConfigContainer implements Globals {
   public static final String MODULE_ID_FORMAT = "%s-module";
   private final String desc;
   private final ModuleCategory category;

   public Module(String name, String desc, ModuleCategory category) {
      super(name);
      this.desc = desc;
      this.category = category;
   }

   protected void sendModuleMessage(String message) {
      ChatUtil.clientSendMessageRaw("§s[%s]§f %s", this.name, message);
   }

   protected void sendModuleMessage(String message, Object... params) {
      this.sendModuleMessage(String.format(message, params));
   }

   public String getId() {
      return String.format("%s-module", this.name.toLowerCase());
   }

   public String getDescription() {
      return this.desc;
   }

   public ModuleCategory getCategory() {
      return this.category;
   }

   public String getModuleData() {
      return "ARRAYLIST_INFO";
   }
}
