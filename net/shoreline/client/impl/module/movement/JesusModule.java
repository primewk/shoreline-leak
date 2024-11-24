package net.shoreline.client.impl.module.movement;

import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2404;
import net.minecraft.class_259;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2828;
import net.minecraft.class_304;
import net.minecraft.class_3532;
import net.minecraft.class_3612;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.entity.player.PlayerJumpEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.impl.event.world.BlockCollisionEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.mixin.accessor.AccessorKeyBinding;
import net.shoreline.client.mixin.accessor.AccessorPlayerMoveC2SPacket;
import net.shoreline.client.util.string.EnumFormatter;

public class JesusModule extends ToggleModule {
   Config<JesusModule.JesusMode> modeConfig;
   Config<Boolean> strictConfig;
   private int floatTimer;
   private boolean fluidState;
   private double floatOffset;

   public JesusModule() {
      super("Jesus", "Allow player to walk on water", ModuleCategory.MOVEMENT);
      this.modeConfig = new EnumConfig("Mode", "The mode for walking on water", JesusModule.JesusMode.SOLID, JesusModule.JesusMode.values());
      this.strictConfig = new BooleanConfig("Strict", "NCP Updated bypass for floating offsets", false, () -> {
         return this.modeConfig.getValue() == JesusModule.JesusMode.SOLID;
      });
      this.floatTimer = 1000;
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
   }

   public void onDisable() {
      this.floatOffset = 0.0D;
      this.floatTimer = 1000;
      class_304.method_1416(((AccessorKeyBinding)mc.field_1690.field_1903).getBoundKey(), false);
   }

   @EventListener
   public void onBlockCollision(BlockCollisionEvent event) {
      class_2680 state = event.getState();
      if (!Modules.FLIGHT.isEnabled() && !Modules.PACKET_FLY.isEnabled() && !mc.field_1724.method_7325() && !mc.field_1724.method_5809() && !state.method_26227().method_15769()) {
         if (this.modeConfig.getValue() != JesusModule.JesusMode.DOLPHIN && (state.method_26204() == class_2246.field_10382 | state.method_26227().method_15772() == class_3612.field_15910 || state.method_26204() == class_2246.field_10164)) {
            event.cancel();
            event.setVoxelShape(class_259.method_1078(new class_238(0.0D, 0.0D, 0.0D, 1.0D, 0.99D, 1.0D)));
            if (mc.field_1724.method_5854() != null) {
               event.setVoxelShape(class_259.method_1078(new class_238(0.0D, 0.0D, 0.0D, 1.0D, 0.949999988079071D, 1.0D)));
            } else if (this.modeConfig.getValue() == JesusModule.JesusMode.TRAMPOLINE) {
               event.setVoxelShape(class_259.method_1078(new class_238(0.0D, 0.0D, 0.0D, 1.0D, 0.96D, 1.0D)));
            }
         }

      }
   }

