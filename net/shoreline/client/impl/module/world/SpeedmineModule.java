package net.shoreline.client.impl.module.world;

import java.text.DecimalFormat;
import net.minecraft.class_1292;
import net.minecraft.class_1294;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2846;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import net.minecraft.class_2846.class_2847;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.AttackBlockEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.mixin.accessor.AccessorClientPlayerInteractionManager;
import net.shoreline.client.util.player.RotationUtil;

public class SpeedmineModule extends RotationModule {
   Config<SpeedmineModule.SpeedmineMode> modeConfig;
   Config<Float> mineSpeedConfig;
   Config<Boolean> instantConfig;
   Config<Float> rangeConfig;
   Config<SpeedmineModule.Swap> swapConfig;
   Config<Boolean> rotateConfig;
   Config<Boolean> grimConfig;
   private class_2338 mining;
   private class_2680 state;
   private class_2350 direction;
   private float damage;
   private boolean switchBack;

   public SpeedmineModule() {
      super("Speedmine", "Mines faster", ModuleCategory.WORLD, 900);
      this.modeConfig = new EnumConfig("Mode", "The mining mode for speedmine", SpeedmineModule.SpeedmineMode.PACKET, SpeedmineModule.SpeedmineMode.values());
      this.mineSpeedConfig = new NumberConfig("Speed", "The speed to mine blocks", 0.0F, 0.7F, 0.9F, () -> {
         return this.modeConfig.getValue() == SpeedmineModule.SpeedmineMode.DAMAGE;
      });
      this.instantConfig = new BooleanConfig("Instant", "Instantly removes the mining block", false, () -> {
         return this.modeConfig.getValue() == SpeedmineModule.SpeedmineMode.PACKET;
      });
      this.rangeConfig = new NumberConfig("Range", "Range for mine", 1.0F, 4.5F, 6.0F, () -> {
         return this.modeConfig.getValue() == SpeedmineModule.SpeedmineMode.PACKET;
      });
      this.swapConfig = new EnumConfig("AutoSwap", "Swaps to the best tool once the mining is complete", SpeedmineModule.Swap.SILENT, SpeedmineModule.Swap.values(), () -> {
         return this.modeConfig.getValue() == SpeedmineModule.SpeedmineMode.PACKET;
      });
      this.rotateConfig = new BooleanConfig("Rotate", "Rotates when mining the block", true, () -> {
         return this.modeConfig.getValue() == SpeedmineModule.SpeedmineMode.PACKET;
      });
      this.grimConfig = new BooleanConfig("Grim", "Uses grim block breaking speeds", false);
   }

   public String getModuleData() {
      DecimalFormat decimal = new DecimalFormat("0.0");
      return decimal.format((double)this.damage);
   }

