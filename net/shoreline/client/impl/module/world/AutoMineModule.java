package net.shoreline.client.impl.module.world;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_2846.class_2847;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.config.ConfigUpdateEvent;
import net.shoreline.client.impl.event.network.AttackBlockEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.EvictingQueue;
import net.shoreline.client.util.Globals;
import net.shoreline.client.util.player.RotationUtil;
import net.shoreline.client.util.world.ExplosionUtil;
import org.jetbrains.annotations.NotNull;

public class AutoMineModule extends RotationModule {
   Config<Boolean> multitaskConfig = new BooleanConfig("Multitask", "Allows mining while using items", false);
   Config<Boolean> autoConfig = new BooleanConfig("Auto", "Automatically mines nearby players feet", false);
   Config<Boolean> autoRemineConfig = new BooleanConfig("AutoRemine", "Automatically remines mined blocks", true, () -> {
      return (Boolean)this.autoConfig.getValue();
   });
   Config<Boolean> strictDirectionConfig = new BooleanConfig("StrictDirection", "Only mines on visible faces", false, () -> {
      return (Boolean)this.autoConfig.getValue();
   });
   Config<Float> enemyRangeConfig = new NumberConfig("EnemyRange", "Range to search for targets", 1.0F, 5.0F, 10.0F, () -> {
      return (Boolean)this.autoConfig.getValue();
   });
   Config<Boolean> doubleBreakConfig = new BooleanConfig("DoubleBreak", "Allows you to mine two blocks at once", false);
   Config<Float> rangeConfig = new NumberConfig("Range", "The range to mine blocks", 0.1F, 4.0F, 5.0F);
   Config<Float> speedConfig = new NumberConfig("Speed", "The speed to mine blocks", 0.1F, 1.0F, 1.0F);
   Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotates when mining the block", true);
   Config<Boolean> switchResetConfig = new BooleanConfig("SwitchReset", "Resets mining after switching items", false);
   Config<Boolean> grimConfig = new BooleanConfig("Grim", "Uses grim block breaking speeds", false);
   Config<Boolean> instantConfig = new BooleanConfig("Instant", "Instant remines mined blocks", true);
   private Deque<AutoMineModule.MiningData> miningQueue = new EvictingQueue(2);
   private long lastBreak;
   private boolean manualOverride;

   public AutoMineModule() {
      super("AutoMine", "Automatically mines blocks", ModuleCategory.WORLD, 900);
   }

   public String getModuleData() {
      if (!this.miningQueue.isEmpty()) {
         AutoMineModule.MiningData data = (AutoMineModule.MiningData)this.miningQueue.peek();
         return String.format("%.1f", Math.min(data.getBlockDamage(), 1.0F));
      } else {
         return super.getModuleData();
      }
   }

   public void onEnable() {
      if ((Boolean)this.doubleBreakConfig.getValue()) {
         this.miningQueue = new EvictingQueue(2);
      } else {
         this.miningQueue = new EvictingQueue(1);
      }

   }

