package net.shoreline.client.impl.module.misc;

import net.minecraft.class_1268;
import net.minecraft.class_1702;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_304;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.mixin.accessor.AccessorKeyBinding;

public class AutoEatModule extends ToggleModule {
   Config<Float> hungerConfig = new NumberConfig("Hunger", "The minimum hunger level before eating", 1.0F, 19.0F, 20.0F);
   private int prevSlot;

   public AutoEatModule() {
      super("AutoEat", "Automatically eats when losing hunger", ModuleCategory.MISCELLANEOUS);
   }

   public void onEnable() {
      this.prevSlot = -1;
   }

   public void onDisable() {
      class_304.method_1416(((AccessorKeyBinding)mc.field_1690.field_1904).getBoundKey(), false);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (!mc.field_1724.method_6115()) {
         if (this.prevSlot != -1) {
            Managers.INVENTORY.setClientSlot(this.prevSlot);
            this.prevSlot = -1;
         }

         class_304.method_1416(((AccessorKeyBinding)mc.field_1690.field_1904).getBoundKey(), false);
      } else {
         class_1702 hungerManager = mc.field_1724.method_7344();
         if ((float)hungerManager.method_7586() <= (Float)this.hungerConfig.getValue()) {
            int slot = this.getFoodSlot();
            if (slot == -1) {
               return;
            }

            if (slot == 45) {
               mc.field_1724.method_6019(class_1268.field_5810);
            } else {
               this.prevSlot = mc.field_1724.method_31548().field_7545;
               Managers.INVENTORY.setClientSlot(slot);
            }

            class_304.method_1416(((AccessorKeyBinding)mc.field_1690.field_1904).getBoundKey(), true);
         }

      }
   }

   public int getFoodSlot() {
      int foodLevel = -1;
      int slot = -1;

      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (stack.method_7909().method_19263() && stack.method_7909() != class_1802.field_8323 && stack.method_7909() != class_1802.field_8233) {
            int hunger = stack.method_7909().method_19264().method_19230();
            if (hunger > foodLevel) {
               slot = i;
               foodLevel = hunger;
            }
         }
      }

      class_1799 offhand = mc.field_1724.method_6079();
      if (offhand.method_7909().method_19263()) {
         if (offhand.method_7909() == class_1802.field_8323 || offhand.method_7909() == class_1802.field_8233) {
            return slot;
         }

         int hunger = offhand.method_7909().method_19264().method_19230();
         if (hunger > foodLevel) {
            slot = 45;
         }
      }

      return slot;
   }
}
