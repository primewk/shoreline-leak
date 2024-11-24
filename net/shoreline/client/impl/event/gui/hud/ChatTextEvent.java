package net.shoreline.client.impl.event.gui.hud;

import net.minecraft.class_5481;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class ChatTextEvent extends Event {
   private class_5481 text;

   public ChatTextEvent(class_5481 text) {
      this.text = text;
   }

   public void setText(class_5481 text) {
      this.text = text;
   }

   public class_5481 getText() {
      return this.text;
   }
}
