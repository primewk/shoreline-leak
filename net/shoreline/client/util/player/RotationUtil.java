package net.shoreline.client.util.player;

import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.shoreline.client.util.Globals;

public class RotationUtil implements Globals {
   public static float[] getRotationsTo(class_243 src, class_243 dest) {
      float yaw = (float)(Math.toDegrees(Math.atan2(dest.method_1020(src).field_1350, dest.method_1020(src).field_1352)) - 90.0D);
      float pitch = (float)Math.toDegrees(-Math.atan2(dest.method_1020(src).field_1351, Math.hypot(dest.method_1020(src).field_1352, dest.method_1020(src).field_1350)));
      return new float[]{class_3532.method_15393(yaw), class_3532.method_15393(pitch)};
   }

   public static class_243 getRotationVector(float pitch, float yaw) {
      float f = pitch * 0.017453292F;
      float g = -yaw * 0.017453292F;
      float h = class_3532.method_15362(g);
      float i = class_3532.method_15374(g);
      float j = class_3532.method_15362(f);
      float k = class_3532.method_15374(f);
      return new class_243((double)(i * j), (double)(-k), (double)(h * j));
   }
}
