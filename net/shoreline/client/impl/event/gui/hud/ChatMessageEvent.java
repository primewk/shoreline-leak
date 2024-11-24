package net.shoreline.client.impl.event.gui.hud;

import net.minecraft.class_2561;
import net.shoreline.client.api.event.Event;

public class ChatMessageEvent extends Event {
   private final class_2561 text;

   public ChatMessageEvent(class_2561 text) {
      this.text = text;
   }

   public class_2561 getText() {
      return this.text;
   }
}
