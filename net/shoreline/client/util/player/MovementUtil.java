package net.shoreline.client.util.player;

import net.minecraft.class_241;
import net.minecraft.class_3532;
import net.minecraft.class_744;
import net.shoreline.client.util.Globals;

public class MovementUtil implements Globals {
   public static boolean isInputtingMovement() {
      return mc.field_1724.field_3913.field_3910 || mc.field_1724.field_3913.field_3909 || mc.field_1724.field_3913.field_3908 || mc.field_1724.field_3913.field_3906;
   }

   public static boolean isMovingInput() {
      return mc.field_1724.field_3913.field_3905 != 0.0F || mc.field_1724.field_3913.field_3907 != 0.0F;
   }

   public static boolean isMoving() {
      double d = mc.field_1724.method_23317() - mc.field_1724.field_3926;
      double e = mc.field_1724.method_23318() - mc.field_1724.field_3940;
      double f = mc.field_1724.method_23321() - mc.field_1724.field_3924;
      return class_3532.method_41190(d, e, f) > class_3532.method_33723(2.0E-4D);
   }

   public static class_241 applySafewalk(double motionX, double motionZ) {
      double offset = 0.05D;
      double moveX = motionX;
      double moveZ = motionZ;
      float fallDist = -mc.field_1724.method_49476();
      if (!mc.field_1724.method_24828()) {
         fallDist = -1.5F;
      }

      while(moveX != 0.0D && mc.field_1687.method_8587(mc.field_1724, mc.field_1724.method_5829().method_989(moveX, (double)fallDist, 0.0D))) {
         if (moveX < 0.05D && moveX >= -0.05D) {
            moveX = 0.0D;
         } else if (moveX > 0.0D) {
            moveX -= 0.05D;
         } else {
            moveX += 0.05D;
         }
      }

      while(moveZ != 0.0D && mc.field_1687.method_8587(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, (double)fallDist, moveZ))) {
         if (moveZ < 0.05D && moveZ >= -0.05D) {
            moveZ = 0.0D;
         } else if (moveZ > 0.0D) {
            moveZ -= 0.05D;
         } else {
            moveZ += 0.05D;
         }
      }

      while(moveX != 0.0D && moveZ != 0.0D && mc.field_1687.method_8587(mc.field_1724, mc.field_1724.method_5829().method_989(moveX, (double)fallDist, moveZ))) {
         if (moveX < 0.05D && moveX >= -0.05D) {
            moveX = 0.0D;
         } else if (moveX > 0.0D) {
            moveX -= 0.05D;
         } else {
            moveX += 0.05D;
         }

         if (moveZ < 0.05D && moveZ >= -0.05D) {
            moveZ = 0.0D;
         } else if (moveZ > 0.0D) {
            moveZ -= 0.05D;
         } else {
            moveZ += 0.05D;
         }
      }

      return new class_241((float)moveX, (float)moveZ);
   }

   public static float getYawOffset(class_744 input, float rotationYaw) {
      if (input.field_3905 < 0.0F) {
         rotationYaw += 180.0F;
      }

      float forward = 1.0F;
      if (input.field_3905 < 0.0F) {
         forward = -0.5F;
      } else if (input.field_3905 > 0.0F) {
         forward = 0.5F;
      }

      float strafe = input.field_3907;
      if (strafe > 0.0F) {
         rotationYaw -= 90.0F * forward;
      }

      if (strafe < 0.0F) {
         rotationYaw += 90.0F * forward;
      }

      return rotationYaw;
   }
}
