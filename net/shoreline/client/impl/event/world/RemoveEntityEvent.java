package net.shoreline.client.impl.event.world;

import net.minecraft.class_1297;
import net.minecraft.class_1297.class_5529;
import net.shoreline.client.api.event.Event;
import net.shoreline.client.util.Globals;

public class RemoveEntityEvent extends Event implements Globals {
   private final class_1297 entity;
   private final class_5529 removalReason;

   public RemoveEntityEvent(class_1297 entity, class_5529 removalReason) {
      this.entity = entity;
      this.removalReason = removalReason;
   }

   public class_1297 getEntity() {
      return this.entity;
   }

   public class_5529 getRemovalReason() {
      return this.removalReason;
   }
}
