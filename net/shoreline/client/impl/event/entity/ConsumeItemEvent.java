package net.shoreline.client.impl.event.entity;

import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.shoreline.client.api.event.Event;

public class ConsumeItemEvent extends Event {
   private final class_1799 activeItemStack;

   public ConsumeItemEvent(class_1799 activeItemStack) {
      this.activeItemStack = activeItemStack;
   }

   public class_1799 getStack() {
      return this.activeItemStack;
   }

   public class_1792 getItem() {
      return this.activeItemStack.method_7909();
   }
}
