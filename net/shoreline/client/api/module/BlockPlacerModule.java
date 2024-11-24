package net.shoreline.client.api.module;

import java.util.function.Predicate;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2248;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;

public class BlockPlacerModule extends RotationModule {
   protected Config<Boolean> strictDirectionConfig = new BooleanConfig("StrictDirection", "Places on visible sides only", false);
   protected Config<Boolean> grimConfig = new BooleanConfig("Grim", "Places using grim instant rotations", false);

   public BlockPlacerModule(String name, String desc, ModuleCategory category) {
      super(name, desc, category);
      this.register(new Config[]{this.strictDirectionConfig, this.grimConfig});
   }

   public BlockPlacerModule(String name, String desc, ModuleCategory category, int rotationPriority) {
      super(name, desc, category, rotationPriority);
      this.register(new Config[]{this.strictDirectionConfig, this.grimConfig});
   }

   protected int getSlot(Predicate<class_1799> filter) {
      for(int i = 0; i < 9; ++i) {
         class_1799 itemStack = mc.field_1724.method_31548().method_5438(i);
         if (!itemStack.method_7960() && filter.test(itemStack)) {
            return i;
         }
      }

      return -1;
   }

   protected int getBlockItemSlot(class_2248 block) {
      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         class_1792 var5 = stack.method_7909();
         if (var5 instanceof class_1747) {
            class_1747 blockItem = (class_1747)var5;
            if (blockItem.method_7711() == block) {
               return i;
            }
         }
      }

      return -1;
   }
}
