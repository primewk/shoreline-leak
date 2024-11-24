package net.shoreline.client.util.world;

import net.minecraft.class_1296;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1421;
import net.minecraft.class_1477;
import net.minecraft.class_1569;
import net.minecraft.class_1690;
import net.minecraft.class_1694;
import net.minecraft.class_1695;
import net.minecraft.class_1696;
import net.minecraft.class_5354;

public class EntityUtil {
   public static float getHealth(class_1297 entity) {
      if (entity instanceof class_1309) {
         class_1309 e = (class_1309)entity;
         return e.method_6032() + e.method_6067();
      } else {
         return 0.0F;
      }
   }

   public static boolean isMonster(class_1297 e) {
      return e instanceof class_1569;
   }

   public static boolean isNeutral(class_1297 e) {
      return e instanceof class_5354 && !((class_5354)e).method_29511();
   }

   public static boolean isPassive(class_1297 e) {
      return e instanceof class_1296 || e instanceof class_1421 || e instanceof class_1477;
   }

   public static boolean isVehicle(class_1297 e) {
      return e instanceof class_1690 || e instanceof class_1695 || e instanceof class_1696 || e instanceof class_1694;
   }
}
