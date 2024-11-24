package net.shoreline.client.impl.event.network;

import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.shoreline.client.api.event.Event;
import net.shoreline.client.util.Globals;

public class SetCurrentHandEvent extends Event implements Globals {
   private final class_1268 hand;

   public SetCurrentHandEvent(class_1268 hand) {
      this.hand = hand;
   }

   public class_1268 getHand() {
      return this.hand;
   }

   public class_1799 getStackInHand() {
      return mc.field_1724.method_5998(this.hand);
   }
}
