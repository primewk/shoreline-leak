package net.shoreline.client.util.world;

import net.shoreline.client.util.Globals;

public class RenderUtil implements Globals {
   public static void reloadRenders(boolean softReload) {
      if (softReload) {
         int x = (int)mc.field_1724.method_23317();
         int y = (int)mc.field_1724.method_23318();
         int z = (int)mc.field_1724.method_23321();
         int d = (Integer)mc.field_1690.method_42503().method_41753() * 16;
         mc.field_1769.method_18146(x - d, y - d, z - d, x + d, y + d, z + d);
      } else {
         mc.field_1769.method_3279();
      }

   }
}
