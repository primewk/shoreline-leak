package net.shoreline.client.util.player;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public class EnchantmentUtil {
   public static Object2IntMap<class_1887> getEnchantments(class_1799 itemStack) {
      Object2IntMap<class_1887> enchants = new Object2IntOpenHashMap();
      class_2499 list = itemStack.method_7921();

      for(int i = 0; i < list.size(); ++i) {
         class_2487 tag = list.method_10602(i);
         class_7923.field_41176.method_17966(class_2960.method_12829(tag.method_10558("id"))).ifPresent((enchantment) -> {
            enchants.put(enchantment, tag.method_10550("lvl"));
         });
      }

      return enchants;
   }
}
