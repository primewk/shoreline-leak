package net.shoreline.client.util.chat;

import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_7469;
import net.minecraft.class_7591;
import net.shoreline.client.util.Globals;

public class ChatUtil implements Globals {
   private static final String PREFIX = "§s[Shoreline] §f";

   public static void clientSendMessage(String message) {
      mc.field_1705.method_1743().method_44811(class_2561.method_30163("§s[Shoreline] §f" + message), (class_7469)null, (class_7591)null);
   }

   public static void clientSendMessage(String message, Object... params) {
      clientSendMessage(String.format(message, params));
   }

   public static void clientSendMessageRaw(String message) {
      mc.field_1705.method_1743().method_44811(class_2561.method_30163(message), (class_7469)null, (class_7591)null);
   }

   public static void clientSendMessageRaw(String message, Object... params) {
      clientSendMessageRaw(String.format(message, params));
   }

   public static void serverSendMessage(String message) {
      if (mc.field_1724 != null) {
         mc.field_1724.field_3944.method_45729(message);
      }

   }

   public static void serverSendMessage(class_1657 player, String message) {
      if (mc.field_1724 != null) {
         String reply = "/msg " + player.method_5477().getString() + " ";
         mc.field_1724.field_3944.method_45729(reply + message);
      }

   }

   public static void serverSendMessage(class_1657 player, String message, Object... params) {
      serverSendMessage(player, String.format(message, params));
   }

   public static void error(String message) {
      clientSendMessage(class_124.field_1061 + message);
   }

   public static void error(String message, Object... params) {
      clientSendMessage(class_124.field_1061 + message, params);
   }
}
