package net.shoreline.client.impl.module.combat;

import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.init.Managers;

public class ReplenishModule extends ToggleModule {
   Config<Integer> percentConfig = new NumberConfig("Percent", "The minimum percent of total stack before replenishing", 0, 25, 80);

   public ReplenishModule() {
      super("Replenish", "Automatically replaces items in your hotbar", ModuleCategory.COMBAT);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         for(int i = 0; i < 9; ++i) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960() && stack.method_7946()) {
               double stackPercent = (double)((float)stack.method_7947() / (float)stack.method_7914() * 100.0F);
               if (stack.method_7947() == 1 || stackPercent <= (double)Math.max((float)(Integer)this.percentConfig.getValue(), 5.0F)) {
                  this.replenishStack(stack, i);
               }
            }
         }

      }
   }

   private void replenishStack(class_1799 item, int hotbarSlot) {
      int total = item.method_7947();

      for(int i = 9; i < 36; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (stack.method_7964().equals(item.method_7964())) {
            class_1792 var8 = stack.method_7909();
            if (var8 instanceof class_1747) {
               class_1747 blockItem = (class_1747)var8;
               var8 = item.method_7909();
               if (!(var8 instanceof class_1747)) {
                  continue;
               }

               class_1747 blockItem1 = (class_1747)var8;
               if (blockItem.method_7711() != blockItem1.method_7711()) {
                  continue;
               }
            }

            if (stack.method_7909() == item.method_7909() && total < stack.method_7914()) {
               Managers.INVENTORY.pickupSlot(i);
               Managers.INVENTORY.pickupSlot(hotbarSlot + 36);
               if (!mc.field_1724.field_7512.method_34255().method_7960()) {
                  Managers.INVENTORY.pickupSlot(i);
               }

               total += stack.method_7947();
            }
         }
      }

   }
}
