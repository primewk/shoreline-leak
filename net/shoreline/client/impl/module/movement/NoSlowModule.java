package net.shoreline.client.impl.module.movement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2560;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2813;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2886;
import net.minecraft.class_304;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import net.minecraft.class_3830;
import net.minecraft.class_408;
import net.minecraft.class_418;
import net.minecraft.class_498;
import net.minecraft.class_744;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_2848.class_2849;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.block.BlockSlipperinessEvent;
import net.shoreline.client.impl.event.block.SteppedOnSlimeBlockEvent;
import net.shoreline.client.impl.event.entity.SlowMovementEvent;
import net.shoreline.client.impl.event.entity.VelocityMultiplierEvent;
import net.shoreline.client.impl.event.network.GameJoinEvent;
import net.shoreline.client.impl.event.network.MovementSlowdownEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.impl.event.network.SetCurrentHandEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.mixin.accessor.AccessorKeyBinding;

public class NoSlowModule extends ToggleModule {
   Config<Boolean> strictConfig = new BooleanConfig("Strict", "Strict NCP bypass for ground slowdowns", false);
   Config<Boolean> airStrictConfig = new BooleanConfig("AirStrict", "Strict NCP bypass for air slowdowns", false);
   Config<Boolean> grimConfig = new BooleanConfig("Grim", "Strict Grim bypass for slowdown", false);
   Config<Boolean> strafeFixConfig = new BooleanConfig("StrafeFix", "Old NCP bypass for strafe", false);
   Config<Boolean> inventoryMoveConfig = new BooleanConfig("InventoryMove", "Allows the player to move while in inventories or screens", true);
   Config<Boolean> arrowMoveConfig = new BooleanConfig("ArrowMove", "Allows the player to look while in inventories or screens by using the arrow keys", false);
   Config<Boolean> itemsConfig = new BooleanConfig("Items", "Removes the slowdown effect caused by using items", true);
   Config<Boolean> shieldsConfig = new BooleanConfig("Shields", "Removes the slowdown effect caused by shields", true);
   Config<Boolean> websConfig = new BooleanConfig("Webs", "Removes the slowdown caused when moving through webs", false);
   Config<Boolean> berryBushConfig = new BooleanConfig("BerryBush", "Removes the slowdown caused when moving through webs", false);
   Config<Float> webSpeedConfig = new NumberConfig("WebSpeed", "Speed to fall through webs", 0.0F, 3.5F, 20.0F, () -> {
      return (Boolean)this.websConfig.getValue();
   });
   Config<Boolean> soulsandConfig = new BooleanConfig("SoulSand", "Removes the slowdown effect caused by walking over SoulSand blocks", false);
   Config<Boolean> honeyblockConfig = new BooleanConfig("HoneyBlock", "Removes the slowdown effect caused by walking over Honey blocks", false);
   Config<Boolean> slimeblockConfig = new BooleanConfig("SlimeBlock", "Removes the slowdown effect caused by walking over Slime blocks", false);
   private boolean sneaking;

   public NoSlowModule() {
      super("NoSlow", "Prevents items from slowing down player", ModuleCategory.MOVEMENT);
   }

