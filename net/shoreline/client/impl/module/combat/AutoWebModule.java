package net.shoreline.client.impl.module.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.BlockPlacerModule;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.init.Managers;

public class AutoWebModule extends BlockPlacerModule {
   Config<Float> rangeConfig = new NumberConfig("PlaceRange", "The range to fill nearby holes", 0.1F, 4.0F, 6.0F);
   Config<Float> enemyRangeConfig = new NumberConfig("EnemyRange", "The maximum range of targets", 0.1F, 10.0F, 15.0F);
   Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotates to block before placing", false);
   Config<Boolean> coverHeadConfig = new BooleanConfig("CoverHead", "Places webs on the targets head", false);
   Config<Integer> shiftTicksConfig = new NumberConfig("ShiftTicks", "The number of blocks to place per tick", 1, 2, 5);
   Config<Integer> shiftDelayConfig = new NumberConfig("ShiftDelay", "The delay between each block placement interval", 0, 1, 5);
   private int shiftDelay;

   public AutoWebModule() {
      super("AutoWeb", "Automatically traps nearby entities in webs", ModuleCategory.COMBAT);
   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      this.disable();
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      int blocksPlaced = 0;
      if (this.shiftDelay < (Integer)this.shiftDelayConfig.getValue()) {
         ++this.shiftDelay;
      } else {
         List<class_2338> webs = new ArrayList();
         Iterator var4 = mc.field_1687.method_18456().iterator();

         while(var4.hasNext()) {
            class_1657 entity = (class_1657)var4.next();
            if (entity != mc.field_1724 && !Managers.SOCIAL.isFriend(entity.method_5477())) {
               double d = (double)mc.field_1724.method_5739(entity);
               if (!(d > (double)(Float)this.enemyRangeConfig.getValue())) {
                  class_2338 feetPos = entity.method_24515();
                  double dist = mc.field_1724.method_33571().method_1025(feetPos.method_46558());
                  if (mc.field_1687.method_8320(feetPos).method_26215() && dist <= ((NumberConfig)this.rangeConfig).getValueSq()) {
                     webs.add(feetPos);
                  }

                  if ((Boolean)this.coverHeadConfig.getValue()) {
                     class_2338 headPos = feetPos.method_10084();
                     double dist2 = mc.field_1724.method_33571().method_1025(headPos.method_46558());
                     if (mc.field_1687.method_8320(headPos).method_26215() && dist2 <= ((NumberConfig)this.rangeConfig).getValueSq()) {
                        webs.add(headPos);
                     }
                  }
               }
            }
         }

         while(blocksPlaced < (Integer)this.shiftTicksConfig.getValue() && blocksPlaced < webs.size()) {
            class_2338 targetPos = (class_2338)webs.get(blocksPlaced);
            ++blocksPlaced;
            this.shiftDelay = 0;
            this.placeWeb(targetPos);
         }

      }
   }

   private void placeWeb(class_2338 pos) {
      int slot = this.getBlockItemSlot(class_2246.field_10343);
      if (slot != -1) {
         Managers.INTERACT.placeBlock(pos, slot, (Boolean)this.grimConfig.getValue(), (Boolean)this.strictDirectionConfig.getValue(), false, (state, angles) -> {
            if ((Boolean)this.rotateConfig.getValue()) {
               if (state) {
                  Managers.ROTATION.setRotationSilent(angles[0], angles[1], (Boolean)this.grimConfig.getValue());
               } else {
                  Managers.ROTATION.setRotationSilentSync((Boolean)this.grimConfig.getValue());
               }
            }

         });
      }
   }
}
