package net.shoreline.client.impl.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.KeyboardUtil;
import net.shoreline.client.util.chat.ChatUtil;

public class PrefixCommand extends Command {
   public PrefixCommand() {
      super("Prefix", "Allows you to change the chat command prefix", literal("prefix"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)builder.then(argument("prefix", StringArgumentType.string()).executes((c) -> {
         String prefix = StringArgumentType.getString(c, "prefix");
         if (prefix.length() > 1) {
            ChatUtil.error("Prefix can only be one character!");
            return 0;
         } else {
            int keycode = KeyboardUtil.getKeyCode(prefix);
            Managers.COMMAND.setPrefix(prefix, keycode);
            ChatUtil.clientSendMessage("Command prefix changed to §s" + prefix);
            return 1;
         }
      }))).executes((c) -> {
         ChatUtil.error("Please provide a new prefix!");
         return 1;
      });
   }
}
