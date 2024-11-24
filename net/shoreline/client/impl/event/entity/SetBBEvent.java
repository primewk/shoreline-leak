package net.shoreline.client.impl.event.entity;

import net.minecraft.class_238;
import net.shoreline.client.api.event.Event;

public class SetBBEvent extends Event {
   private final class_238 boundingBox;

   public SetBBEvent(class_238 boundingBox) {
      this.boundingBox = boundingBox;
   }

   public class_238 getBoundingBox() {
      return this.boundingBox;
   }
}
