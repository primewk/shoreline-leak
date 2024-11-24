package net.shoreline.client.util.player;

import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.shoreline.client.util.Globals;

public class InventoryUtil implements Globals {
   public static boolean isHolding32k() {
      return isHolding32k(1000);
   }

   public static boolean isHolding32k(int lvl) {
      class_1799 mainhand = mc.field_1724.method_6047();
      return class_1890.method_8225(class_1893.field_9118, mainhand) >= lvl;
   }

   public static boolean hasItemInInventory(class_1792 item, boolean hotbar) {
      int startSlot = hotbar ? 0 : 9;

      for(int i = startSlot; i < 36; ++i) {
         class_1799 itemStack = mc.field_1724.method_31548().method_5438(i);
         if (!itemStack.method_7960() && itemStack.method_7909() == item) {
            return true;
         }
      }

      return false;
   }
}
