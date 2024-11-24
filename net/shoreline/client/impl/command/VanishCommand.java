package net.shoreline.client.impl.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_1297;
import net.minecraft.class_2172;
import net.minecraft.class_1297.class_5529;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.mixin.accessor.AccessorEntity;
import net.shoreline.client.util.chat.ChatUtil;

public class VanishCommand extends Command {
   private class_1297 mount;

   public VanishCommand() {
      super("Vanish", "Desyncs the riding entity", literal("vanish"));
   }

   public void buildCommand(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)builder.then(argument("mount", StringArgumentType.string()).suggests(suggest(new String[]{"mount", "remount"})).executes((c) -> {
         String dismount = StringArgumentType.getString(c, "mount");
         if (dismount.equalsIgnoreCase("dismount")) {
            if (mc.field_1724.method_3144() && mc.field_1724.method_5854() != null) {
               if (this.mount != null) {
                  ChatUtil.error("Entity vanished, must remount before mounting!");
                  return 0;
               }

               this.mount = mc.field_1724.method_5854();
               mc.field_1724.method_29239();
               mc.field_1687.method_2945(this.mount.method_5628(), class_5529.field_26999);
            }
         } else if (dismount.equalsIgnoreCase("remount")) {
            if (this.mount == null) {
               ChatUtil.error("No vanished entity!");
               return 0;
            }

            ((AccessorEntity)this.mount).hookUnsetRemoved();
            mc.field_1687.method_53875(this.mount);
            mc.field_1724.method_5873(this.mount, true);
            this.mount = null;
         }

         return 1;
      }))).executes((c) -> {
         ChatUtil.error("Invalid usage! Usage: " + this.getUsage());
         return 1;
      });
   }
}
