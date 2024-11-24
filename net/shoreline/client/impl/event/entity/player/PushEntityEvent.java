package net.shoreline.client.impl.event.entity.player;

import net.minecraft.class_1297;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class PushEntityEvent extends Event {
   private final class_1297 pushed;
   private final class_1297 pusher;

   public PushEntityEvent(class_1297 pushed, class_1297 pusher) {
      this.pushed = pushed;
      this.pusher = pusher;
   }

   public class_1297 getPushed() {
      return this.pushed;
   }

   public class_1297 getPusher() {
      return this.pusher;
   }
}
