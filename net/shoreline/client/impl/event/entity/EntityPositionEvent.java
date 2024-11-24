package net.shoreline.client.impl.event.entity;

import net.minecraft.class_243;
import net.shoreline.client.api.event.Event;

public class EntityPositionEvent extends Event {
   private final class_243 updatePos;
   private final class_243 prevPos;

   public EntityPositionEvent(class_243 updatePos, class_243 prevPos) {
      this.updatePos = updatePos;
      this.prevPos = prevPos;
   }

   public class_243 getUpdatePos() {
      return this.updatePos;
   }

   public class_243 getPrevPos() {
      return this.prevPos;
   }
}
