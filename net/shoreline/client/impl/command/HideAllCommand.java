package net.shoreline.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Iterator;
import net.minecraft.class_2172;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.chat.ChatUtil;

public class HideAllCommand extends Command {
   public HideAllCommand() {
      super("HideAll", "Hides all modules from the arraylist", literal("hideall"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((c) -> {
         Iterator var1 = Managers.MODULE.getModules().iterator();

         while(var1.hasNext()) {
            Module module = (Module)var1.next();
            if (module instanceof ToggleModule) {
               ToggleModule toggleModule = (ToggleModule)module;
               if (!toggleModule.isHidden()) {
                  toggleModule.setHidden(true);
               }
            }
         }

         ChatUtil.clientSendMessage("All modules are hidden");
         return 1;
      });
   }
}
