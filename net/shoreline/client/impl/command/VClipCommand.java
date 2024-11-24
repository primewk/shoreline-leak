package net.shoreline.client.impl.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.chat.ChatUtil;

public class VClipCommand extends Command {
   public VClipCommand() {
      super("VClip", "Vertically clips the player", literal("vclip"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)builder.then(argument("distance", DoubleArgumentType.doubleArg()).executes((c) -> {
         double dist = DoubleArgumentType.getDouble(c, "distance");
         double y = Managers.POSITION.getY();
         if (Math.abs(y) != 256.0D) {
            Managers.POSITION.setPositionY(y + dist);
            ChatUtil.clientSendMessage("Vertically clipped §s" + dist + "§f blocks");
         }

         return 1;
      }))).executes((c) -> {
         ChatUtil.error("Must provide distance!");
         return 1;
      });
   }
}