   protected void onDisable() {
      this.miningQueue.clear();
      this.manualOverride = false;
      Managers.INVENTORY.syncToClient();
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      AutoMineModule.MiningData miningData = null;
      if (!this.miningQueue.isEmpty()) {
         miningData = (AutoMineModule.MiningData)this.miningQueue.getFirst();
      }

      double distance;
      if ((Boolean)this.autoConfig.getValue() && !this.manualOverride && (miningData == null || mc.field_1687.method_22347(miningData.getPos()))) {
         class_1657 playerTarget = null;
         distance = 3.4028234663852886E38D;
         Iterator var6 = mc.field_1687.method_18456().iterator();

         while(var6.hasNext()) {
            class_1657 entity = (class_1657)var6.next();
            if (entity != mc.field_1724 && !Managers.SOCIAL.isFriend(entity.method_5477())) {
               double dist = (double)mc.field_1724.method_5739(entity);
               if (!(dist > (double)(Float)this.enemyRangeConfig.getValue()) && dist < distance) {
                  distance = dist;
                  playerTarget = entity;
               }
            }
         }

         if (playerTarget != null) {
            PriorityQueue<AutoMineModule.AutoMineCalc> miningPositions = this.getMiningPosition(playerTarget);
            PriorityQueue<AutoMineModule.AutoMineCalc> miningPositionsNoAir = this.getNoAir(miningPositions);
            PriorityQueue<AutoMineModule.AutoMineCalc> cityPositions = (Boolean)this.autoRemineConfig.getValue() ? miningPositions : miningPositionsNoAir;
            if (cityPositions.isEmpty()) {
               return;
            }

            AutoMineModule.AutoMineCalc cityPos;
            if ((Boolean)this.doubleBreakConfig.getValue()) {
               cityPos = (AutoMineModule.AutoMineCalc)cityPositions.poll();
               if (cityPos != null) {
                  miningPositionsNoAir.remove(cityPos);
                  class_2338 cityPos2 = null;
                  if (!miningPositionsNoAir.isEmpty()) {
                     cityPos2 = ((AutoMineModule.AutoMineCalc)miningPositionsNoAir.poll()).pos();
                  }

                  AutoMineModule.AutoMiningData data1;
                  if (cityPos2 != null) {
                     if (!mc.field_1687.method_22347(cityPos.pos()) && !mc.field_1687.method_22347(cityPos2) && !this.isBlockDelayGrim()) {
                        data1 = new AutoMineModule.AutoMiningData(cityPos2, (Boolean)this.strictDirectionConfig.getValue() ? Managers.INTERACT.getPlaceDirectionGrim(cityPos2) : class_2350.field_11036);
                        AutoMineModule.MiningData data2 = new AutoMineModule.AutoMiningData(cityPos.pos(), (Boolean)this.strictDirectionConfig.getValue() ? Managers.INTERACT.getPlaceDirectionGrim(cityPos.pos()) : class_2350.field_11036);
                        this.startMining(data1);
                        this.startMining(data2);
                        this.miningQueue.addFirst(data1);
                        this.miningQueue.addFirst(data2);
                     }
                  } else if (!mc.field_1687.method_22347(cityPos.pos()) && !this.isBlockDelayGrim()) {
                     data1 = new AutoMineModule.AutoMiningData(cityPos.pos(), (Boolean)this.strictDirectionConfig.getValue() ? Managers.INTERACT.getPlaceDirectionGrim(cityPos.pos()) : class_2350.field_11036);
                     this.startMining(data1);
                     this.miningQueue.addFirst(data1);
                  }
               }
            } else {
               cityPos = (AutoMineModule.AutoMineCalc)cityPositions.poll();
               if (cityPos != null && !this.isBlockDelayGrim()) {
                  if (miningData instanceof AutoMineModule.AutoMiningData && miningData.isInstantRemine() && !mc.field_1687.method_22347(miningData.getPos()) && (Boolean)this.autoRemineConfig.getValue()) {
                     this.stopMining(miningData);
                  } else if (!mc.field_1687.method_22347(cityPos.pos()) && !this.isBlockDelayGrim()) {
                     AutoMineModule.MiningData data = new AutoMineModule.AutoMiningData(cityPos.pos(), (Boolean)this.strictDirectionConfig.getValue() ? Managers.INTERACT.getPlaceDirectionGrim(cityPos.pos()) : class_2350.field_11036);
                     this.startMining(data);
                     this.miningQueue.addFirst(data);
                  }
               }
            }
         }
      }

      if (!this.miningQueue.isEmpty()) {
         Iterator var13 = this.miningQueue.iterator();

         while(var13.hasNext()) {
            AutoMineModule.MiningData data = (AutoMineModule.MiningData)var13.next();
            if (this.isDataPacketMine(data) && data.getState().method_26215()) {
               Managers.INVENTORY.syncToClient();
               this.miningQueue.remove(data);
               return;
            }

            float damageDelta = Modules.SPEEDMINE.calcBlockBreakingDelta(data.getState(), mc.field_1687, data.getPos());
            data.damage(damageDelta);
            if (data.getBlockDamage() >= 1.0F && this.isDataPacketMine(data)) {
               if (mc.field_1724.method_6115() && !(Boolean)this.multitaskConfig.getValue()) {
                  return;
               }

               if (data.getSlot() != -1) {
                  Managers.INVENTORY.setSlot(data.getSlot());
               }
            }
         }

         AutoMineModule.MiningData miningData2 = (AutoMineModule.MiningData)this.miningQueue.getFirst();
         if (miningData2 != null) {
            distance = mc.field_1724.method_33571().method_1025(miningData2.getPos().method_46558());
            if (distance > ((NumberConfig)this.rangeConfig).getValueSq()) {
               this.miningQueue.remove(miningData2);
               return;
            }

            if (miningData2.getState().method_26215()) {
               if (this.manualOverride) {
                  this.manualOverride = false;
                  this.miningQueue.remove(miningData2);
                  return;
               }

               if ((Boolean)this.instantConfig.getValue()) {
                  if (miningData2 instanceof AutoMineModule.AutoMiningData && !(Boolean)this.autoRemineConfig.getValue()) {
                     this.miningQueue.remove(miningData2);
                     return;
                  }

                  miningData2.setInstantRemine();
                  miningData2.setDamage(1.0F);
               } else {
                  miningData2.resetDamage();
               }

               return;
            }

            if (miningData2.getBlockDamage() >= (Float)this.speedConfig.getValue() || miningData2.isInstantRemine()) {
               if (mc.field_1724.method_6115() && !(Boolean)this.multitaskConfig.getValue()) {
                  return;
               }

               this.stopMining(miningData2);
            }
         }

      }
   }

