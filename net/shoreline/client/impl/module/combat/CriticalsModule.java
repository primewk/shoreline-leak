package net.shoreline.client.impl.module.combat;

import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2879;
import net.minecraft.class_2828.class_2829;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.imixin.IPlayerInteractEntityC2SPacket;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;
import net.shoreline.client.util.network.InteractType;
import net.shoreline.client.util.player.InventoryUtil;
import net.shoreline.client.util.string.EnumFormatter;
import net.shoreline.client.util.world.EntityUtil;

public class CriticalsModule extends ToggleModule {
   Config<CriticalsModule.CritMode> modeConfig;
   Config<Boolean> packetSyncConfig;
   private class_2824 attackPacket;
   private class_2879 swingPacket;
   private final Timer attackTimer;

   public CriticalsModule() {
      super("Criticals", "Modifies attacks to always land critical hits", ModuleCategory.COMBAT);
      this.modeConfig = new EnumConfig("Mode", "Mode for critical attack modifier", CriticalsModule.CritMode.PACKET, CriticalsModule.CritMode.values());
      this.packetSyncConfig = new BooleanConfig("Tick-Sync", "Syncs the cached packet interaction to the next tick", false);
      this.attackTimer = new CacheTimer();
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.POST) {
         if ((Boolean)this.packetSyncConfig.getValue() && this.attackPacket != null && this.swingPacket != null) {
            Managers.NETWORK.sendPacket(this.attackPacket);
            Managers.NETWORK.sendPacket(this.swingPacket);
            this.attackPacket = null;
            this.swingPacket = null;
         }

      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (!Modules.AURA.isEnabled()) {
         class_2596 var4 = event.getPacket();
         if (var4 instanceof IPlayerInteractEntityC2SPacket) {
            IPlayerInteractEntityC2SPacket packet = (IPlayerInteractEntityC2SPacket)var4;
            if (packet.getType() == InteractType.ATTACK && !event.isClientPacket()) {
               if (this.isGrim()) {
                  if (!mc.field_1724.method_24828()) {
                     double x = mc.field_1724.method_23317();
                     double y = mc.field_1724.method_23318();
                     double z = mc.field_1724.method_23321();
                     Managers.NETWORK.sendPacket(new class_2829(x, y - 1.0E-6D, z, false));
                  }

                  return;
               }

               if (!Managers.POSITION.isOnGround() || mc.field_1724.method_3144() || mc.field_1724.method_6128() || mc.field_1724.method_5799() || mc.field_1724.method_5771() || mc.field_1724.method_21754() || mc.field_1724.method_6059(class_1294.field_5919) || mc.field_1724.field_3913.field_3904 || InventoryUtil.isHolding32k()) {
                  return;
               }

               class_1297 e = packet.getEntity();
               if (e != null && e.method_5805() && !(e instanceof class_1511)) {
                  if (this.attackTimer.passed(500)) {
                     event.cancel();
                     if (EntityUtil.isVehicle(e)) {
                        if (this.modeConfig.getValue() == CriticalsModule.CritMode.PACKET) {
                           for(int i = 0; i < 5; ++i) {
                              Managers.NETWORK.sendPacket(class_2824.method_34206(e, Managers.POSITION.isSneaking()));
                              Managers.NETWORK.sendPacket(new class_2879(class_1268.field_5808));
                           }
                        }

                        return;
                     }

                     this.attackPacket = class_2824.method_34206(e, Managers.POSITION.isSneaking());
                     if (!(Boolean)this.packetSyncConfig.getValue()) {
                        Managers.NETWORK.sendPacket(this.attackPacket);
                     }

                     this.preAttackPacket();
                     mc.field_1724.method_7277(e);
                     this.attackTimer.reset();
                  }

                  return;
               }

               return;
            }
         }

         var4 = event.getPacket();
         if (var4 instanceof class_2879) {
            class_2879 packet = (class_2879)var4;
            if ((Boolean)this.packetSyncConfig.getValue() && this.attackPacket != null) {
               event.cancel();
               this.swingPacket = packet;
            }
         }

      }
   }

   public void preAttackPacket() {
      double x = Managers.POSITION.getX();
      double y = Managers.POSITION.getY();
      double z = Managers.POSITION.getZ();
      switch((CriticalsModule.CritMode)this.modeConfig.getValue()) {
      case VANILLA:
         double d = 1.0E-7D + 1.0E-7D * (1.0D + (double)RANDOM.nextInt(RANDOM.nextBoolean() ? 34 : 43));
         Managers.NETWORK.sendPacket(new class_2829(x, y + 0.10159999877214432D + d * 3.0D, z, false));
         Managers.NETWORK.sendPacket(new class_2829(x, y + 0.02019999921321869D + d * 2.0D, z, false));
         Managers.NETWORK.sendPacket(new class_2829(x, y + 3.239E-4D + d, z, false));
         break;
      case PACKET:
         Managers.NETWORK.sendPacket(new class_2829(x, y + 0.05000000074505806D, z, false));
         Managers.NETWORK.sendPacket(new class_2829(x, y, z, false));
         Managers.NETWORK.sendPacket(new class_2829(x, y + 0.029999999329447746D, z, false));
         Managers.NETWORK.sendPacket(new class_2829(x, y, z, false));
         break;
      case PACKET_STRICT:
         Managers.NETWORK.sendPacket(new class_2829(x, y + 0.10999999940395355D, z, false));
         Managers.NETWORK.sendPacket(new class_2829(x, y + 0.11000135540962219D, z, false));
         Managers.NETWORK.sendPacket(new class_2829(x, y + 1.357900032417092E-6D, z, false));
         break;
      case LOW_HOP:
         Managers.MOVEMENT.setMotionY(0.3425D);
      }

   }

   public boolean isGrim() {
      return this.modeConfig.getValue() == CriticalsModule.CritMode.GRIM;
   }

   public static enum CritMode {
      PACKET,
      PACKET_STRICT,
      VANILLA,
      GRIM,
      LOW_HOP;

      // $FF: synthetic method
      private static CriticalsModule.CritMode[] $values() {
         return new CriticalsModule.CritMode[]{PACKET, PACKET_STRICT, VANILLA, GRIM, LOW_HOP};
      }
   }
}
