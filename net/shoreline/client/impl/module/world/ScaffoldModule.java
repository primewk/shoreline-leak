package net.shoreline.client.impl.module.world;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_3965;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.entity.player.PlayerMoveEvent;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.player.MovementUtil;
import net.shoreline.client.util.player.PlayerUtil;
import net.shoreline.client.util.player.RayCastUtil;
import net.shoreline.client.util.player.RotationUtil;
import net.shoreline.client.util.render.animation.Animation;

public final class ScaffoldModule extends RotationModule {
   Config<Boolean> grimConfig = new BooleanConfig("Grim", "Places using grim instant rotations", false);
   Config<Boolean> towerConfig = new BooleanConfig("Tower", "Goes up faster when holding down space", true);
   Config<Boolean> keepYConfig = new BooleanConfig("KeepY", "Keeps your Y level", false);
   Config<Boolean> safeWalkConfig = new BooleanConfig("SafeWalk", "If to prevent you from falling off edges", true);
   Config<ScaffoldModule.BlockPicker> pickerConfig;
   Config<Boolean> renderConfig;
   Config<Integer> fadeTimeConfig;
   private final Map<class_2338, Animation> fadeList;
   private ScaffoldModule.BlockData lastBlockData;
   private boolean sneakOverride;

   public ScaffoldModule() {
      super("Scaffold", "Rapidly places blocks under your feet", ModuleCategory.WORLD);
      this.pickerConfig = new EnumConfig("BlockPicker", "How to pick a block from the hotbar", ScaffoldModule.BlockPicker.NORMAL, ScaffoldModule.BlockPicker.values());
      this.renderConfig = new BooleanConfig("Render", "Renders where scaffold is placing blocks", false);
      this.fadeTimeConfig = new NumberConfig("Fade-Time", "Timer for the fade", 0, 250, 1000, () -> {
         return false;
      });
      this.fadeList = new HashMap();
   }

   protected void onDisable() {
      super.onDisable();
      if (mc.field_1724 != null) {
         Managers.INVENTORY.syncToClient();
      }

      if (this.sneakOverride) {
         mc.field_1690.field_1832.method_23481(false);
      }

      this.sneakOverride = false;
      this.lastBlockData = null;
   }

   @EventListener
   public void onUpdate(PlayerTickEvent event) {
      ScaffoldModule.BlockData data = this.getBlockData();
      if (data != null) {
         this.lastBlockData = data;
         int blockSlot = this.getBlockSlot();
         if (blockSlot != -1) {
            this.getRotationAnglesFor(data);
            if (!(Boolean)this.grimConfig.getValue() || data.getHitResult() != null) {
               boolean result = Managers.INTERACT.placeBlock(data.getHitResult(), blockSlot, false, (state, angles) -> {
                  angles = data.getAngles();
                  if (angles != null) {
                     if ((Boolean)this.grimConfig.getValue()) {
                        if (state) {
                           Managers.ROTATION.setRotationSilent(angles[0], angles[1], true);
                        } else {
                           Managers.ROTATION.setRotationSilentSync(true);
                        }
                     } else if (state) {
                        this.setRotation(angles[0], angles[1]);
                     }

                  }
               });
               if (result && (Boolean)this.towerConfig.getValue() && mc.field_1690.field_1903.method_1434()) {
                  class_243 velocity = mc.field_1724.method_18798();
                  double velocityY = velocity.field_1351;
                  if (mc.field_1724.method_24828() || velocityY < 0.1D || velocityY <= 0.16477328182606651D) {
                     mc.field_1724.method_18800(velocity.field_1352, 0.41999998688697815D, velocity.field_1350);
                  }
               }

            }
         }
      }
   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      this.lastBlockData = null;
   }

   @EventListener
   public void onMove(PlayerMoveEvent event) {
      if ((Boolean)this.safeWalkConfig.getValue() && MovementUtil.isInputtingMovement() && mc.field_1724.method_24828()) {
         class_241 movement = MovementUtil.applySafewalk(event.getX(), event.getZ());
         if ((Boolean)this.grimConfig.getValue()) {
            class_243 velocity = mc.field_1724.method_18798();
            double deltaX = velocity.method_10216() - (double)movement.field_1343;
            double deltaZ = velocity.method_10215() - (double)movement.field_1342;
            this.sneakOverride = Math.abs(deltaX) > 9.0E-4D || Math.abs(deltaZ) > 9.0E-4D;
            mc.field_1690.field_1832.method_23481(this.sneakOverride);
            return;
         }

         event.setX((double)movement.field_1343);
         event.setZ((double)movement.field_1342);
         event.cancel();
      }

   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if ((Boolean)this.renderConfig.getValue()) {
         Iterator var2 = this.fadeList.entrySet().iterator();

         while(true) {
            if (!var2.hasNext()) {
               if (this.lastBlockData == null || this.lastBlockData.getHitResult() == null) {
                  return;
               }

               if ((Boolean)this.renderConfig.getValue()) {
                  Animation animation = new Animation(true, (float)(Integer)this.fadeTimeConfig.getValue());
                  this.fadeList.put(this.lastBlockData.getPos().method_10093(this.lastBlockData.getSide()), animation);
               }

               this.fadeList.entrySet().removeIf((e) -> {
                  return ((Animation)e.getValue()).getFactor() == 0.0D;
               });
               break;
            }

            Entry<class_2338, Animation> set = (Entry)var2.next();
            ((Animation)set.getValue()).setState(false);
            int boxAlpha = (int)(80.0D * ((Animation)set.getValue()).getFactor());
            int lineAlpha = (int)(145.0D * ((Animation)set.getValue()).getFactor());
            Color boxColor = Modules.COLORS.getColor(boxAlpha);
            Color lineColor = Modules.COLORS.getColor(lineAlpha);
            RenderManager.renderBox(event.getMatrices(), (class_2338)set.getKey(), boxColor.getRGB());
            RenderManager.renderBoundingBox(event.getMatrices(), (class_2338)set.getKey(), 1.5F, lineColor.getRGB());
         }
      }

   }

