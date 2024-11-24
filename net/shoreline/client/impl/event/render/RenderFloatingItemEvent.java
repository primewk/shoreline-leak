package net.shoreline.client.impl.event.render;

import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class RenderFloatingItemEvent extends Event {
   private final class_1799 floatingItem;

   public RenderFloatingItemEvent(class_1799 floatingItem) {
      this.floatingItem = floatingItem;
   }

   public class_1792 getFloatingItem() {
      return this.floatingItem.method_7909();
   }

   public class_1799 getFloatingItemStack() {
      return this.floatingItem;
   }
}
