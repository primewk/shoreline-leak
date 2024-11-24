package net.shoreline.client.impl.event;

import net.minecraft.class_1297;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class EntityOutlineEvent extends Event {
   private final class_1297 entity;

   public EntityOutlineEvent(class_1297 entity) {
      this.entity = entity;
   }

   public class_1297 getEntity() {
      return this.entity;
   }
}
