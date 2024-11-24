package net.shoreline.client.impl.module.combat;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2596;
import net.minecraft.class_2626;
import net.minecraft.class_2680;
import net.minecraft.class_2767;
import net.minecraft.class_2824;
import net.minecraft.class_2879;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ObsidianPlacerModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.impl.event.world.AddEntityEvent;
import net.shoreline.client.impl.event.world.RemoveEntityEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.math.position.PositionUtil;
import net.shoreline.client.util.player.PlayerUtil;
import net.shoreline.client.util.render.animation.Animation;

public class SurroundModule extends ObsidianPlacerModule {
   Config<Float> placeRangeConfig = new NumberConfig("PlaceRange", "The placement range for surround", 0.0F, 4.0F, 6.0F);
   Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotates to block before placing", false);
   Config<Boolean> attackConfig = new BooleanConfig("Attack", "Attacks crystals in the way of surround", true);
   Config<Boolean> centerConfig = new BooleanConfig("Center", "Centers the player before placing blocks", false);
   Config<Boolean> extendConfig = new BooleanConfig("Extend", "Extends surround if the player is not in the center of a block", true, () -> {
      return !(Boolean)this.centerConfig.getValue();
   });
   Config<Boolean> supportConfig = new BooleanConfig("Support", "Creates a floor for the surround if there is none", false);
   Config<Integer> shiftTicksConfig = new NumberConfig("ShiftTicks", "The number of blocks to place per tick", 1, 2, 5);
   Config<Integer> shiftDelayConfig = new NumberConfig("ShiftDelay", "The delay between each block placement interval", 0, 1, 5);
   Config<Boolean> jumpDisableConfig = new BooleanConfig("AutoDisable", "Disables after moving out of the hole", true);
   Config<Boolean> renderConfig = new BooleanConfig("Render", "Renders where scaffold is placing blocks", false);
   Config<Integer> fadeTimeConfig = new NumberConfig("Fade-Time", "Time to fade", 0, 250, 1000, () -> {
      return false;
   });
   private final Map<class_2338, Animation> fadeList = new HashMap();
   private List<class_2338> surround = new ArrayList();
   private List<class_2338> placements = new ArrayList();
   private int blocksPlaced;
   private int shiftDelay;
   private double prevY;

   public SurroundModule() {
      super("Surround", "Surrounds feet with obsidian", ModuleCategory.COMBAT, 950);
   }

