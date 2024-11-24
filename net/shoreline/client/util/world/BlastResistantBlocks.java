package net.shoreline.client.util.world;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.shoreline.client.util.Globals;

public class BlastResistantBlocks implements Globals {
   private static final Set<class_2248> BLAST_RESISTANT;
   private static final Set<class_2248> UNBREAKABLE;

   public static boolean isBreakable(class_2338 pos) {
      return isBreakable(mc.field_1687.method_8320(pos).method_26204());
   }

   public static boolean isBreakable(class_2248 block) {
      return !UNBREAKABLE.contains(block);
   }

   public static boolean isUnbreakable(class_2338 pos) {
      return isUnbreakable(mc.field_1687.method_8320(pos).method_26204());
   }

   public static boolean isUnbreakable(class_2248 block) {
      return UNBREAKABLE.contains(block);
   }

   public static boolean isBlastResistant(class_2338 pos) {
      return isBlastResistant(mc.field_1687.method_8320(pos).method_26204());
   }

   public static boolean isBlastResistant(class_2248 block) {
      return BLAST_RESISTANT.contains(block);
   }

   static {
      BLAST_RESISTANT = new ReferenceOpenHashSet(Set.of(class_2246.field_10540, class_2246.field_10535, class_2246.field_10485, class_2246.field_10443, class_2246.field_10327));
      UNBREAKABLE = new ReferenceOpenHashSet(Set.of(class_2246.field_9987, class_2246.field_10525, class_2246.field_10395, class_2246.field_10398, class_2246.field_10499));
   }
}
