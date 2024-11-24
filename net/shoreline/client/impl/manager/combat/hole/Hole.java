package net.shoreline.client.impl.manager.combat.hole;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_2374;
import net.minecraft.class_243;

public class Hole implements class_2374 {
   private final List<class_2338> holeOffsets;
   private final class_2338 origin;
   private final HoleType safety;

   public Hole(class_2338 origin, HoleType safety, class_2338... holeOffsets) {
      this.origin = origin;
      this.safety = safety;
      this.holeOffsets = Lists.newArrayList(holeOffsets);
      this.holeOffsets.add(origin);
   }

   public double squaredDistanceTo(class_1297 entity) {
      return entity.method_33571().method_1025(this.getCenter());
   }

   public boolean isStandard() {
      return this.holeOffsets.size() == 5;
   }

   public boolean isDouble() {
      return this.holeOffsets.size() == 8;
   }

   public boolean isDoubleX() {
      return this.isDouble() && this.holeOffsets.contains(this.origin.method_10069(2, 0, 0));
   }

   public boolean isDoubleZ() {
      return this.isDouble() && this.holeOffsets.contains(this.origin.method_10069(0, 0, 2));
   }

   public boolean isQuad() {
      return this.holeOffsets.size() == 12;
   }

   public HoleType getSafety() {
      return this.safety;
   }

   public class_2338 getPos() {
      return this.origin;
   }

   public List<class_2338> getHoleOffsets() {
      return this.holeOffsets;
   }

   public boolean addHoleOffsets(class_2338... off) {
      return this.holeOffsets.addAll(Arrays.asList(off));
   }

   public class_243 getCenter() {
      class_2338 center;
      if (this.isDoubleX()) {
         center = this.origin.method_10069(1, 0, 0);
      } else if (this.isDoubleZ()) {
         center = this.origin.method_10069(0, 0, -1);
      } else {
         if (!this.isQuad()) {
            return this.origin.method_46558();
         }

         center = this.origin.method_10069(1, 0, -1);
      }

      return class_243.method_24954(center);
   }

   public double method_10216() {
      return (double)this.origin.method_10263();
   }

   public double method_10214() {
      return (double)this.origin.method_10264();
   }

   public double method_10215() {
      return (double)this.origin.method_10260();
   }
}
