package net.shoreline.client.util.player;

import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_304;
import net.minecraft.class_3483;
import net.minecraft.class_3532;
import net.shoreline.client.util.Globals;

public final class PlayerUtil implements Globals {
   public static class_2338 getRoundedBlockPos(double x, double y, double z) {
      int flooredX = class_3532.method_15357(x);
      int flooredY = (int)Math.round(y);
      int flooredZ = class_3532.method_15357(z);
      return new class_2338(flooredX, flooredY, flooredZ);
   }

   public static float getLocalPlayerHealth() {
      return mc.field_1724.method_6032() + mc.field_1724.method_6067();
   }

   public static int computeFallDamage(float fallDistance, float damageMultiplier) {
      if (mc.field_1724.method_5864().method_20210(class_3483.field_42971)) {
         return 0;
      } else {
         class_1293 statusEffectInstance = mc.field_1724.method_6112(class_1294.field_5913);
         float f = statusEffectInstance == null ? 0.0F : (float)(statusEffectInstance.method_5578() + 1);
         return class_3532.method_15386((fallDistance - 3.0F - f) * damageMultiplier);
      }
   }

   public static boolean isHolding(class_1792 item) {
      class_1799 itemStack = mc.field_1724.method_6047();
      if (!itemStack.method_7960() && itemStack.method_7909() == item) {
         return true;
      } else {
         itemStack = mc.field_1724.method_6079();
         return !itemStack.method_7960() && itemStack.method_7909() == item;
      }
   }

   public static boolean isHotbarKeysPressed() {
      class_304[] var0 = mc.field_1690.field_1852;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         class_304 binding = var0[var2];
         if (binding.method_1434()) {
            return true;
         }
      }

      return false;
   }
}
