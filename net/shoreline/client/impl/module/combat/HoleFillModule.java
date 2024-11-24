package net.shoreline.client.impl.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2824;
import net.minecraft.class_2879;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ObsidianPlacerModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.impl.manager.combat.hole.Hole;
import net.shoreline.client.impl.manager.combat.hole.HoleType;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.render.animation.Animation;

public class HoleFillModule extends ObsidianPlacerModule {
   Config<Boolean> obsidianConfig = new BooleanConfig("Obsidian", "Fills obsidian holes", true);
   Config<Boolean> doublesConfig = new BooleanConfig("Doubles", "Fills double holes", false);
   Config<Float> rangeConfig = new NumberConfig("PlaceRange", "The range to fill nearby holes", 0.1F, 4.0F, 6.0F);
   Config<Boolean> websConfig = new BooleanConfig("Webs", "Fills holes with webs", false);
   Config<Boolean> autoConfig = new BooleanConfig("Auto", "Fills holes when enemies are within a certain range", false);
   Config<Float> targetRangeConfig = new NumberConfig("TargetRange", "The range from the target to the hole", 0.5F, 3.0F, 5.0F, () -> {
      return (Boolean)this.autoConfig.getValue();
   });
   Config<Float> enemyRangeConfig = new NumberConfig("EnemyRange", "The maximum range of targets", 0.1F, 10.0F, 15.0F, () -> {
      return (Boolean)this.autoConfig.getValue();
   });
   Config<Boolean> attackConfig = new BooleanConfig("Attack", "Attacks crystals in the way of hole fill", true);
   Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotates to block before placing", false);
   Config<Integer> shiftTicksConfig = new NumberConfig("ShiftTicks", "The number of blocks to place per tick", 1, 2, 5);
   Config<Integer> shiftDelayConfig = new NumberConfig("ShiftDelay", "The delay between each block placement interval", 0, 1, 5);
   Config<Boolean> autoDisableConfig = new BooleanConfig("AutoDisable", "Disables after filling all holes", false);
   Config<Boolean> renderConfig = new BooleanConfig("Render", "Renders where scaffold is placing blocks", false);
   Config<Integer> fadeTimeConfig = new NumberConfig("Fade-Time", "Time to fade", 0, 250, 1000, () -> {
      return false;
   });
   private int shiftDelay;
   private final Map<class_2338, Animation> fadeList = new HashMap();
   private List<class_2338> fills = new ArrayList();

   public HoleFillModule() {
      super("HoleFill", "Fills in nearby holes with blocks", ModuleCategory.COMBAT);
   }

