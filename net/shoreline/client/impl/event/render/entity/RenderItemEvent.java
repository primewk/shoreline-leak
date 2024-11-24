package net.shoreline.client.impl.event.render.entity;

import net.minecraft.class_1542;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class RenderItemEvent extends Event {
   private final class_1542 itemEntity;

   public RenderItemEvent(class_1542 itemEntity) {
      this.itemEntity = itemEntity;
   }

   public class_1542 getItem() {
      return this.itemEntity;
   }
}
