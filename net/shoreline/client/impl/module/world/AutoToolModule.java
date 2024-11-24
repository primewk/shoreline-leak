package net.shoreline.client.impl.module.world;

import net.minecraft.class_1799;
import net.minecraft.class_1831;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2680;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.AttackBlockEvent;

public final class AutoToolModule extends ToggleModule {
   public AutoToolModule() {
      super("AutoTool", "Automatically switches to a tool before mining", ModuleCategory.WORLD);
   }

   @EventListener
   public void onBreakBlock(AttackBlockEvent event) {
      class_2680 state = mc.field_1687.method_8320(event.getPos());
      int blockSlot = this.getBestToolNoFallback(state);
      if (blockSlot != -1) {
         mc.field_1724.method_31548().field_7545 = blockSlot;
      }

   }

   public int getBestTool(class_2680 state) {
      int slot = this.getBestToolNoFallback(state);
      return slot != -1 ? slot : mc.field_1724.method_31548().field_7545;
   }

   public int getBestToolNoFallback(class_2680 state) {
      int slot = -1;
      float bestTool = 0.0F;

      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (!stack.method_7960() && stack.method_7909() instanceof class_1831) {
            float speed = stack.method_7924(state);
            int efficiency = class_1890.method_8225(class_1893.field_9131, stack);
            if (efficiency > 0) {
               speed += (float)(efficiency * efficiency) + 1.0F;
            }

            if (speed > bestTool) {
               bestTool = speed;
               slot = i;
            }
         }
      }

      return slot;
   }
}
