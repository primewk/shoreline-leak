package net.shoreline.client.impl.event.gui;

import net.minecraft.class_1799;
import net.minecraft.class_332;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.StageEvent;

@Cancelable
public class RenderTooltipEvent extends StageEvent {
   public final class_332 context;
   private final class_1799 stack;
   private final int x;
   private final int y;

   public RenderTooltipEvent(class_332 context, class_1799 stack, int x, int y) {
      this.context = context;
      this.stack = stack;
      this.x = x;
      this.y = y;
   }

   public class_332 getContext() {
      return this.context;
   }

   public class_1799 getStack() {
      return this.stack;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }
}
