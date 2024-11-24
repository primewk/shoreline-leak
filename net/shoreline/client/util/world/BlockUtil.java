package net.shoreline.client.util.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2586;
import net.minecraft.class_2802;
import net.minecraft.class_2818;
import net.minecraft.class_4076;
import net.shoreline.client.util.Globals;

public class BlockUtil implements Globals {
   public static List<class_2586> blockEntities() {
      List<class_2586> list = new ArrayList();
      Iterator var1 = loadedChunks().iterator();

      while(var1.hasNext()) {
         class_2818 chunk = (class_2818)var1.next();
         list.addAll(chunk.method_12214().values());
      }

      return list;
   }

   public static List<class_2818> loadedChunks() {
      List<class_2818> chunks = new ArrayList();
      int viewDist = (Integer)mc.field_1690.method_42503().method_41753();

      for(int x = -viewDist; x <= viewDist; ++x) {
         for(int z = -viewDist; z <= viewDist; ++z) {
            class_2818 chunk = mc.field_1687.method_2935().method_21730((int)mc.field_1724.method_23317() / 16 + x, (int)mc.field_1724.method_23321() / 16 + z);
            if (chunk != null) {
               chunks.add(chunk);
            }
         }
      }

      return chunks;
   }

   public static boolean isBlockAccessible(class_2338 pos) {
      return mc.field_1687.method_22347(pos) && !mc.field_1687.method_22347(pos.method_10069(0, -1, 0)) && mc.field_1687.method_22347(pos.method_10069(0, 1, 0)) && mc.field_1687.method_22347(pos.method_10069(0, 2, 0));
   }

   public static boolean isBlockLoaded(double x, double z) {
      class_2802 chunkManager = mc.field_1687.method_2935();
      return chunkManager != null ? chunkManager.method_12123(class_4076.method_32204(x), class_4076.method_32204(z)) : false;
   }
}
