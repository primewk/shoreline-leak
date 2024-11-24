package net.shoreline.client.impl.event;

import net.minecraft.class_437;
import net.shoreline.client.api.event.Event;

public class ScreenOpenEvent extends Event {
   private final class_437 screen;

   public ScreenOpenEvent(class_437 screen) {
      this.screen = screen;
   }

   public class_437 getScreen() {
      return this.screen;
   }
}