   @EventListener
   public void onPlayerJump(PlayerJumpEvent event) {
      if (!this.isInFluid() && this.isOnFluid()) {
         event.cancel();
      }

   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (Modules.FLIGHT.isEnabled() || Modules.PACKET_FLY.isEnabled()) {
            return;
         }

         if (this.modeConfig.getValue() == JesusModule.JesusMode.SOLID) {
            if (!this.isInFluid() && !(mc.field_1724.field_6017 > 3.0F) && mc.field_1724.method_5715()) {
            }

            if (!mc.field_1690.field_1832.method_1434() && !mc.field_1690.field_1903.method_1434()) {
               if (this.isInFluid()) {
                  this.floatTimer = 0;
                  Managers.MOVEMENT.setMotionY(0.11D);
                  return;
               }

               if (this.floatTimer == 0) {
                  Managers.MOVEMENT.setMotionY(0.3D);
               } else if (this.floatTimer == 1) {
                  Managers.MOVEMENT.setMotionY(0.0D);
               }

               ++this.floatTimer;
            }
         } else if (this.modeConfig.getValue() == JesusModule.JesusMode.DOLPHIN && this.isInFluid() && !mc.field_1690.field_1832.method_1434() && !mc.field_1690.field_1903.method_1434()) {
            class_304.method_1416(((AccessorKeyBinding)mc.field_1690.field_1903).getBoundKey(), true);
         }
      }

   }

   @EventListener
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (!Modules.FLIGHT.isEnabled() && !Modules.PACKET_FLY.isEnabled()) {
         if (event.getStage() == EventStage.PRE && this.modeConfig.getValue() == JesusModule.JesusMode.TRAMPOLINE) {
            boolean inFluid = this.getFluidBlockInBB(mc.field_1724.method_5829()) != null;
            if (inFluid && !mc.field_1724.method_5715()) {
               mc.field_1724.method_24830(false);
            }

            class_2248 block = mc.field_1687.method_8320(new class_2338((int)Math.floor(mc.field_1724.method_23317()), (int)Math.floor(mc.field_1724.method_23318()), (int)Math.floor(mc.field_1724.method_23321()))).method_26204();
            if (this.fluidState && !mc.field_1724.method_31549().field_7479 && !mc.field_1724.method_5799()) {
               if (mc.field_1724.method_18798().field_1351 < -0.3D || mc.field_1724.method_24828() || mc.field_1724.method_21754()) {
                  this.fluidState = false;
                  return;
               }

               Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 / 0.9800000190734863D + 0.08D);
               Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 - 0.03120000000005D);
            }

            if (this.isInFluid()) {
               Managers.MOVEMENT.setMotionY(0.1D);
               this.fluidState = false;
               return;
            }

            if (!this.isInFluid() && block instanceof class_2404 && mc.field_1724.method_18798().field_1351 < 0.2D) {
               Managers.MOVEMENT.setMotionY((Boolean)this.strictConfig.getValue() ? 0.184D : 0.5D);
               this.fluidState = true;
            }
         }

      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (!event.isClientPacket() && mc.field_1724 != null && mc.method_1562() != null && mc.field_1724.field_6012 > 20 && !Modules.FLIGHT.isEnabled() && !Modules.PACKET_FLY.isEnabled()) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2828) {
            class_2828 packet = (class_2828)var3;
            if (packet.method_36171() && this.modeConfig.getValue() == JesusModule.JesusMode.SOLID && !this.isInFluid() && this.isOnFluid() && mc.field_1724.field_6017 <= 3.0F) {
               double y = packet.method_12268(mc.field_1724.method_23318());
               if (!(Boolean)this.strictConfig.getValue()) {
                  this.floatOffset = mc.field_1724.field_6012 % 2 == 0 ? 0.0D : 0.05D;
               }

               ((AccessorPlayerMoveC2SPacket)packet).hookSetY(y - this.floatOffset);
               if ((Boolean)this.strictConfig.getValue()) {
                  this.floatOffset += 0.12D;
                  if (this.floatOffset > 0.4D) {
                     this.floatOffset = 0.2D;
                  }
               }
            }
         }

      }
   }

   public boolean isInFluid() {
      return mc.field_1724.method_5799() || mc.field_1724.method_5771();
   }

   public class_2680 getFluidBlockInBB(class_238 box) {
      return this.getFluidBlockInBB(class_3532.method_15357(box.field_1322 - 0.2D));
   }

   public class_2680 getFluidBlockInBB(int minY) {
      for(int i = class_3532.method_15357(mc.field_1724.method_5829().field_1323); i < class_3532.method_15384(mc.field_1724.method_5829().field_1320); ++i) {
         for(int j = class_3532.method_15357(mc.field_1724.method_5829().field_1321); j < class_3532.method_15384(mc.field_1724.method_5829().field_1324); ++j) {
            class_2680 state = mc.field_1687.method_8320(new class_2338(i, minY, j));
            if (state.method_26204() instanceof class_2404) {
               return state;
            }
         }
      }

      return null;
   }

   public boolean isOnFluid() {
      if (mc.field_1724.field_6017 >= 3.0F) {
         return false;
      } else {
         class_238 bb = mc.field_1724.method_5854() != null ? mc.field_1724.method_5854().method_5829().method_35580(0.0D, 0.0D, 0.0D).method_989(0.0D, -0.05000000074505806D, 0.0D) : mc.field_1724.method_5829().method_35580(0.0D, 0.0D, 0.0D).method_989(0.0D, -0.05000000074505806D, 0.0D);
         boolean onLiquid = false;
         int y = (int)bb.field_1322;

         for(int x = class_3532.method_15357(bb.field_1323); x < class_3532.method_15357(bb.field_1320 + 1.0D); ++x) {
            for(int z = class_3532.method_15357(bb.field_1321); z < class_3532.method_15357(bb.field_1324 + 1.0D); ++z) {
               class_2248 block = mc.field_1687.method_8320(new class_2338(x, y, z)).method_26204();
               if (block != class_2246.field_10124) {
                  if (!(block instanceof class_2404)) {
                     return false;
                  }

                  onLiquid = true;
               }
            }
         }

         return onLiquid;
      }
   }

   public static enum JesusMode {
      SOLID,
      DOLPHIN,
      TRAMPOLINE;

      // $FF: synthetic method
      private static JesusModule.JesusMode[] $values() {
         return new JesusModule.JesusMode[]{SOLID, DOLPHIN, TRAMPOLINE};
      }
   }
}
