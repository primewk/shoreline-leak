package net.shoreline.client.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.awt.Desktop;
import java.io.IOException;
import net.minecraft.class_2172;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.util.chat.ChatUtil;

public class OpenFolderCommand extends Command {
   public OpenFolderCommand() {
      super("OpenFolder", "Opens the client configurations folder", literal("openfolder"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((c) -> {
         try {
            Desktop.getDesktop().open(Shoreline.CONFIG.getClientDirectory().toFile());
         } catch (IOException var2) {
            var2.printStackTrace();
            ChatUtil.error("Failed to open client folder!");
         }

         return 1;
      });
   }
}