   public void onDisable() {
      this.fadeList.clear();
      this.fills.clear();
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
         List<class_2338> holes = new ArrayList();
         Iterator var4 = Managers.HOLE.getHoles().iterator();

         while(true) {
            while(true) {
               while(true) {
                  Hole hole;
                  do {
                     do {
                        do {
                           do {
                              do {
                                 if (!var4.hasNext()) {
                                    this.fills = holes;
                                    if (this.fills.isEmpty()) {
                                       if ((Boolean)this.autoDisableConfig.getValue()) {
                                          this.disable();
                                       }

                                       return;
                                    }

                                    while(blocksPlaced < (Integer)this.shiftTicksConfig.getValue() && blocksPlaced < this.fills.size()) {
                                       class_2338 targetPos = (class_2338)this.fills.get(blocksPlaced);
                                       ++blocksPlaced;
                                       this.shiftDelay = 0;
                                       this.attackPlace(targetPos);
                                    }

                                    return;
                                 }

                                 hole = (Hole)var4.next();
                              } while(hole.isQuad());
                           } while(hole.isDouble() && !(Boolean)this.doublesConfig.getValue());
                        } while(hole.getSafety() == HoleType.OBSIDIAN && !(Boolean)this.obsidianConfig.getValue());
                     } while(hole.squaredDistanceTo(mc.field_1724) > ((NumberConfig)this.rangeConfig).getValueSq());
                  } while(mc.field_1687.method_8335((class_1297)null, new class_238(hole.getPos())).stream().anyMatch((e) -> {
                     return !Modules.SURROUND.isEntityBlockingSurround(e);
                  }));

                  if ((Boolean)this.autoConfig.getValue()) {
                     Iterator var6 = mc.field_1687.method_18456().iterator();

                     while(var6.hasNext()) {
                        class_1657 entity = (class_1657)var6.next();
                        if (entity != mc.field_1724 && !Managers.SOCIAL.isFriend(entity.method_5477())) {
                           double dist = (double)mc.field_1724.method_5739(entity);
                           if (!(dist > (double)(Float)this.enemyRangeConfig.getValue()) && (!(entity.method_23318() >= hole.method_10214()) || !(hole.squaredDistanceTo(entity) > ((NumberConfig)this.targetRangeConfig).getValueSq()))) {
                              holes.add(hole.getPos());
                              break;
                           }
                        }
                     }
                  } else {
                     holes.add(hole.getPos());
                  }
               }
            }
         }
      }
   }

   private void attack(class_1297 entity) {
      Managers.NETWORK.sendPacket(class_2824.method_34206(entity, mc.field_1724.method_5715()));
      Managers.NETWORK.sendPacket(new class_2879(class_1268.field_5808));
   }

   private void attackPlace(class_2338 targetPos) {
      int slot = (Boolean)this.websConfig.getValue() ? this.getBlockItemSlot(class_2246.field_10343) : this.getResistantBlockItem();
      if (slot != -1) {
         this.attackPlace(targetPos, slot);
      }
   }

   private void attackPlace(class_2338 targetPos, int slot) {
      if ((Boolean)this.attackConfig.getValue()) {
         List<class_1297> entities = mc.field_1687.method_8335((class_1297)null, new class_238(targetPos)).stream().filter((e) -> {
            return e instanceof class_1511;
         }).toList();
         Iterator var4 = entities.iterator();

         while(var4.hasNext()) {
            class_1297 entity = (class_1297)var4.next();
            this.attack(entity);
         }
      }

      Managers.INTERACT.placeBlock(targetPos, slot, (Boolean)this.grimConfig.getValue(), (Boolean)this.strictDirectionConfig.getValue(), false, (state, angles) -> {
         if ((Boolean)this.rotateConfig.getValue()) {
            if (state) {
               Managers.ROTATION.setRotationSilent(angles[0], angles[1], (Boolean)this.grimConfig.getValue());
            } else {
               Managers.ROTATION.setRotationSilentSync((Boolean)this.grimConfig.getValue());
            }
         }

      });
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if ((Boolean)this.renderConfig.getValue()) {
         Iterator var2 = this.fadeList.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<class_2338, Animation> set = (Entry)var2.next();
            ((Animation)set.getValue()).setState(false);
            int boxAlpha = (int)(80.0D * ((Animation)set.getValue()).getFactor());
            int lineAlpha = (int)(145.0D * ((Animation)set.getValue()).getFactor());
            Color boxColor = Modules.COLORS.getColor(boxAlpha);
            Color lineColor = Modules.COLORS.getColor(lineAlpha);
            RenderManager.renderBox(event.getMatrices(), (class_2338)set.getKey(), boxColor.getRGB());
            RenderManager.renderBoundingBox(event.getMatrices(), (class_2338)set.getKey(), 1.5F, lineColor.getRGB());
         }

         if (this.fills.isEmpty()) {
            return;
         }

         var2 = this.fills.iterator();

         while(var2.hasNext()) {
            class_2338 pos = (class_2338)var2.next();
            Animation animation = new Animation(true, (float)(Integer)this.fadeTimeConfig.getValue());
            this.fadeList.put(pos, animation);
         }
      }

      this.fadeList.entrySet().removeIf((e) -> {
         return ((Animation)e.getValue()).getFactor() == 0.0D;
      });
   }
}
