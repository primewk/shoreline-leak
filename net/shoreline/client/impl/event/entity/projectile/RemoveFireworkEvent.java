package net.shoreline.client.impl.event.entity.projectile;

import net.minecraft.class_1671;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class RemoveFireworkEvent extends Event {
   private final class_1671 rocketEntity;

   public RemoveFireworkEvent(class_1671 rocketEntity) {
      this.rocketEntity = rocketEntity;
   }

   public class_1671 getRocketEntity() {
      return this.rocketEntity;
   }
}
