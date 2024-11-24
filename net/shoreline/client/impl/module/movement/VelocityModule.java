package net.shoreline.client.impl.module.movement;

import java.text.DecimalFormat;
import net.minecraft.class_1297;
import net.minecraft.class_1536;
import net.minecraft.class_2350;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_2664;
import net.minecraft.class_2743;
import net.minecraft.class_2846;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2846.class_2847;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.NumberDisplay;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.entity.player.PushEntityEvent;
import net.shoreline.client.impl.event.entity.player.PushFluidsEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PushOutOfBlocksEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.mixin.accessor.AccessorClientWorld;
import net.shoreline.client.mixin.accessor.AccessorEntityVelocityUpdateS2CPacket;
import net.shoreline.client.mixin.accessor.AccessorExplosionS2CPacket;
import net.shoreline.client.util.string.EnumFormatter;

public class VelocityModule extends ToggleModule {
   Config<Boolean> knockbackConfig = new BooleanConfig("Knockback", "Removes player knockback velocity", true);
   Config<Boolean> explosionConfig = new BooleanConfig("Explosion", "Removes player explosion velocity", true);
   Config<VelocityModule.VelocityMode> modeConfig;
   Config<Float> horizontalConfig;
   Config<Float> verticalConfig;
   Config<Boolean> pushEntitiesConfig;
   Config<Boolean> pushBlocksConfig;
   Config<Boolean> pushLiquidsConfig;
   Config<Boolean> pushFishhookConfig;
   private boolean cancelVelocity;

   public VelocityModule() {
      super("Velocity", "Reduces the amount of player knockback velocity", ModuleCategory.MOVEMENT);
      this.modeConfig = new EnumConfig("Mode", "The mode for velocity", VelocityModule.VelocityMode.NORMAL, VelocityModule.VelocityMode.values());
      this.horizontalConfig = new NumberConfig("Horizontal", "How much horizontal knock-back to take", 0.0F, 0.0F, 100.0F, NumberDisplay.PERCENT, () -> {
         return this.modeConfig.getValue() == VelocityModule.VelocityMode.NORMAL;
      });
      this.verticalConfig = new NumberConfig("Vertical", "How much vertical knock-back to take", 0.0F, 0.0F, 100.0F, NumberDisplay.PERCENT, () -> {
         return this.modeConfig.getValue() == VelocityModule.VelocityMode.NORMAL;
      });
      this.pushEntitiesConfig = new BooleanConfig("NoPush-Entities", "Prevents being pushed away from entities", true);
      this.pushBlocksConfig = new BooleanConfig("NoPush-Blocks", "Prevents being pushed out of blocks", true);
      this.pushLiquidsConfig = new BooleanConfig("NoPush-Liquids", "Prevents being pushed by flowing liquids", true);
      this.pushFishhookConfig = new BooleanConfig("NoPush-Fishhook", "Prevents being pulled by fishing rod hooks", true);
   }

   public String getModuleData() {
      if (this.modeConfig.getValue() == VelocityModule.VelocityMode.NORMAL) {
         DecimalFormat decimal = new DecimalFormat("0.0");
         return String.format("H:%s%%, V:%s%%", decimal.format(this.horizontalConfig.getValue()), decimal.format(this.verticalConfig.getValue()));
      } else {
         return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
      }
   }

   public void onEnable() {
      this.cancelVelocity = false;
   }

