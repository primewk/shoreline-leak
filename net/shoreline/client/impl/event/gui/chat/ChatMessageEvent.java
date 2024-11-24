package net.shoreline.client.impl.event.gui.chat;

import net.minecraft.class_3544;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;
import org.apache.commons.lang3.StringUtils;

@Cancelable
public class ChatMessageEvent extends Event {
   private final String message;

   public ChatMessageEvent(String message) {
      this.message = message;
   }

   public String getMessage() {
      return this.normalize(this.message);
   }

   private String normalize(String chatText) {
      return class_3544.method_43681(StringUtils.normalizeSpace(chatText.trim()));
   }

   @Cancelable
   public static class Server extends ChatMessageEvent {
      public Server(String message) {
         super(message);
      }
   }

   @Cancelable
   public static class Client extends ChatMessageEvent {
      public Client(String message) {
         super(message);
      }
   }
}
