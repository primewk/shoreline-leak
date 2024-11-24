package net.shoreline.client.impl.event.world;

import net.minecraft.class_1297;
import net.shoreline.client.api.event.Event;

public class AddEntityEvent extends Event {
   private final class_1297 entity;

   public AddEntityEvent(class_1297 entity) {
      this.entity = entity;
   }

   public class_1297 getEntity() {
      return this.entity;
   }
}
