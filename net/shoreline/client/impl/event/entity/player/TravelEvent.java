package net.shoreline.client.impl.event.entity.player;

import net.minecraft.class_243;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.StageEvent;

@Cancelable
public class TravelEvent extends StageEvent {
   private final class_243 movementInput;

   public TravelEvent(class_243 movementInput) {
      this.movementInput = movementInput;
   }

   public class_243 getMovementInput() {
      return this.movementInput;
   }
}
