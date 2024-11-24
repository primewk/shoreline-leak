package net.shoreline.client.impl.module.movement;

import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1671;
import net.minecraft.class_1781;
import net.minecraft.class_1799;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.entity.player.TravelEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.mixin.accessor.AccessorPlayerMoveC2SPacket;
import net.shoreline.client.util.string.EnumFormatter;

public class ElytraFlyModule extends ToggleModule {
   Config<ElytraFlyModule.FlyMode> modeConfig;
   Config<Float> speedConfig;
   Config<Float> vspeedConfig;
   Config<Boolean> instantFlyConfig;
   Config<Boolean> fireworkConfig;
   private float pitch;
   private class_1671 fireworkRocketEntity;

   public ElytraFlyModule() {
      super("ElytraFly", "Allows you to fly freely using an elytra", ModuleCategory.MOVEMENT);
      this.modeConfig = new EnumConfig("Mode", "The mode for elytra flight", ElytraFlyModule.FlyMode.CONTROL, ElytraFlyModule.FlyMode.values());
      this.speedConfig = new NumberConfig("Speed", "The horizontal flight speed", 0.1F, 2.5F, 10.0F);
      this.vspeedConfig = new NumberConfig("VerticalSpeed", "The vertical flight speed", 0.1F, 1.0F, 5.0F);
      this.instantFlyConfig = new BooleanConfig("InstantFly", "Automatically activates elytra from the ground", false);
      this.fireworkConfig = new BooleanConfig("Fireworks", "Uses fireworks when flying", false, () -> {
         return this.modeConfig.getValue() != ElytraFlyModule.FlyMode.PACKET;
      });
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
   }

   @EventListener
   public void onTravel(TravelEvent event) {
      if (event.getStage() == EventStage.PRE && mc.field_1724 != null && mc.field_1687 != null && mc.field_1724.method_6128()) {
         float yaw;
         switch((ElytraFlyModule.FlyMode)this.modeConfig.getValue()) {
         case CONTROL:
            event.cancel();
            float forward = mc.field_1724.field_3913.field_3905;
            yaw = mc.field_1724.field_3913.field_3907;
            float yaw = mc.field_1724.method_36454();
            if (forward == 0.0F && yaw == 0.0F) {
               Managers.MOVEMENT.setMotionXZ(0.0D, 0.0D);
            } else {
               this.pitch = 12.0F;
               double rx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
               double rz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
               Managers.MOVEMENT.setMotionXZ((double)(forward * (Float)this.speedConfig.getValue()) * rx + (double)(yaw * (Float)this.speedConfig.getValue()) * rz, (double)(forward * (Float)this.speedConfig.getValue()) * rz - (double)(yaw * (Float)this.speedConfig.getValue()) * rx);
            }

            Managers.MOVEMENT.setMotionY(0.0D);
            this.pitch = 0.0F;
            if (mc.field_1690.field_1903.method_1434()) {
               this.pitch = -51.0F;
               Managers.MOVEMENT.setMotionY((double)(Float)this.vspeedConfig.getValue());
            } else if (mc.field_1690.field_1832.method_1434()) {
               Managers.MOVEMENT.setMotionY((double)(-(Float)this.vspeedConfig.getValue()));
            }
            break;
         case BOOST:
            event.cancel();
            mc.field_1724.field_42108.method_48567(0.0F);
            this.glideElytraVec(mc.field_1724.method_36455());
            boolean boost = mc.field_1690.field_1903.method_1434();
            yaw = mc.field_1724.method_36454() * 0.017453292F;
            if (boost) {
               double sin = (double)(-class_3532.method_15374(yaw));
               double cos = (double)class_3532.method_15362(yaw);
               double motionX = sin * (double)(Float)this.speedConfig.getValue() / 20.0D;
               double motionZ = cos * (double)(Float)this.speedConfig.getValue() / 20.0D;
               Managers.MOVEMENT.setMotionXZ(mc.field_1724.method_18798().field_1352 + motionX, mc.field_1724.method_18798().field_1350 + motionZ);
            }
         }

      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (mc.field_1724 != null) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2828) {
            class_2828 packet = (class_2828)var3;
            if (packet.method_36172() && mc.field_1724.method_6128()) {
               if (this.modeConfig.getValue() == ElytraFlyModule.FlyMode.CONTROL) {
                  if (mc.field_1690.field_1913.method_1434()) {
                     ((AccessorPlayerMoveC2SPacket)packet).hookSetYaw(packet.method_12271(0.0F) - 90.0F);
                  }

                  if (mc.field_1690.field_1849.method_1434()) {
                     ((AccessorPlayerMoveC2SPacket)packet).hookSetYaw(packet.method_12271(0.0F) + 90.0F);
                  }
               }

               ((AccessorPlayerMoveC2SPacket)packet).hookSetPitch(this.pitch);
            }
         }

      }
   }

   private void boostFirework() {
      int slot = -1;

      int prev;
      for(prev = 0; prev < 9; ++prev) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(prev);
         if (!stack.method_7960() && stack.method_7909() instanceof class_1781) {
            slot = prev;
            break;
         }
      }

      if (slot != -1) {
         prev = mc.field_1724.method_31548().field_7545;
         Managers.INVENTORY.setClientSlot(slot);
         mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
         Managers.INVENTORY.setClientSlot(prev);
      }

   }

   private void glideElytraVec(float pitch) {
      double d = 0.08D;
      boolean bl = mc.field_1724.method_18798().field_1351 <= 0.0D;
      if (bl && mc.field_1724.method_6059(class_1294.field_5906)) {
         d = 0.01D;
      }

      class_243 vec3d4 = mc.field_1724.method_18798();
      class_243 vec3d5 = this.getRotationVector(pitch, mc.field_1724.method_36454());
      float f = pitch * 0.017453292F;
      double i = Math.sqrt(vec3d5.field_1352 * vec3d5.field_1352 + vec3d5.field_1350 * vec3d5.field_1350);
      double j = vec3d4.method_37267();
      double k = vec3d5.method_1033();
      double l = Math.cos((double)f);
      l = l * l * Math.min(1.0D, k / 0.4D);
      vec3d4 = mc.field_1724.method_18798().method_1031(0.0D, d * (-1.0D + l * 0.75D), 0.0D);
      if (f < 0.0F && i > 0.0D) {
         double m = j * (double)(-class_3532.method_15374(f)) * 0.04D;
         vec3d4 = vec3d4.method_1031(-vec3d5.field_1352 * m / i, m * 3.2D, -vec3d5.field_1350 * m / i);
      }

      mc.field_1724.method_18799(vec3d4.method_18805(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D));
   }

   protected final class_243 getRotationVector(float pitch, float yaw) {
      float f = pitch * 0.017453292F;
      float g = -yaw * 0.017453292F;
      float h = class_3532.method_15362(g);
      float i = class_3532.method_15374(g);
      float j = class_3532.method_15362(f);
      float k = class_3532.method_15374(f);
      return new class_243((double)(i * j), (double)(-k), (double)(h * j));
   }

   public static enum FlyMode {
      CONTROL,
      BOOST,
      FACTORIZE,
      PACKET,
      BOUNCE;

      // $FF: synthetic method
      private static ElytraFlyModule.FlyMode[] $values() {
         return new ElytraFlyModule.FlyMode[]{CONTROL, BOOST, FACTORIZE, PACKET, BOUNCE};
      }
   }
}
