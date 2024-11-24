package net.shoreline.client.impl.event.text;

import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class TextVisitEvent extends Event {
   private String text;

   public TextVisitEvent(String text) {
      this.text = text;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }
}
