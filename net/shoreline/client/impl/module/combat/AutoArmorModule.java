package net.shoreline.client.impl.module.combat;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.PriorityQueue;
import java.util.Queue;
import net.minecraft.class_1738;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_490;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.NumberDisplay;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.player.EnchantmentUtil;

public class AutoArmorModule extends ToggleModule {
   Config<AutoArmorModule.Priority> priorityConfig;
   Config<Float> minDurabilityConfig;
   Config<Boolean> elytraPriorityConfig;
   Config<Boolean> blastLeggingsConfig;
   Config<Boolean> noBindingConfig;
   Config<Boolean> inventoryConfig;
   private final Queue<AutoArmorModule.ArmorSlot> helmet;
   private final Queue<AutoArmorModule.ArmorSlot> chestplate;
   private final Queue<AutoArmorModule.ArmorSlot> leggings;
   private final Queue<AutoArmorModule.ArmorSlot> boots;

   public AutoArmorModule() {
      super("AutoArmor", "Automatically replaces armor pieces", ModuleCategory.COMBAT);
      this.priorityConfig = new EnumConfig("Priority", "Armor enchantment priority", AutoArmorModule.Priority.BLAST_PROTECTION, AutoArmorModule.Priority.values());
      this.minDurabilityConfig = new NumberConfig("MinDurability", "Durability percent to replace armor", 0.0F, 0.0F, 20.0F, NumberDisplay.PERCENT);
      this.elytraPriorityConfig = new BooleanConfig("ElytraPriority", "Prioritizes existing elytras in the chestplate armor slot", true);
      this.blastLeggingsConfig = new BooleanConfig("Leggings-BlastPriority", "Prioritizes Blast Protection leggings", true);
      this.noBindingConfig = new BooleanConfig("NoBinding", "Avoids armor with the Curse of Binding enchantment", true);
      this.inventoryConfig = new BooleanConfig("AllowInventory", "Allows armor to be swapped while in the inventory menu", false);
      this.helmet = new PriorityQueue();
      this.chestplate = new PriorityQueue();
      this.leggings = new PriorityQueue();
      this.boots = new PriorityQueue();
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (mc.field_1755 == null || mc.field_1755 instanceof class_490 && (Boolean)this.inventoryConfig.getValue()) {
            this.helmet.clear();
            this.chestplate.clear();
            this.leggings.clear();
            this.boots.clear();

            int j;
            class_1799 stack;
            for(j = 9; j < 45; ++j) {
               stack = mc.field_1724.method_31548().method_5438(j);
               if (!stack.method_7960()) {
                  class_1792 var5 = stack.method_7909();
                  if (var5 instanceof class_1738) {
                     class_1738 armor = (class_1738)var5;
                     if (!(Boolean)this.noBindingConfig.getValue() || !class_1890.method_8224(stack)) {
                        int index = armor.method_7685().method_5927();
                        float dura = (float)(stack.method_7936() - stack.method_7919()) / (float)stack.method_7936();
                        if (!(dura < (Float)this.minDurabilityConfig.getValue())) {
                           AutoArmorModule.ArmorSlot data = new AutoArmorModule.ArmorSlot(index, j, stack);
                           switch(index) {
                           case 0:
                              this.helmet.add(data);
                              break;
                           case 1:
                              this.chestplate.add(data);
                              break;
                           case 2:
                              this.leggings.add(data);
                              break;
                           case 3:
                              this.boots.add(data);
                           }
                        }
                     }
                  }
               }
            }

            for(j = 0; j < 4; ++j) {
               stack = mc.field_1724.method_31548().method_7372(j);
               if (!(Boolean)this.elytraPriorityConfig.getValue() || stack.method_7909() != class_1802.field_8833) {
                  float armorDura = (float)(stack.method_7936() - stack.method_7919()) / (float)stack.method_7936();
                  if (stack.method_7960() && !(armorDura >= (Float)this.minDurabilityConfig.getValue())) {
                     AutoArmorModule.ArmorSlot bootsSlot;
                     switch(j) {
                     case 0:
                        if (!this.helmet.isEmpty()) {
                           bootsSlot = (AutoArmorModule.ArmorSlot)this.helmet.poll();
                           this.swapArmor(bootsSlot.getType(), bootsSlot.getSlot());
                        }
                        break;
                     case 1:
                        if (!this.chestplate.isEmpty()) {
                           bootsSlot = (AutoArmorModule.ArmorSlot)this.chestplate.poll();
                           this.swapArmor(bootsSlot.getType(), bootsSlot.getSlot());
                        }
                        break;
                     case 2:
                        if (!this.leggings.isEmpty()) {
                           bootsSlot = (AutoArmorModule.ArmorSlot)this.leggings.poll();
                           this.swapArmor(bootsSlot.getType(), bootsSlot.getSlot());
                        }
                        break;
                     case 3:
                        if (!this.boots.isEmpty()) {
                           bootsSlot = (AutoArmorModule.ArmorSlot)this.boots.poll();
                           this.swapArmor(bootsSlot.getType(), bootsSlot.getSlot());
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public void swapArmor(int armorSlot, int slot) {
      class_1799 stack = mc.field_1724.method_31548().method_7372(armorSlot);
      armorSlot = 8 - armorSlot;
      Managers.INVENTORY.pickupSlot(slot);
      boolean rt = !stack.method_7960();
      Managers.INVENTORY.pickupSlot(armorSlot);
      if (rt) {
         Managers.INVENTORY.pickupSlot(slot);
      }

   }

   public float getPriority(int i, class_1799 armorStack) {
      return 1.0F;
   }

   public static enum Priority {
      BLAST_PROTECTION(class_1893.field_9107),
      PROTECTION(class_1893.field_9111),
      PROJECTILE_PROTECTION(class_1893.field_9096);

      private final class_1887 enchant;

      private Priority(class_1887 enchant) {
         this.enchant = enchant;
      }

      public class_1887 getEnchantment() {
         return this.enchant;
      }

      // $FF: synthetic method
      private static AutoArmorModule.Priority[] $values() {
         return new AutoArmorModule.Priority[]{BLAST_PROTECTION, PROTECTION, PROJECTILE_PROTECTION};
      }
   }

   public class ArmorSlot implements Comparable<AutoArmorModule.ArmorSlot> {
      private final int armorType;
      private final int slot;
      private final class_1799 armorStack;

      public ArmorSlot(int armorType, int slot, class_1799 armorStack) {
         this.armorType = armorType;
         this.slot = slot;
         this.armorStack = armorStack;
      }

      public int compareTo(AutoArmorModule.ArmorSlot other) {
         if (this.armorType != other.armorType) {
            return 0;
         } else {
            class_1799 otherStack = other.getArmorStack();
            class_1738 armorItem = (class_1738)this.armorStack.method_7909();
            class_1738 otherItem = (class_1738)otherStack.method_7909();
            int durabilityDiff = armorItem.method_7686().method_48403(armorItem.method_48398()) - otherItem.method_7686().method_48403(otherItem.method_48398());
            if (durabilityDiff != 0) {
               return durabilityDiff;
            } else {
               class_1887 enchantment = ((AutoArmorModule.Priority)AutoArmorModule.this.priorityConfig.getValue()).getEnchantment();
               if ((Boolean)AutoArmorModule.this.blastLeggingsConfig.getValue() && this.armorType == 2 && this.hasEnchantment(class_1893.field_9107)) {
                  return -1;
               } else if (this.hasEnchantment(enchantment)) {
                  return other.hasEnchantment(enchantment) ? 0 : -1;
               } else {
                  return other.hasEnchantment(enchantment) ? 1 : 0;
               }
            }
         }
      }

      public boolean hasEnchantment(class_1887 enchantment) {
         Object2IntMap<class_1887> enchants = EnchantmentUtil.getEnchantments(this.armorStack);
         return enchants.containsKey(enchantment);
      }

      public class_1799 getArmorStack() {
         return this.armorStack;
      }

      public int getType() {
         return this.armorType;
      }

      public int getSlot() {
         return this.slot;
      }
   }
}
