package net.shoreline.client.api.module;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.class_2246;
import net.minecraft.class_2248;

public class ObsidianPlacerModule extends BlockPlacerModule {
   private static final List<class_2248> RESISTANT_BLOCKS = new LinkedList<class_2248>() {
      {
         this.add(class_2246.field_10540);
         this.add(class_2246.field_22423);
         this.add(class_2246.field_10443);
      }
   };

   public ObsidianPlacerModule(String name, String desc, ModuleCategory category) {
      super(name, desc, category);
   }

   public ObsidianPlacerModule(String name, String desc, ModuleCategory category, int rotationPriority) {
      super(name, desc, category, rotationPriority);
   }

   protected int getResistantBlockItem() {
      Iterator var1 = RESISTANT_BLOCKS.iterator();

      int slot;
      do {
         if (!var1.hasNext()) {
            return -1;
         }

         class_2248 type = (class_2248)var1.next();
         slot = this.getBlockItemSlot(type);
      } while(slot == -1);

      return slot;
   }
}
