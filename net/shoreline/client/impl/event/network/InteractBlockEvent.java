package net.shoreline.client.impl.event.network;

import net.minecraft.class_1268;
import net.minecraft.class_3965;
import net.minecraft.class_746;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class InteractBlockEvent extends Event {
   private final class_746 player;
   private final class_1268 hand;
   private final class_3965 hitResult;

   public InteractBlockEvent(class_746 player, class_1268 hand, class_3965 hitResult) {
      this.player = player;
      this.hand = hand;
      this.hitResult = hitResult;
   }

   public class_746 getPlayer() {
      return this.player;
   }

   public class_1268 getHand() {
      return this.hand;
   }

   public class_3965 getHitResult() {
      return this.hitResult;
   }
}
