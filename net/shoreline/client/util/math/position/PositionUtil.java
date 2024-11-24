package net.shoreline.client.util.math.position;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_238;

public class PositionUtil {
   public static List<class_2338> getAllInBox(class_238 box, class_2338 pos) {
      List<class_2338> intersections = new ArrayList();

      for(int x = (int)Math.floor(box.field_1323); (double)x < Math.ceil(box.field_1320); ++x) {
         for(int z = (int)Math.floor(box.field_1321); (double)z < Math.ceil(box.field_1324); ++z) {
            intersections.add(new class_2338(x, pos.method_10264(), z));
         }
      }

      return intersections;
   }

   public static List<class_2338> getAllInBox(class_238 box) {
      List<class_2338> intersections = new ArrayList();

      for(int x = (int)Math.floor(box.field_1323); (double)x < Math.ceil(box.field_1320); ++x) {
         for(int y = (int)Math.floor(box.field_1322); (double)y < Math.ceil(box.field_1325); ++y) {
            for(int z = (int)Math.floor(box.field_1321); (double)z < Math.ceil(box.field_1324); ++z) {
               intersections.add(new class_2338(x, y, z));
            }
         }
      }

      return intersections;
   }
}
