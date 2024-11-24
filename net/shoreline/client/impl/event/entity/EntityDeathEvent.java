package net.shoreline.client.impl.event.entity;

import net.minecraft.class_1309;
import net.shoreline.client.api.event.Event;

public class EntityDeathEvent extends Event {
   private final class_1309 entity;

   public EntityDeathEvent(class_1309 entity) {
      this.entity = entity;
   }

   public class_1309 getEntity() {
      return this.entity;
   }
}
