package net.shoreline.client.impl.module.combat;

import java.util.Iterator;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1779;
import net.minecraft.class_1799;
import net.minecraft.class_2886;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.NumberDisplay;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.math.timer.TickTimer;

public class AutoXPModule extends RotationModule {
   Config<Float> delayConfig;
   Config<Boolean> durabilityCheckConfig;
   Config<Boolean> rotateConfig;
   Config<Boolean> swingConfig;
   private final TickTimer delayTimer;

   public AutoXPModule() {
      super("AutoXP", "Automatically throws xp silently.", ModuleCategory.COMBAT, 850);
      this.delayConfig = new NumberConfig("Delay", "Delay to throw xp in ticks.", 1.0F, 1.0F, 10.0F, NumberDisplay.DEFAULT);
      this.durabilityCheckConfig = new BooleanConfig("DurabilityCheck", "Check if your armor and held item durability is full then disables if it is.", true);
      this.rotateConfig = new BooleanConfig("Rotate", "Rotates the player while throwing xp.", false);
      this.swingConfig = new BooleanConfig("Swing", "Swings hand while throwing xp.", false);
      this.delayTimer = new TickTimer();
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      if (mc.field_1724 != null && this.delayTimer.passed((Number)this.delayConfig.getValue())) {
         if ((Boolean)this.durabilityCheckConfig.getValue() && areItemsFullDura(mc.field_1724)) {
            this.disable();
         } else {
            int prev = mc.field_1724.method_31548().field_7545;
            int slot = -1;

            for(int i = 0; i < 9; ++i) {
               class_1799 stack = mc.field_1724.method_31548().method_5438(i);
               if (stack.method_7909() instanceof class_1779) {
                  slot = i;
                  break;
               }
            }

            if (slot == -1) {
               this.disable();
            } else {
               Managers.INVENTORY.setSlot(slot);
               if ((Boolean)this.rotateConfig.getValue()) {
                  this.setRotation(mc.field_1724.method_36454(), 90.0F);
                  if (this.isRotationBlocked()) {
                     return;
                  }
               }

               Managers.NETWORK.sendSequencedPacket((id) -> {
                  return new class_2886(class_1268.field_5808, id);
               });
               if ((Boolean)this.swingConfig.getValue()) {
                  mc.field_1724.method_6104(class_1268.field_5808);
               }

               Managers.INVENTORY.setSlot(prev);
               this.delayTimer.reset();
            }
         }
      }
   }

   public static boolean areItemsFullDura(class_1657 player) {
      if (isItemFullDura(player.method_6047()) && isItemFullDura(player.method_6079())) {
         Iterator var1 = player.method_5661().iterator();

         class_1799 stack;
         do {
            if (!var1.hasNext()) {
               return true;
            }

            stack = (class_1799)var1.next();
         } while(isItemFullDura(stack));

         return false;
      } else {
         return false;
      }
   }

   private static boolean isItemFullDura(class_1799 stack) {
      if (stack.method_7960()) {
         return true;
      } else {
         int maxDura = stack.method_7936();
         int currentDura = stack.method_7919();
         return currentDura == 0 || maxDura == 0;
      }
   }
}