   public void onDisable() {
      if ((Boolean)this.airStrictConfig.getValue() && this.sneaking) {
         Managers.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12984));
      }

      this.sneaking = false;
      Managers.TICK.setClientTick(1.0F);
   }

   @EventListener
   public void onGameJoin(GameJoinEvent event) {
      this.onEnable();
   }

   @EventListener
   public void onSetCurrentHand(SetCurrentHandEvent event) {
      if ((Boolean)this.airStrictConfig.getValue() && !this.sneaking && this.checkSlowed()) {
         this.sneaking = true;
         Managers.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12979));
      }

   }

   @EventListener
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (event.getStage() == EventStage.PRE && (Boolean)this.grimConfig.getValue() && mc.field_1724.method_6115() && !mc.field_1724.method_5715() && (Boolean)this.itemsConfig.getValue()) {
         class_1799 offHandStack = mc.field_1724.method_6079();
         if (mc.field_1724.method_6058() == class_1268.field_5810) {
            Managers.INVENTORY.setSlotForced(mc.field_1724.method_31548().field_7545 % 8 + 1);
            Managers.INVENTORY.syncToClient();
         } else if (!offHandStack.method_19267() && offHandStack.method_7909() != class_1802.field_8102 && offHandStack.method_7909() != class_1802.field_8399 && offHandStack.method_7909() != class_1802.field_8255) {
            Managers.NETWORK.sendSequencedPacket((id) -> {
               return new class_2886(class_1268.field_5810, id);
            });
         }
      }

   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if ((Boolean)this.airStrictConfig.getValue() && this.sneaking && !mc.field_1724.method_6115()) {
            this.sneaking = false;
            Managers.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12984));
         }

         if ((Boolean)this.strafeFixConfig.getValue() && this.checkSlowed()) {
         }

         if ((Boolean)this.inventoryMoveConfig.getValue() && this.checkScreen()) {
            long handle = mc.method_22683().method_4490();
            class_304[] keys = new class_304[]{mc.field_1690.field_1903, mc.field_1690.field_1894, mc.field_1690.field_1881, mc.field_1690.field_1849, mc.field_1690.field_1913};
            class_304[] var5 = keys;
            int var6 = keys.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               class_304 binding = var5[var7];
               binding.method_23481(class_3675.method_15987(handle, ((AccessorKeyBinding)binding).getBoundKey().method_1444()));
            }

            if ((Boolean)this.arrowMoveConfig.getValue()) {
               float yaw = mc.field_1724.method_36454();
               float pitch = mc.field_1724.method_36455();
               if (class_3675.method_15987(handle, 265)) {
                  pitch -= 3.0F;
               } else if (class_3675.method_15987(handle, 264)) {
                  pitch += 3.0F;
               } else if (class_3675.method_15987(handle, 263)) {
                  yaw -= 3.0F;
               } else if (class_3675.method_15987(handle, 262)) {
                  yaw += 3.0F;
               }

               mc.field_1724.method_36456(yaw);
               mc.field_1724.method_36457(class_3532.method_15363(pitch, -90.0F, 90.0F));
            }
         }

         if ((Boolean)this.grimConfig.getValue() && ((Boolean)this.websConfig.getValue() || (Boolean)this.berryBushConfig.getValue())) {
            Iterator var9 = this.getIntersectingWebs().iterator();

            while(var9.hasNext()) {
               class_2338 pos = (class_2338)var9.next();
               Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12971, pos, class_2350.field_11033));
               Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12973, pos, class_2350.field_11033));
            }
         }
      }

   }

   @EventListener
   public void onSlowMovement(SlowMovementEvent event) {
      class_2248 block = event.getState().method_26204();
      if (block instanceof class_2560 && (Boolean)this.websConfig.getValue() || block instanceof class_3830 && (Boolean)this.berryBushConfig.getValue()) {
         if ((Boolean)this.grimConfig.getValue()) {
            event.cancel();
         } else if (mc.field_1724.method_24828()) {
            Managers.TICK.setClientTick(1.0F);
         } else {
            Managers.TICK.setClientTick((Float)this.webSpeedConfig.getValue() / 2.0F);
         }
      }

   }

   @EventListener
   public void onMovementSlowdown(MovementSlowdownEvent event) {
      if (this.checkSlowed()) {
         class_744 var10000 = event.input;
         var10000.field_3905 *= 5.0F;
         var10000 = event.input;
         var10000.field_3907 *= 5.0F;
      }

   }

   @EventListener
   public void onVelocityMultiplier(VelocityMultiplierEvent event) {
      if (event.getBlock() == class_2246.field_10114 && (Boolean)this.soulsandConfig.getValue() || event.getBlock() == class_2246.field_21211 && (Boolean)this.honeyblockConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onSteppedOnSlimeBlock(SteppedOnSlimeBlockEvent event) {
      if ((Boolean)this.slimeblockConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onBlockSlipperiness(BlockSlipperinessEvent event) {
      if (event.getBlock() == class_2246.field_10030 && (Boolean)this.slimeblockConfig.getValue()) {
         event.cancel();
         event.setSlipperiness(0.6F);
      }

   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (mc.field_1724 != null && mc.field_1687 != null && !mc.method_1542()) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2828) {
            class_2828 packet = (class_2828)var3;
            if (packet.method_36171() && (Boolean)this.strictConfig.getValue() && this.checkSlowed()) {
               Managers.INVENTORY.setSlotForced(mc.field_1724.method_31548().field_7545);
               return;
            }
         }

         if (event.getPacket() instanceof class_2813 && (Boolean)this.strictConfig.getValue()) {
            if (mc.field_1724.method_6115()) {
               mc.field_1724.method_6075();
            }

            if (this.sneaking || Managers.POSITION.isSneaking()) {
               Managers.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12984));
            }

            if (Managers.POSITION.isSprinting()) {
               Managers.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
            }
         }

      }
   }

   public boolean checkSlowed() {
      return !mc.field_1724.method_3144() && !mc.field_1724.method_5715() && (mc.field_1724.method_6115() && (Boolean)this.itemsConfig.getValue() || mc.field_1724.method_6039() && (Boolean)this.shieldsConfig.getValue() && !(Boolean)this.grimConfig.getValue());
   }

   public boolean checkScreen() {
      return mc.field_1755 != null && !(mc.field_1755 instanceof class_408) && !(mc.field_1755 instanceof class_498) && !(mc.field_1755 instanceof class_418);
   }

   public List<class_2338> getIntersectingWebs() {
      int radius = 5;
      List<class_2338> blocks = new ArrayList();

      for(int x = radius; x > -radius; --x) {
         for(int y = radius; y > -radius; --y) {
            for(int z = radius; z > -radius; --z) {
               class_2338 blockPos = class_2338.method_49637(mc.field_1724.method_23317() + (double)x, mc.field_1724.method_23318() + (double)y, mc.field_1724.method_23321() + (double)z);
               class_2680 state = mc.field_1687.method_8320(blockPos);
               if (state.method_26204() instanceof class_2560 && (Boolean)this.websConfig.getValue() || state.method_26204() instanceof class_3830 && (Boolean)this.berryBushConfig.getValue()) {
                  blocks.add(blockPos);
               }
            }
         }
      }

      return blocks;
   }

   public boolean getStrafeFix() {
      return (Boolean)this.strafeFixConfig.getValue();
   }
}
