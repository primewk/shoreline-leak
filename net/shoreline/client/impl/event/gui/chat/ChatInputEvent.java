package net.shoreline.client.impl.event.gui.chat;

import net.shoreline.client.api.event.Event;

public class ChatInputEvent extends Event {
   private final String chatText;

   public ChatInputEvent(String chatText) {
      this.chatText = chatText;
   }

   public String getChatText() {
      return this.chatText;
   }
}
