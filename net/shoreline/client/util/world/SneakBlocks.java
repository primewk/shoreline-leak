package net.shoreline.client.util.world;

import java.util.Set;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2680;

public class SneakBlocks {
   private static final Set<class_2248> SNEAK_BLOCKS;

   public static boolean isSneakBlock(class_2680 state) {
      return isSneakBlock(state.method_26204());
   }

   public static boolean isSneakBlock(class_2248 block) {
      return SNEAK_BLOCKS.contains(block);
   }

   static {
      SNEAK_BLOCKS = Set.of(new class_2248[]{class_2246.field_10034, class_2246.field_10443, class_2246.field_10380, class_2246.field_9980, class_2246.field_10181, class_2246.field_16333, class_2246.field_16331, class_2246.field_16336, class_2246.field_10485, class_2246.field_16329, class_2246.field_16335, class_2246.field_10535, class_2246.field_10105, class_2246.field_10414, class_2246.field_10223, class_2246.field_10179});
   }
}
