package net.shoreline.client.impl.event.entity;

import net.minecraft.class_1297;
import net.minecraft.class_5712;
import net.shoreline.client.api.event.Event;

public class EntityGameEvent extends Event {
   private final class_5712 gameEvent;
   private final class_1297 entity;

   public EntityGameEvent(class_5712 gameEvent, class_1297 entity) {
      this.gameEvent = gameEvent;
      this.entity = entity;
   }

   public class_5712 getGameEvent() {
      return this.gameEvent;
   }

   public class_1297 getEntity() {
      return this.entity;
   }
}
