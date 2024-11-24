package net.shoreline.client.impl.manager.player;

import net.minecraft.class_243;
import net.shoreline.client.util.Globals;

public class MovementManager implements Globals {
   public void setMotionY(double y) {
      class_243 motion = mc.field_1724.method_18798();
      mc.field_1724.method_18800(motion.method_10216(), y, motion.method_10215());
   }

   public void setMotionXZ(double x, double z) {
      class_243 motion = mc.field_1724.method_18798();
      mc.field_1724.method_18800(x, motion.field_1351, z);
   }
}