   @EventListener
   public void onAttackBlock(AttackBlockEvent event) {
      if (event.getState().method_26204().method_36555() != -1.0F && !event.getState().method_26215() && !mc.field_1724.method_7337()) {
         event.cancel();
         int queueSize = this.miningQueue.size();
         if (queueSize == 0) {
            this.attemptMine(event.getPos(), event.getDirection());
         } else {
            AutoMineModule.MiningData data1;
            if (queueSize == 1) {
               data1 = (AutoMineModule.MiningData)this.miningQueue.getFirst();
               if (data1.getPos().equals(event.getPos())) {
                  return;
               }

               if (data1 instanceof AutoMineModule.AutoMiningData) {
                  this.manualOverride = true;
               }

               this.attemptMine(event.getPos(), event.getDirection());
            } else if (queueSize == 2) {
               data1 = (AutoMineModule.MiningData)this.miningQueue.getFirst();
               AutoMineModule.MiningData data2 = (AutoMineModule.MiningData)this.miningQueue.getLast();
               if (data1.getPos().equals(event.getPos()) || data2.getPos().equals(event.getPos())) {
                  return;
               }

               if (data1 instanceof AutoMineModule.AutoMiningData || data2 instanceof AutoMineModule.AutoMiningData) {
                  this.manualOverride = true;
               }

               this.attemptMine(event.getPos(), event.getDirection());
            }
         }

         mc.field_1724.method_6104(class_1268.field_5808);
      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (event.getPacket() instanceof class_2868 && (Boolean)this.switchResetConfig.getValue()) {
         Iterator var2 = this.miningQueue.iterator();

         while(var2.hasNext()) {
            AutoMineModule.MiningData data = (AutoMineModule.MiningData)var2.next();
            data.resetDamage();
         }
      }

   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      Iterator var2 = this.miningQueue.iterator();

      while(var2.hasNext()) {
         AutoMineModule.MiningData data = (AutoMineModule.MiningData)var2.next();
         this.renderMiningData(event.getMatrices(), data);
      }

   }

   private void renderMiningData(class_4587 matrixStack, AutoMineModule.MiningData data) {
      if (data != null && !mc.field_1724.method_7337() && data.getBlockDamage() > 0.01F) {
         float miningSpeed = this.isDataPacketMine(data) ? 1.0F : (Float)this.speedConfig.getValue();
         class_2338 mining = data.getPos();
         class_265 outlineShape = class_259.method_1077();
         if (!data.isInstantRemine()) {
            outlineShape = data.getState().method_26218(mc.field_1687, mining);
            outlineShape = outlineShape.method_1110() ? class_259.method_1077() : outlineShape;
         }

         class_238 render1 = outlineShape.method_1107();
         class_238 render = new class_238((double)mining.method_10263() + render1.field_1323, (double)mining.method_10264() + render1.field_1322, (double)mining.method_10260() + render1.field_1321, (double)mining.method_10263() + render1.field_1320, (double)mining.method_10264() + render1.field_1325, (double)mining.method_10260() + render1.field_1324);
         class_243 center = render.method_1005();
         float scale = class_3532.method_15363(data.getBlockDamage() / miningSpeed, 0.0F, 1.0F);
         double dx = (render1.field_1320 - render1.field_1323) / 2.0D;
         double dy = (render1.field_1325 - render1.field_1322) / 2.0D;
         double dz = (render1.field_1324 - render1.field_1321) / 2.0D;
         class_238 scaled = (new class_238(center, center)).method_1009(dx * (double)scale, dy * (double)scale, dz * (double)scale);
         RenderManager.renderBox(matrixStack, scaled, data.getBlockDamage() > 0.95F * miningSpeed ? 1610678016 : 1627324416);
         RenderManager.renderBoundingBox(matrixStack, scaled, 2.5F, data.getBlockDamage() > 0.95F * miningSpeed ? 1610678016 : 1627324416);
      }

   }

   @EventListener
   public void onConfigUpdate(ConfigUpdateEvent event) {
      if (event.getStage() == EventStage.POST && event.getConfig() == this.doubleBreakConfig) {
         if ((Boolean)this.doubleBreakConfig.getValue()) {
            this.miningQueue = new EvictingQueue(2);
         } else {
            this.miningQueue = new EvictingQueue(1);
         }
      }

   }

   private PriorityQueue<AutoMineModule.AutoMineCalc> getNoAir(PriorityQueue<AutoMineModule.AutoMineCalc> calcs) {
      PriorityQueue<AutoMineModule.AutoMineCalc> noAir = new PriorityQueue();
      Iterator var3 = calcs.iterator();

      while(var3.hasNext()) {
         AutoMineModule.AutoMineCalc calc = (AutoMineModule.AutoMineCalc)var3.next();
         if (!mc.field_1687.method_22347(calc.pos())) {
            noAir.add(calc);
         }
      }

      return noAir;
   }

   private PriorityQueue<AutoMineModule.AutoMineCalc> getMiningPosition(class_1657 entity) {
      List<class_2338> entityIntersections = Modules.SURROUND.getSurroundEntities((class_1297)entity);
      PriorityQueue<AutoMineModule.AutoMineCalc> miningPositions = new PriorityQueue();
      Iterator var4 = entityIntersections.iterator();

      while(var4.hasNext()) {
         class_2338 blockPos = (class_2338)var4.next();
         double dist = mc.field_1724.method_33571().method_1025(blockPos.method_46558());
         if (!(dist > ((NumberConfig)this.rangeConfig).getValueSq()) && !mc.field_1687.method_8320(blockPos).method_45474()) {
            miningPositions.add(new AutoMineModule.AutoMineCalc(blockPos, Double.MAX_VALUE));
         }
      }

      List<class_2338> surroundBlocks = Modules.SURROUND.getEntitySurroundNoSupport(entity);
      Iterator var12 = surroundBlocks.iterator();

      while(var12.hasNext()) {
         class_2338 blockPos = (class_2338)var12.next();
         double dist = mc.field_1724.method_33571().method_1025(blockPos.method_46558());
         if (!(dist > ((NumberConfig)this.rangeConfig).getValueSq())) {
            double damage = ExplosionUtil.getDamageTo(entity, blockPos.method_46558().method_1023(0.0D, -0.5D, 0.0D), true);
            miningPositions.add(new AutoMineModule.AutoMineCalc(blockPos, damage));
         }
      }

      return miningPositions;
   }

   private void attemptMine(class_2338 pos, class_2350 direction) {
      if (!this.isBlockDelayGrim()) {
         AutoMineModule.MiningData miningData = new AutoMineModule.MiningData(pos, direction);
         this.startMining(miningData);
         this.miningQueue.addFirst(miningData);
      }
   }

   private void startMining(AutoMineModule.MiningData data) {
      if (!data.getState().method_26215() && !data.isStarted()) {
         Managers.NETWORK.sendSequencedPacket((id) -> {
            return new class_2846(class_2847.field_12973, data.getPos(), data.getDirection(), id);
         });
         Managers.NETWORK.sendSequencedPacket((id) -> {
            return new class_2846(class_2847.field_12968, data.getPos(), data.getDirection(), id);
         });
         if ((Boolean)this.doubleBreakConfig.getValue()) {
            Managers.NETWORK.sendSequencedPacket((id) -> {
               return new class_2846(class_2847.field_12973, data.getPos(), data.getDirection(), id);
            });
         }

         data.setStarted();
      }
   }

   private void abortMining(AutoMineModule.MiningData data) {
      if (data.isStarted() && !data.getState().method_26215() && !data.isInstantRemine() && !(data.getBlockDamage() >= 1.0F)) {
         Managers.NETWORK.sendSequencedPacket((id) -> {
            return new class_2846(class_2847.field_12971, data.getPos(), data.getDirection(), id);
         });
         Managers.INVENTORY.syncToClient();
      }
   }

   private void stopMining(AutoMineModule.MiningData data) {
      if (data.isStarted() && !data.getState().method_26215()) {
         boolean canSwap = data.getSlot() != -1;
         if (canSwap) {
            Managers.INVENTORY.setSlot(data.getSlot());
         }

         if ((Boolean)this.rotateConfig.getValue()) {
            float[] rotations = RotationUtil.getRotationsTo(mc.field_1724.method_33571(), data.getPos().method_46558());
            this.setRotationSilent(rotations[0], rotations[1]);
         }

         Managers.NETWORK.sendSequencedPacket((id) -> {
            return new class_2846(class_2847.field_12973, data.getPos(), data.getDirection(), id);
         });
         this.lastBreak = System.currentTimeMillis();
         if (canSwap) {
            Managers.INVENTORY.syncToClient();
         }

         if ((Boolean)this.rotateConfig.getValue()) {
            Managers.ROTATION.setRotationSilentSync(true);
         }

      }
   }

   private boolean isDataPacketMine(AutoMineModule.MiningData data) {
      return this.miningQueue.size() == 2 && data == this.miningQueue.getLast();
   }

   public boolean isBlockDelayGrim() {
      return System.currentTimeMillis() - this.lastBreak <= 280L && (Boolean)this.grimConfig.getValue();
   }

   public static class MiningData {
      private final class_2338 pos;
      private final class_2350 direction;
      private float blockDamage;
      private boolean instantRemine;
      private boolean started;

      public MiningData(class_2338 pos, class_2350 direction) {
         this.pos = pos;
         this.direction = direction;
      }

      public boolean isInstantRemine() {
         return this.instantRemine;
      }

      public void setInstantRemine() {
         this.instantRemine = true;
      }

      public float damage(float dmg) {
         this.blockDamage += dmg;
         return this.blockDamage;
      }

      public void setDamage(float blockDamage) {
         this.blockDamage = blockDamage;
      }

      public void resetDamage() {
         this.instantRemine = false;
         this.blockDamage = 0.0F;
      }

      public class_2338 getPos() {
         return this.pos;
      }

      public class_2350 getDirection() {
         return this.direction;
      }

      public int getSlot() {
         return Modules.AUTO_TOOL.getBestToolNoFallback(this.getState());
      }

      public class_2680 getState() {
         return Globals.mc.field_1687.method_8320(this.pos);
      }

      public boolean isStarted() {
         return this.started;
      }

      public void setStarted() {
         this.started = true;
      }

      public float getBlockDamage() {
         return this.blockDamage;
      }
   }

   private static record AutoMineCalc(class_2338 pos, double entityDamage) implements Comparable<AutoMineModule.AutoMineCalc> {
      private AutoMineCalc(class_2338 pos, double entityDamage) {
         this.pos = pos;
         this.entityDamage = entityDamage;
      }

      public int compareTo(@NotNull AutoMineModule.AutoMineCalc o) {
         return Double.compare(-this.entityDamage(), -o.entityDamage());
      }

      public class_2338 pos() {
         return this.pos;
      }

      public double entityDamage() {
         return this.entityDamage;
      }
   }

   public static class AutoMiningData extends AutoMineModule.MiningData {
      public AutoMiningData(class_2338 pos, class_2350 direction) {
         super(pos, direction);
      }
   }
}
