package net.shoreline.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.api.command.ModuleArgumentType;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.util.chat.ChatUtil;

public class DrawnCommand extends Command {
   public DrawnCommand() {
      super("Drawn", "Toggles the drawn state of the module", literal("drawn"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)builder.then(argument("module", ModuleArgumentType.module()).executes((c) -> {
         Module module = ModuleArgumentType.getModule(c, "module");
         if (module instanceof ToggleModule) {
            ToggleModule toggle = (ToggleModule)module;
            boolean hide = !toggle.isHidden();
            toggle.setHidden(hide);
            String var10000 = module.getName();
            ChatUtil.clientSendMessage("§7" + var10000 + "§f is now " + (hide ? "§chidden§f" : "§svisible§f") + class_124.field_1070 + " in the Hud");
         }

         return 1;
      }))).executes((c) -> {
         ChatUtil.error("Must provide module to draw!");
         return 1;
      });
   }
}
