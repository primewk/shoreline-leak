package net.shoreline.client.impl.event.gui;

import net.minecraft.class_4587;
import net.shoreline.client.api.event.Event;

public class RenderScreenEvent extends Event {
   public final class_4587 matrixStack;

   public RenderScreenEvent(class_4587 matrixStack) {
      this.matrixStack = matrixStack;
   }
}