   private int getBlockSlot() {
      class_1799 serverStack = Managers.INVENTORY.getServerItem();
      if (!serverStack.method_7960() && serverStack.method_7909() instanceof class_1747) {
         return Managers.INVENTORY.getServerSlot();
      } else {
         int blockSlot = -1;
         int count = 0;

         for(int i = 0; i < 9; ++i) {
            class_1799 itemStack = mc.field_1724.method_31548().method_5438(i);
            if (!itemStack.method_7960() && itemStack.method_7909() instanceof class_1747) {
               if (this.pickerConfig.getValue() == ScaffoldModule.BlockPicker.NORMAL) {
                  return i;
               }

               if (blockSlot == -1 || itemStack.method_7947() > count) {
                  blockSlot = i;
                  count = itemStack.method_7947();
               }
            }
         }

         return blockSlot;
      }
   }

   private void getRotationAnglesFor(ScaffoldModule.BlockData data) {
      class_2338 pos = data.getPos();
      class_2350 direction = data.getSide();
      if (!(Boolean)this.grimConfig.getValue()) {
         class_243 hitVec = pos.method_46558().method_1019((new class_243(direction.method_23955())).method_1021(0.5D));
         data.setAngles(RotationUtil.getRotationsTo(mc.field_1724.method_33571(), hitVec));
      } else {
         float yawOffset = 180.0F;
         if (direction.equals(mc.field_1724.method_5735().method_10153())) {
            yawOffset = 0.0F;
         }

         float rotationYaw = MovementUtil.getYawOffset(mc.field_1724.field_3913, mc.field_1724.method_36454() - yawOffset);

         for(float yaw = rotationYaw - 55.0F; yaw <= rotationYaw + 55.0F; yaw += 0.5F) {
            for(float pitch = 75.0F; pitch <= 90.0F; pitch += 0.5F) {
               float[] angles = new float[]{yaw, pitch};
               class_239 result = RayCastUtil.rayCast(4.0D, angles);
               if (result instanceof class_3965) {
                  class_3965 hitResult = (class_3965)result;
                  if (hitResult.method_17777().equals(data.getPos()) && hitResult.method_17780() == direction) {
                     data.setHitResult(hitResult);
                     data.setAngles(angles);
                     return;
                  }
               }
            }
         }

         data.setHitResult((class_3965)null);
         data.setAngles((float[])null);
      }
   }

   private ScaffoldModule.BlockData getBlockData() {
      double posY = mc.field_1724.method_23318();
      if (!(Boolean)this.keepYConfig.getValue() || mc.field_1724.method_24828()) {
         posY = (double)((int)Math.round(mc.field_1724.method_23318()));
      }

      class_2338 pos = PlayerUtil.getRoundedBlockPos(mc.field_1724.method_23317(), posY, mc.field_1724.method_23321()).method_10074();
      class_2350[] var4 = class_2350.values();
      int var5 = var4.length;

      int var6;
      class_2350 direction;
      class_2338 neighbor;
      for(var6 = 0; var6 < var5; ++var6) {
         direction = var4[var6];
         neighbor = pos.method_10093(direction);
         if (!mc.field_1687.method_8320(neighbor).method_45474()) {
            return new ScaffoldModule.BlockData(neighbor, direction.method_10153());
         }
      }

      var4 = class_2350.values();
      var5 = var4.length;

      for(var6 = 0; var6 < var5; ++var6) {
         direction = var4[var6];
         neighbor = pos.method_10093(direction);
         if (mc.field_1687.method_8320(neighbor).method_45474()) {
            class_2350[] var9 = class_2350.values();
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               class_2350 direction1 = var9[var11];
               class_2338 neighbor1 = neighbor.method_10093(direction1);
               if (!mc.field_1687.method_8320(neighbor1).method_45474()) {
                  return new ScaffoldModule.BlockData(neighbor1, direction1.method_10153());
               }
            }
         }
      }

      return null;
   }

   private static enum BlockPicker {
      NORMAL,
      GREATEST;

      // $FF: synthetic method
      private static ScaffoldModule.BlockPicker[] $values() {
         return new ScaffoldModule.BlockPicker[]{NORMAL, GREATEST};
      }
   }

   private static class BlockData {
      private class_3965 hitResult;
      private float[] angles;

      public BlockData(class_2338 pos, class_2350 direction) {
         this((class_3965)(new class_3965(pos.method_46558(), direction, pos, false)), (float[])null);
      }

      public BlockData(class_3965 hitResult, float[] angles) {
         this.hitResult = hitResult;
         this.angles = angles;
      }

      public class_3965 getHitResult() {
         return this.hitResult;
      }

      public void setHitResult(class_3965 hitResult) {
         this.hitResult = hitResult;
      }

      public class_2338 getPos() {
         return this.hitResult.method_17777();
      }

      public class_2350 getSide() {
         return this.hitResult.method_17780();
      }

      public float[] getAngles() {
         return this.angles;
      }

      public void setAngles(float[] angles) {
         this.angles = angles;
      }
   }
}