   public void onEnable() {
      if (mc.field_1724 != null) {
         if ((Boolean)this.centerConfig.getValue()) {
            double x = Math.floor(mc.field_1724.method_23317()) + 0.5D;
            double z = Math.floor(mc.field_1724.method_23321()) + 0.5D;
            Managers.MOVEMENT.setMotionXZ((x - mc.field_1724.method_23317()) / 2.0D, (z - mc.field_1724.method_23321()) / 2.0D);
         }

         this.prevY = mc.field_1724.method_23318();
      }
   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      this.disable();
   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent event) {
      if (event.getEntity() == mc.field_1724) {
         this.disable();
      }

   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      if (!Modules.SELF_TRAP.isEnabled()) {
         this.blocksPlaced = 0;
         if ((Boolean)this.jumpDisableConfig.getValue() && Math.abs(mc.field_1724.method_23318() - this.prevY) > 0.5D) {
            this.disable();
         } else {
            class_2338 pos = PlayerUtil.getRoundedBlockPos(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321());
            if (this.shiftDelay < (Integer)this.shiftDelayConfig.getValue()) {
               ++this.shiftDelay;
            } else {
               int slot = this.getResistantBlockItem();
               if (slot != -1) {
                  this.surround = this.getSurroundPositions(pos);
                  this.placements = (List)this.surround.stream().filter((blockPos) -> {
                     return mc.field_1687.method_8320(blockPos).method_45474();
                  }).collect(Collectors.toList());
                  if (!this.placements.isEmpty()) {
                     class_2338 block;
                     if ((Boolean)this.supportConfig.getValue()) {
                        Iterator var4 = (new ArrayList(this.placements)).iterator();

                        while(var4.hasNext()) {
                           block = (class_2338)var4.next();
                           class_2350 direction = Managers.INTERACT.getInteractDirection(block, (Boolean)this.grimConfig.getValue(), (Boolean)this.strictDirectionConfig.getValue());
                           if (direction == null) {
                              this.placements.add(block.method_10074());
                           }
                        }
                     }

                     Collections.reverse(this.placements);
                     int shiftTicks = (Integer)this.shiftTicksConfig.getValue();

                     while(this.blocksPlaced < shiftTicks && !this.placements.isEmpty() && this.blocksPlaced < this.placements.size()) {
                        block = (class_2338)this.placements.get(this.blocksPlaced);
                        ++this.blocksPlaced;
                        this.shiftDelay = 0;
                        this.attackPlace(block, slot);
                     }

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
      int slot = this.getResistantBlockItem();
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

   public List<class_2338> getSurroundPositions(class_2338 pos) {
      List<class_2338> entities = this.getSurroundEntities(pos);
      List<class_2338> blocks = new CopyOnWriteArrayList();
      Iterator var4 = entities.iterator();

      class_2338 epos;
      while(var4.hasNext()) {
         epos = (class_2338)var4.next();
         class_2350[] var6 = class_2350.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            class_2350 dir2 = var6[var8];
            if (dir2.method_10166().method_10179()) {
               class_2338 pos2 = epos.method_10081(dir2.method_10163());
               if (!entities.contains(pos2) && !blocks.contains(pos2)) {
                  double dist = mc.field_1724.method_5707(pos2.method_46558());
                  if (!(dist > ((NumberConfig)this.placeRangeConfig).getValueSq())) {
                     blocks.add(pos2);
                  }
               }
            }
         }
      }

      var4 = entities.iterator();

      while(var4.hasNext()) {
         epos = (class_2338)var4.next();
         if (epos != pos) {
            blocks.add(epos.method_10074());
         }
      }

      return blocks;
   }

   public List<class_2338> getSurroundEntities(class_1297 entity) {
      List<class_2338> entities = new LinkedList();
      entities.add(entity.method_24515());
      if ((Boolean)this.extendConfig.getValue()) {
         class_2350[] var3 = class_2350.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            class_2350 dir = var3[var5];
            if (dir.method_10166().method_10179()) {
               entities.addAll(PositionUtil.getAllInBox(entity.method_5829(), entity.method_24515()));
            }
         }
      }

      return entities;
   }

   public List<class_2338> getSurroundEntities(class_2338 pos) {
      List<class_2338> entities = new LinkedList();
      entities.add(pos);
      if ((Boolean)this.extendConfig.getValue()) {
         class_2350[] var3 = class_2350.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            class_2350 dir = var3[var5];
            if (dir.method_10166().method_10179()) {
               class_2338 pos1 = pos.method_10081(dir.method_10163());
               List<class_1297> box = mc.field_1687.method_8335((class_1297)null, new class_238(pos1)).stream().filter((e) -> {
                  return !this.isEntityBlockingSurround(e);
               }).toList();
               if (!box.isEmpty()) {
                  Iterator var9 = box.iterator();

                  while(var9.hasNext()) {
                     class_1297 entity = (class_1297)var9.next();
                     entities.addAll(PositionUtil.getAllInBox(entity.method_5829(), pos));
                  }
               }
            }
         }
      }

      return entities;
   }

   public List<class_2338> getEntitySurroundNoSupport(class_1297 entity) {
      List<class_2338> entities = this.getSurroundEntities(entity);
      List<class_2338> blocks = new CopyOnWriteArrayList();
      Iterator var4 = entities.iterator();

      while(var4.hasNext()) {
         class_2338 epos = (class_2338)var4.next();
         class_2350[] var6 = class_2350.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            class_2350 dir2 = var6[var8];
            if (dir2.method_10166().method_10179()) {
               class_2338 pos2 = epos.method_10081(dir2.method_10163());
               if (!entities.contains(pos2) && !blocks.contains(pos2)) {
                  double dist = mc.field_1724.method_5707(pos2.method_46558());
                  if (!(dist > ((NumberConfig)this.placeRangeConfig).getValueSq())) {
                     blocks.add(pos2);
                  }
               }
            }
         }
      }

      return blocks;
   }

   public boolean isEntityBlockingSurround(class_1297 entity) {
      return entity instanceof class_1542 || entity instanceof class_1303 || entity instanceof class_1511 && (Boolean)this.attackConfig.getValue();
   }

   @EventListener
   public void onAddEntity(AddEntityEvent event) {
      class_1297 var3 = event.getEntity();
      if (var3 instanceof class_1511) {
         class_1511 crystalEntity = (class_1511)var3;
         if ((Boolean)this.attackConfig.getValue() && !Modules.SELF_TRAP.isEnabled()) {
            Iterator var5 = this.surround.iterator();

            while(var5.hasNext()) {
               class_2338 blockPos = (class_2338)var5.next();
               if (crystalEntity.method_24515() == blockPos) {
                  Managers.NETWORK.sendPacket(class_2824.method_34206(crystalEntity, mc.field_1724.method_5715()));
                  Managers.NETWORK.sendPacket(new class_2879(class_1268.field_5808));
                  break;
               }
            }

            return;
         }
      }

   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1724 != null && !Modules.SELF_TRAP.isEnabled()) {
         class_2596 var4 = event.getPacket();
         if (var4 instanceof class_2626) {
            class_2626 packet = (class_2626)var4;
            class_2680 state = packet.method_11308();
            class_2338 targetPos = packet.method_11309();
            if (this.surround.contains(targetPos) && state.method_45474()) {
               ++this.blocksPlaced;
               RenderSystem.recordRenderCall(() -> {
                  this.attackPlace(targetPos);
               });
            }
         } else {
            var4 = event.getPacket();
            if (var4 instanceof class_2767) {
               class_2767 packet = (class_2767)var4;
               if (packet.method_11888() == class_3419.field_15245 && packet.method_11894().comp_349() == class_3417.field_15152) {
                  class_2338 targetPos = class_2338.method_49637(packet.method_11890(), packet.method_11889(), packet.method_11893());
                  if (this.surround.contains(targetPos)) {
                     ++this.blocksPlaced;
                     RenderSystem.recordRenderCall(() -> {
                        this.attackPlace(targetPos);
                     });
                  }
               }
            }
         }

      }
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (!Modules.SELF_TRAP.isEnabled()) {
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

            if (this.placements.isEmpty()) {
               return;
            }

            var2 = this.placements.iterator();

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
}
