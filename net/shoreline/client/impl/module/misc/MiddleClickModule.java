package net.shoreline.client.impl.module.misc;

import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_239;
import net.minecraft.class_3966;
import net.minecraft.class_239.class_240;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.MouseClickEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.player.RayCastUtil;

public class MiddleClickModule extends ToggleModule {
   Config<Boolean> friendConfig = new BooleanConfig("Friend", "Friends players when middle click", true);
   Config<Boolean> pearlConfig = new BooleanConfig("Pearl", "Throws a pearl when middle click", true);
   Config<Boolean> fireworkConfig = new BooleanConfig("Firework", "Uses firework to boost elytra when middle click", false);

   public MiddleClickModule() {
      super("MiddleClick", "Adds an additional bind on the mouse middle button", ModuleCategory.MISCELLANEOUS);
   }

   @EventListener
   public void onMouseClick(MouseClickEvent event) {
      if (mc.field_1724 != null && mc.field_1761 != null) {
         if (event.getButton() == 2 && event.getAction() == 1 && mc.field_1755 == null) {
            double d = mc.field_1761.method_2926() ? 6.0D : (double)mc.field_1761.method_2904();
            class_239 result = Modules.FREECAM.isEnabled() ? RayCastUtil.raycastEntity(d, Modules.FREECAM.getCameraPosition(), Modules.FREECAM.getCameraRotations()) : RayCastUtil.raycastEntity(d);
            if ((Boolean)this.friendConfig.getValue() && result != null && result.method_17783() == class_240.field_1331) {
               class_1297 var6 = ((class_3966)result).method_17782();
               if (var6 instanceof class_1657) {
                  class_1657 target = (class_1657)var6;
                  if (Managers.SOCIAL.isFriend(target.method_5477())) {
                     Managers.SOCIAL.remove(target.method_5477());
                  } else {
                     Managers.SOCIAL.addFriend(target.method_5477());
                  }

                  return;
               }
            }

            class_1792 item = null;
            if (mc.field_1724.method_6128() && (Boolean)this.fireworkConfig.getValue()) {
               item = class_1802.field_8639;
            } else if ((Boolean)this.pearlConfig.getValue()) {
               item = class_1802.field_8634;
            }

            if (item == null) {
               return;
            }

            int slot = -1;

            int prev;
            for(prev = 0; prev < 9; ++prev) {
               class_1799 stack = mc.field_1724.method_31548().method_5438(prev);
               if (stack.method_7909() == item) {
                  slot = prev;
                  break;
               }
            }

            if (slot != -1) {
               prev = mc.field_1724.method_31548().field_7545;
               Managers.INVENTORY.setClientSlot(slot);
               mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
               Managers.INVENTORY.setClientSlot(prev);
            }
         }

      }
   }
}