   public void onDisable() {
      if (this.mining != null) {
         Managers.INVENTORY.syncToClient();
      }

      this.mining = null;
      this.state = null;
      this.direction = null;
      this.damage = 0.0F;
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE && this.modeConfig.getValue() == SpeedmineModule.SpeedmineMode.DAMAGE) {
         AccessorClientPlayerInteractionManager interactionManager = (AccessorClientPlayerInteractionManager)mc.field_1761;
         if (interactionManager.hookGetCurrentBreakingProgress() >= (Float)this.mineSpeedConfig.getValue()) {
            interactionManager.hookSetCurrentBreakingProgress(1.0F);
         }

      }
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      if (this.modeConfig.getValue() == SpeedmineModule.SpeedmineMode.PACKET && !mc.field_1724.method_7337()) {
         if (this.mining == null) {
            this.damage = 0.0F;
         } else {
            this.state = mc.field_1687.method_8320(this.mining);
            int prev = mc.field_1724.method_31548().field_7545;
            int slot = Modules.AUTO_TOOL.getBestTool(this.state);
            double dist = mc.field_1724.method_5707(this.mining.method_46558());
            if (!(dist > ((NumberConfig)this.rangeConfig).getValueSq()) && !this.state.method_26215() && !(this.damage > 3.0F)) {
               if (this.damage > 1.0F && !Modules.AUTO_CRYSTAL.isAttacking() && !Modules.AUTO_CRYSTAL.isPlacing() && !mc.field_1724.method_6115()) {
                  if (this.isRotationBlocked()) {
                     return;
                  }

                  if (this.swapConfig.getValue() != SpeedmineModule.Swap.OFF) {
                     Managers.INVENTORY.setSlot(slot);
                     Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12973, this.mining, this.direction));
                     Managers.INVENTORY.syncToClient();
                  }

                  this.damage = 0.0F;
                  this.mining = null;
                  this.state = null;
                  this.direction = null;
               } else {
                  float delta = this.calcBlockBreakingDelta(this.state, mc.field_1687, this.mining);
                  this.damage += delta;
                  if (delta + this.damage > 1.0F && (Boolean)this.rotateConfig.getValue() && !Modules.AUTO_CRYSTAL.isAttacking() && !Modules.AUTO_CRYSTAL.isPlacing()) {
                     float[] rotations = RotationUtil.getRotationsTo(mc.field_1724.method_33571(), this.mining.method_46558());
                     this.setRotation(rotations[0], rotations[1]);
                  }
               }
            } else {
               Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12971, this.mining, class_2350.field_11033));
               this.mining = null;
               this.state = null;
               this.direction = null;
               this.damage = 0.0F;
            }

         }
      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2846) {
         class_2846 packet = (class_2846)var3;
         if (packet.method_12363() == class_2847.field_12973 && this.modeConfig.getValue() == SpeedmineModule.SpeedmineMode.DAMAGE && (Boolean)this.grimConfig.getValue()) {
            Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12971, packet.method_12362().method_10086(500), packet.method_12360()));
         }
      }

   }

   public class_2338 getBlockTarget() {
      return this.mining;
   }

   @EventListener
   public void onAttackBlock(AttackBlockEvent event) {
      if (this.modeConfig.getValue() == SpeedmineModule.SpeedmineMode.PACKET) {
         if (mc.field_1724 != null && mc.field_1687 != null && !mc.field_1724.method_7337() && (this.mining == null || event.getPos() != this.mining)) {
            if (this.mining != null) {
               Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12971, this.mining, class_2350.field_11033));
            }

            this.mining = event.getPos();
            this.direction = event.getDirection();
            this.damage = 0.0F;
            if (this.mining != null && this.direction != null) {
               int slot = Modules.AUTO_TOOL.getBestTool(event.getState());
               if ((Boolean)this.grimConfig.getValue()) {
                  Managers.INVENTORY.setSlot(slot);
               }

               event.cancel();
               Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12968, this.mining, this.direction));
               if ((Boolean)this.grimConfig.getValue()) {
                  Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12971, this.mining, this.direction));
               }

               Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12973, this.mining, this.direction));
               if ((Boolean)this.instantConfig.getValue()) {
                  mc.field_1687.method_8650(this.mining, false);
               }

               if ((Boolean)this.grimConfig.getValue()) {
                  Managers.INVENTORY.syncToClient();
               }
            }

         }
      }
   }

   float calcBlockBreakingDelta(class_2680 state, class_1922 world, class_2338 pos) {
      if (this.swapConfig.getValue() == SpeedmineModule.Swap.OFF) {
         return state.method_26165(mc.field_1724, mc.field_1687, pos);
      } else {
         float f = state.method_26214(world, pos);
         if (f == -1.0F) {
            return 0.0F;
         } else {
            int i = this.canHarvest(state) ? 30 : 100;
            return this.getBlockBreakingSpeed(state) / f / (float)i;
         }
      }
   }

   private float getBlockBreakingSpeed(class_2680 block) {
      int tool = Modules.AUTO_TOOL.getBestTool(block);
      float f = mc.field_1724.method_31548().method_5438(tool).method_7924(block);
      if (f > 1.0F) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(tool);
         int i = class_1890.method_8225(class_1893.field_9131, stack);
         if (i > 0 && !stack.method_7960()) {
            f += (float)(i * i + 1);
         }
      }

      if (class_1292.method_5576(mc.field_1724)) {
         f *= 1.0F + (float)(class_1292.method_5575(mc.field_1724) + 1) * 0.2F;
      }

      if (mc.field_1724.method_6059(class_1294.field_5901)) {
         float var10000;
         switch(mc.field_1724.method_6112(class_1294.field_5901).method_5578()) {
         case 0:
            var10000 = 0.3F;
            break;
         case 1:
            var10000 = 0.09F;
            break;
         case 2:
            var10000 = 0.0027F;
            break;
         default:
            var10000 = 8.1E-4F;
         }

         float g = var10000;
         f *= g;
      }

      if (mc.field_1724.method_5777(class_3486.field_15517) && !class_1890.method_8200(mc.field_1724)) {
         f /= 5.0F;
      }

      if (!mc.field_1724.method_24828()) {
         f /= 5.0F;
      }

      return f;
   }

   private boolean canHarvest(class_2680 state) {
      if (state.method_29291()) {
         int tool = Modules.AUTO_TOOL.getBestTool(state);
         return mc.field_1724.method_31548().method_5438(tool).method_7951(state);
      } else {
         return true;
      }
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (this.mining != null && this.state != null && !mc.field_1724.method_7337() && this.modeConfig.getValue() == SpeedmineModule.SpeedmineMode.PACKET) {
         class_265 outlineShape = this.state.method_26218(mc.field_1687, this.mining);
         if (!outlineShape.method_1110()) {
            class_238 render1 = outlineShape.method_1107();
            class_238 render = new class_238((double)this.mining.method_10263() + render1.field_1323, (double)this.mining.method_10264() + render1.field_1322, (double)this.mining.method_10260() + render1.field_1321, (double)this.mining.method_10263() + render1.field_1320, (double)this.mining.method_10264() + render1.field_1325, (double)this.mining.method_10260() + render1.field_1324);
            class_243 center = render.method_1005();
            float scale = class_3532.method_15363(this.damage, 0.0F, 1.0F);
            if (scale > 1.0F) {
               scale = 1.0F;
            }

            double dx = (render1.field_1320 - render1.field_1323) / 2.0D;
            double dy = (render1.field_1325 - render1.field_1322) / 2.0D;
            double dz = (render1.field_1324 - render1.field_1321) / 2.0D;
            class_238 scaled = (new class_238(center, center)).method_1009(dx * (double)scale, dy * (double)scale, dz * (double)scale);
            RenderManager.renderBox(event.getMatrices(), scaled, this.damage > 0.95F ? 1610678016 : 1627324416);
            RenderManager.renderBoundingBox(event.getMatrices(), scaled, 2.5F, this.damage > 0.95F ? 1610678016 : 1627324416);
         }
      }
   }

   public static enum SpeedmineMode {
      PACKET,
      DAMAGE;

      // $FF: synthetic method
      private static SpeedmineModule.SpeedmineMode[] $values() {
         return new SpeedmineModule.SpeedmineMode[]{PACKET, DAMAGE};
      }
   }

   public static enum Swap {
      NORMAL,
      SILENT,
      OFF;

      // $FF: synthetic method
      private static SpeedmineModule.Swap[] $values() {
         return new SpeedmineModule.Swap[]{NORMAL, SILENT, OFF};
      }
   }
}
