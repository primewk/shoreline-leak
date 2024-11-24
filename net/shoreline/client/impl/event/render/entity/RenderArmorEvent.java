package net.shoreline.client.impl.event.render.entity;

import net.minecraft.class_1309;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class RenderArmorEvent extends Event {
   private final class_1309 entity;

   public RenderArmorEvent(class_1309 entity) {
      this.entity = entity;
   }

   public class_1309 getEntity() {
      return this.entity;
   }
}
