package net.shoreline.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.shoreline.client.ShorelineMod;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.chat.ChatUtil;

public class ModulesCommand extends Command {
   public ModulesCommand() {
      super("Modules", "Displays all client modules", literal("modules"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((c) -> {
         StringBuilder modulesList = new StringBuilder();
         Iterator var2 = Managers.MODULE.getModules().iterator();

         while(var2.hasNext()) {
            String var10000;
            Module module;
            label24: {
               module = (Module)var2.next();
               if (module instanceof ToggleModule) {
                  ToggleModule t = (ToggleModule)module;
                  if (t.isEnabled()) {
                     var10000 = "§s";
                     break label24;
                  }
               }

               var10000 = "§f";
            }

            String formatting = var10000;
            modulesList.append(formatting);
            modulesList.append(module.getName());
            modulesList.append(class_124.field_1070);
            if (!module.getName().equalsIgnoreCase(ShorelineMod.isBaritonePresent() ? "Baritone" : "Speedmine")) {
               modulesList.append(", ");
            }
         }

         ChatUtil.clientSendMessageRaw(" §7Modules:§f " + modulesList);
         return 1;
      });
   }
}
