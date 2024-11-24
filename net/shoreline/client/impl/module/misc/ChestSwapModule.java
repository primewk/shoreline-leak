package net.shoreline.client.impl.module.misc;

import net.minecraft.class_1304;
import net.minecraft.class_1738;
import net.minecraft.class_1740;
import net.minecraft.class_1770;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.init.Managers;

public class ChestSwapModule extends ToggleModule {
   Config<ChestSwapModule.Priority> priorityConfig;

   public ChestSwapModule() {
      super("ChestSwap", "Automatically swaps chestplate", ModuleCategory.MISCELLANEOUS);
      this.priorityConfig = new EnumConfig("Priority", "The chestplate material to prioritize", ChestSwapModule.Priority.NETHERITE, ChestSwapModule.Priority.values());
   }

   public void onEnable() {
      label17: {
         class_1799 armorStack = mc.field_1724.method_31548().method_7372(2);
         class_1792 var3 = armorStack.method_7909();
         int elytraSlot;
         if (var3 instanceof class_1738) {
            class_1738 armorItem = (class_1738)var3;
            if (armorItem.method_7685() == class_1304.field_6174) {
               elytraSlot = this.getElytraSlot();
               if (elytraSlot != -1) {
                  Managers.INVENTORY.pickupSlot(elytraSlot);
                  Managers.INVENTORY.pickupSlot(6);
                  Managers.INVENTORY.pickupSlot(elytraSlot);
               }
               break label17;
            }
         }

         elytraSlot = this.getChestplateSlot();
         if (elytraSlot != -1) {
            Managers.INVENTORY.pickupSlot(elytraSlot);
            Managers.INVENTORY.pickupSlot(6);
            Managers.INVENTORY.pickupSlot(elytraSlot);
         }
      }

      this.disable();
   }

   private int getChestplateSlot() {
      int slot = -1;

      for(int i = 0; i < 45; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         class_1792 var5 = stack.method_7909();
         if (var5 instanceof class_1738) {
            class_1738 armorItem = (class_1738)var5;
            if (armorItem.method_7685() == class_1304.field_6174) {
               if (armorItem.method_7686() == class_1740.field_21977 && this.priorityConfig.getValue() == ChestSwapModule.Priority.NETHERITE) {
                  slot = i;
                  break;
               }

               if (armorItem.method_7686() == class_1740.field_7889 && this.priorityConfig.getValue() == ChestSwapModule.Priority.DIAMOND) {
                  slot = i;
                  break;
               }

               slot = i;
            }
         }
      }

      return slot;
   }

   private int getElytraSlot() {
      int slot = -1;

      for(int i = 0; i < 45; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (stack.method_7909() instanceof class_1770) {
            slot = i;
            break;
         }
      }

      return slot;
   }

   private static enum Priority {
      NETHERITE,
      DIAMOND;

      // $FF: synthetic method
      private static ChestSwapModule.Priority[] $values() {
         return new ChestSwapModule.Priority[]{NETHERITE, DIAMOND};
      }
   }
}
