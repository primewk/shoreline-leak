package net.shoreline.client.impl.module.combat;

import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2824;
import net.minecraft.class_2879;
import net.minecraft.class_2828.class_2829;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ObsidianPlacerModule;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.math.position.PositionUtil;

public class BlockLagModule extends ObsidianPlacerModule {
   Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotates before placing the block", false);
   Config<Boolean> attackConfig = new BooleanConfig("Attack", "Attacks crystals in the way of block", true);
   Config<Boolean> autoDisableConfig = new BooleanConfig("AutoDisable", "Automatically disables after placing block", false);
   private double prevY;

   public BlockLagModule() {
      super("BlockLag", "Lags you into a block", ModuleCategory.COMBAT);
   }

   public void onEnable() {
      if (mc.field_1724 != null) {
         this.prevY = mc.field_1724.method_23318();
      }
   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      this.disable();
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      if (Math.abs(mc.field_1724.method_23318() - this.prevY) > 0.5D) {
         this.disable();
      } else {
         class_2338 pos = mc.field_1724.method_24515();
         if (!this.isInsideBlock()) {
            Managers.NETWORK.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.42D, mc.field_1724.method_23321(), true));
            Managers.NETWORK.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.75D, mc.field_1724.method_23321(), true));
            Managers.NETWORK.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.01D, mc.field_1724.method_23321(), true));
            Managers.NETWORK.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.16D, mc.field_1724.method_23321(), true));
            Managers.POSITION.setPositionClient(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.167D, mc.field_1724.method_23321());
            this.attackPlace(pos);
            Managers.POSITION.setPositionClient(mc.field_1724.method_23317(), mc.field_1724.method_23318() - 1.167D, mc.field_1724.method_23321());
            class_243 dist = this.getLagOffsetVec();
            Managers.NETWORK.sendPacket(new class_2829(dist.field_1352, dist.field_1351, dist.field_1350, false));
         }

         if ((Boolean)this.autoDisableConfig.getValue()) {
            this.disable();
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

   public boolean isInsideBlock() {
      return PositionUtil.getAllInBox(mc.field_1724.method_5829(), mc.field_1724.method_24515()).stream().anyMatch((pos) -> {
         return !mc.field_1687.method_8320(pos).method_45474();
      });
   }

   public class_243 getLagOffsetVec() {
      return new class_243(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 3.5D, mc.field_1724.method_23321());
   }
}
