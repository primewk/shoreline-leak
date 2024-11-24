package net.shoreline.client.impl.event.keyboard;

import net.minecraft.class_744;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.StageEvent;

@Cancelable
public class KeyboardTickEvent extends StageEvent {
   private final class_744 input;

   public KeyboardTickEvent(class_744 input) {
      this.input = input;
   }

   public class_744 getInput() {
      return this.input;
   }
}
