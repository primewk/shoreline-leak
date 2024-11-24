package net.shoreline.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Iterator;
import net.minecraft.class_2172;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.chat.ChatUtil;

public class DisableAllCommand extends Command {
   public DisableAllCommand() {
      super("DisableAll", "Disables all enabled modules", literal("disableall"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((c) -> {
         Iterator var1 = Managers.MODULE.getModules().iterator();

         while(var1.hasNext()) {
            Module module = (Module)var1.next();
            if (module instanceof ToggleModule) {
               ToggleModule toggleModule = (ToggleModule)module;
               if (toggleModule.isEnabled()) {
                  toggleModule.disable();
               }
            }
         }

         ChatUtil.clientSendMessage("All modules are disabled");
         return 1;
      });
   }
}
