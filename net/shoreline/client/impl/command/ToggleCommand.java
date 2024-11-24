package net.shoreline.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.api.command.ModuleArgumentType;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.util.chat.ChatUtil;

public class ToggleCommand extends Command {
   public ToggleCommand() {
      super("Toggle", "Enables/Disables a module", literal("toggle"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)builder.then(argument("module", ModuleArgumentType.module()).executes((c) -> {
         Module module = ModuleArgumentType.getModule(c, "module");
         if (module instanceof ToggleModule) {
            ToggleModule t = (ToggleModule)module;
            t.toggle();
            Object[] var10001 = new Object[2];
            String var10004 = t.getName();
            var10001[0] = "§7" + var10004 + "§f";
            var10001[1] = t.isEnabled() ? "§senabled§f" : "§cdisabled§f";
            ChatUtil.clientSendMessage("%s is now %s", var10001);
         }

         return 1;
      }))).executes((c) -> {
         ChatUtil.error("Must provide module to toggle!");
         return 1;
      });
   }
}
