package net.shoreline.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Iterator;
import net.minecraft.class_2172;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.api.command.CommandArgumentType;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.chat.ChatUtil;

public class HelpCommand extends Command {
   public HelpCommand() {
      super("Help", "Displays command functionality", literal("help"));
   }

   private static String toHelpMessage(Command command) {
      return String.format("%s %s- %s", command.getName(), command.getUsage(), command.getDescription());
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)builder.then(argument("command", CommandArgumentType.command()).executes((c) -> {
         Command command = CommandArgumentType.getCommand(c, "command");
         ChatUtil.clientSendMessage(toHelpMessage(command));
         return 1;
      }))).executes((c) -> {
         ChatUtil.clientSendMessageRaw("§s[Commands Help]");
         Iterator var1 = Managers.COMMAND.getCommands().iterator();

         while(var1.hasNext()) {
            Command c1 = (Command)var1.next();
            if (!(c1 instanceof ModuleCommand)) {
               ChatUtil.clientSendMessageRaw(toHelpMessage(c1));
            }
         }

         return 1;
      });
   }
}
