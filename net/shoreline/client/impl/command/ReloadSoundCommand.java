package net.shoreline.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.util.chat.ChatUtil;

public class ReloadSoundCommand extends Command {
   public ReloadSoundCommand() {
      super("ReloadSound", "Reloads the Minecraft sound system", literal("reloadsound"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         mc.method_1483().method_38566();
         ChatUtil.clientSendMessage("Reloaded the SoundSystem");
         return 1;
      });
   }
}
