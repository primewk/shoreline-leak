package net.shoreline.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_1799;
import net.minecraft.class_2172;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.util.chat.ChatUtil;

public class NbtCommand extends Command {
   public NbtCommand() {
      super("Nbt", "Displays all nbt tags on the held item", literal("nbt"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         class_1799 mainhand = mc.field_1724.method_6047();
         if (mainhand.method_7985() && mainhand.method_7969() != null) {
            ChatUtil.clientSendMessage(mainhand.method_7969().toString());
            return 1;
         } else {
            ChatUtil.error("No Nbt tags on this item!");
            return 0;
         }
      });
   }
}
