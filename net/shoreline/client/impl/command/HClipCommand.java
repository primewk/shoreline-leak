package net.shoreline.client.impl.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.chat.ChatUtil;

public class HClipCommand extends Command {
   public HClipCommand() {
      super("HClip", "Horizontally clips the player", literal("hclip"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)builder.then(argument("distance", DoubleArgumentType.doubleArg()).executes((c) -> {
         double dist = DoubleArgumentType.getDouble(c, "distance");
         double rad = Math.toRadians((double)(mc.field_1724.method_36454() + 90.0F));
         double x = Math.cos(rad) * dist;
         double z = Math.sin(rad) * dist;
         Managers.POSITION.setPositionXZ(x, z);
         ChatUtil.clientSendMessage("Horizontally clipped §s" + dist + "§f blocks");
         return 1;
      }))).executes((c) -> {
         ChatUtil.error("Must provide distance!");
         return 1;
      });
   }
}
