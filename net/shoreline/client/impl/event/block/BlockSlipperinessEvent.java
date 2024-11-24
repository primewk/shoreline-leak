package net.shoreline.client.impl.event.block;

import net.minecraft.class_2248;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class BlockSlipperinessEvent extends Event {
   private final class_2248 block;
   private float slipperiness;

   public BlockSlipperinessEvent(class_2248 block, float slipperiness) {
      this.block = block;
      this.slipperiness = slipperiness;
   }

   public class_2248 getBlock() {
      return this.block;
   }

   public float getSlipperiness() {
      return this.slipperiness;
   }

   public void setSlipperiness(float slipperiness) {
      this.slipperiness = slipperiness;
   }
}
