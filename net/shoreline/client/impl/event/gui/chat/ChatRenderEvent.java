package net.shoreline.client.impl.event.gui.chat;

import net.minecraft.class_332;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class ChatRenderEvent extends Event {
   private final class_332 context;
   private final float x;
   private final float y;

   public ChatRenderEvent(class_332 context, float x, float y) {
      this.context = context;
      this.x = x;
      this.y = y;
   }

   public class_332 getContext() {
      return this.context;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }
}
