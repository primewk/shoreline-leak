package net.shoreline.client.util.math.position;

import net.minecraft.class_2374;
import net.minecraft.class_243;

public record TimedVec3d(class_243 pos, long time) implements class_2374 {
   public TimedVec3d(class_243 pos, long time) {
      this.pos = pos;
      this.time = time;
   }

   public double method_10216() {
      return this.pos.method_10216();
   }

   public double method_10214() {
      return this.pos.method_10214();
   }

   public double method_10215() {
      return this.pos.method_10215();
   }

   public class_243 pos() {
      return this.pos;
   }

   public long time() {
      return this.time;
   }
}
