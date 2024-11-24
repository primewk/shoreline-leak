package net.shoreline.client.impl.module.combat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1713;
import net.minecraft.class_1753;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1833;
import net.minecraft.class_1842;
import net.minecraft.class_1844;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import net.minecraft.class_2846.class_2847;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.init.Managers;

public class SelfBowModule extends RotationModule {
   private final Set<class_1293> arrows = new HashSet();

   public SelfBowModule() {
      super("SelfBow", "Shoots player with beneficial tipped arrows", ModuleCategory.COMBAT);
   }

   public void onDisable() {
      mc.field_1690.field_1904.method_23481(false);
      this.arrows.clear();
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      int arrowSlot = -1;
      class_1293 statusEffect = null;

      int i;
      for(i = 9; i < 36; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (!stack.method_7960() && stack.method_7909() instanceof class_1833) {
            class_1842 p = class_1844.method_8063(stack);
            Iterator var7 = p.method_8049().iterator();

            while(var7.hasNext()) {
               class_1293 effect = (class_1293)var7.next();
               class_1291 type = effect.method_5579();
               if (type.method_5573() && !this.arrows.contains(effect)) {
                  arrowSlot = i;
                  statusEffect = effect;
                  break;
               }
            }

            if (arrowSlot != -1) {
               break;
            }
         }
      }

      i = -1;

      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (!stack.method_7960() && stack.method_7909() == class_1802.field_8102) {
            i = i;
            break;
         }
      }

      if (mc.field_1724.method_6047().method_7909() == class_1802.field_8102 && i != -1 && arrowSlot != -1) {
         this.setRotation(mc.field_1724.method_36454(), -90.0F);
         mc.field_1761.method_2906(0, arrowSlot, 9, class_1713.field_7791, mc.field_1724);
         float pullTime = class_1753.method_7722(mc.field_1724.method_6048());
         if (pullTime >= 0.15F) {
            this.arrows.add(statusEffect);
            mc.field_1690.field_1904.method_23481(false);
            Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
            mc.field_1724.method_6075();
         } else {
            mc.field_1690.field_1904.method_23481(true);
         }

      } else {
         this.disable();
      }
   }
}