   public void onDisable() {
      if (this.cancelVelocity) {
         if (this.modeConfig.getValue() == VelocityModule.VelocityMode.GRIM) {
            float yaw = Managers.ROTATION.getServerYaw();
            float pitch = Managers.ROTATION.getServerPitch();
            Managers.NETWORK.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), yaw, pitch, mc.field_1724.method_24828()));
            if (!mc.field_1724.method_20448()) {
               Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12971, mc.field_1724.method_24515().method_10084(), class_2350.field_11033));
            }

            Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12973, mc.field_1724.method_20448() ? mc.field_1724.method_24515() : mc.field_1724.method_24515().method_10084(), class_2350.field_11033));
         }

         this.cancelVelocity = false;
      }

   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_2596 var5 = event.getPacket();
         if (var5 instanceof class_2743) {
            class_2743 packet = (class_2743)var5;
            if ((Boolean)this.knockbackConfig.getValue()) {
               if (packet.method_11818() != mc.field_1724.method_5628()) {
                  return;
               }

               switch((VelocityModule.VelocityMode)this.modeConfig.getValue()) {
               case NORMAL:
                  if ((Float)this.horizontalConfig.getValue() == 0.0F && (Float)this.verticalConfig.getValue() == 0.0F) {
                     event.cancel();
                     return;
                  }

                  ((AccessorEntityVelocityUpdateS2CPacket)packet).setVelocityX((int)((float)packet.method_11815() * ((Float)this.horizontalConfig.getValue() / 100.0F)));
                  ((AccessorEntityVelocityUpdateS2CPacket)packet).setVelocityY((int)((float)packet.method_11816() * ((Float)this.verticalConfig.getValue() / 100.0F)));
                  ((AccessorEntityVelocityUpdateS2CPacket)packet).setVelocityZ((int)((float)packet.method_11819() * ((Float)this.horizontalConfig.getValue() / 100.0F)));
                  return;
               case GRIM:
                  if (!Managers.ANTICHEAT.hasPassed(100L)) {
                     return;
                  }

                  event.cancel();
                  this.cancelVelocity = true;
                  return;
               default:
                  return;
               }
            }
         }

         var5 = event.getPacket();
         if (var5 instanceof class_2664) {
            class_2664 packet = (class_2664)var5;
            if ((Boolean)this.explosionConfig.getValue()) {
               switch((VelocityModule.VelocityMode)this.modeConfig.getValue()) {
               case NORMAL:
                  if ((Float)this.horizontalConfig.getValue() == 0.0F && (Float)this.verticalConfig.getValue() == 0.0F) {
                     event.cancel();
                     break;
                  }

                  ((AccessorExplosionS2CPacket)packet).setPlayerVelocityX(packet.method_11472() * ((Float)this.horizontalConfig.getValue() / 100.0F));
                  ((AccessorExplosionS2CPacket)packet).setPlayerVelocityY(packet.method_11473() * ((Float)this.verticalConfig.getValue() / 100.0F));
                  ((AccessorExplosionS2CPacket)packet).setPlayerVelocityZ(packet.method_11474() * ((Float)this.horizontalConfig.getValue() / 100.0F));
                  break;
               case GRIM:
                  if (!Managers.ANTICHEAT.hasPassed(100L)) {
                     return;
                  }

                  event.cancel();
                  this.cancelVelocity = true;
               }

               if (event.isCanceled()) {
                  mc.method_40000(() -> {
                     ((AccessorClientWorld)mc.field_1687).hookPlaySound(packet.method_11475(), packet.method_11477(), packet.method_11478(), class_3417.field_15152, class_3419.field_15245, 4.0F, (1.0F + (RANDOM.nextFloat() - RANDOM.nextFloat()) * 0.2F) * 0.7F, false, RANDOM.nextLong());
                  });
               }

               return;
            }
         }

         var5 = event.getPacket();
         if (var5 instanceof class_2663) {
            class_2663 packet = (class_2663)var5;
            if (packet.method_11470() == 31 && (Boolean)this.pushFishhookConfig.getValue()) {
               class_1297 entity = packet.method_11469(mc.field_1687);
               if (entity instanceof class_1536) {
                  class_1536 hook = (class_1536)entity;
                  if (hook.method_26957() == mc.field_1724) {
                     event.cancel();
                  }
               }
            }
         }

      }
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE && this.cancelVelocity) {
         if (this.modeConfig.getValue() == VelocityModule.VelocityMode.GRIM && Managers.ANTICHEAT.hasPassed(100L)) {
            float yaw = Managers.ROTATION.getServerYaw();
            float pitch = Managers.ROTATION.getServerPitch();
            Managers.NETWORK.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), yaw, pitch, mc.field_1724.method_24828()));
            Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12973, mc.field_1724.method_20448() ? mc.field_1724.method_24515() : mc.field_1724.method_24515().method_10084(), class_2350.field_11033));
         }

         this.cancelVelocity = false;
      }

   }

   @EventListener
   public void onPushEntity(PushEntityEvent event) {
      if ((Boolean)this.pushEntitiesConfig.getValue() && event.getPushed().equals(mc.field_1724)) {
         event.cancel();
      }

   }

   @EventListener
   public void onPushOutOfBlocks(PushOutOfBlocksEvent event) {
      if ((Boolean)this.pushBlocksConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onPushFluid(PushFluidsEvent event) {
      if ((Boolean)this.pushLiquidsConfig.getValue()) {
         event.cancel();
      }

   }

   private static enum VelocityMode {
      NORMAL,
      GRIM;

      // $FF: synthetic method
      private static VelocityModule.VelocityMode[] $values() {
         return new VelocityModule.VelocityMode[]{NORMAL, GRIM};
      }
   }
}
