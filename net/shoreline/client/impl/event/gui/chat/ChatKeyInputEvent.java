package net.shoreline.client.impl.event.gui.chat;

import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class ChatKeyInputEvent extends Event {
   private final int keycode;
   private String chatText;

   public ChatKeyInputEvent(int keycode, String chatText) {
      this.keycode = keycode;
      this.chatText = chatText;
   }

   public int getKeycode() {
      return this.keycode;
   }

   public String getChatText() {
      return this.chatText;
   }

   public void setChatText(String chatText) {
      this.chatText = chatText;
   }
}
