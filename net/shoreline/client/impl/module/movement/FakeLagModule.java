package net.shoreline.client.impl.module.movement;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.world.FakePlayerEntity;

public class FakeLagModule extends ToggleModule {
   Config<FakeLagModule.LagMode> modeConfig;
   Config<Boolean> pulseConfig;
   Config<Float> factorConfig;
   Config<Boolean> renderConfig;
   private FakePlayerEntity serverModel;
   private boolean blinking;
   private final Queue<class_2596<?>> packets;

   public FakeLagModule() {
      super("FakeLag", "Withholds packets from the server, creating clientside lag", ModuleCategory.MOVEMENT);
      this.modeConfig = new EnumConfig("Mode", "The mode for caching packets", FakeLagModule.LagMode.BLINK, FakeLagModule.LagMode.values());
      this.pulseConfig = new BooleanConfig("Pulse", "Releases packets at intervals", false);
      this.factorConfig = new NumberConfig("Factor", "The factor for packet intervals", 0.0F, 1.0F, 10.0F, () -> {
         return (Boolean)this.pulseConfig.getValue();
      });
      this.renderConfig = new BooleanConfig("Render", "Renders the serverside player position", true);
      this.packets = new LinkedBlockingQueue();
   }

   public void onEnable() {
      if ((Boolean)this.renderConfig.getValue()) {
         this.serverModel = new FakePlayerEntity(mc.field_1724, mc.method_53462());
         this.serverModel.despawnPlayer();
         this.serverModel.spawnPlayer();
      }

   }

   public void onDisable() {
      if (mc.field_1724 != null) {
         if (!this.packets.isEmpty()) {
            Iterator var1 = this.packets.iterator();

            while(var1.hasNext()) {
               class_2596<?> p = (class_2596)var1.next();
               Managers.NETWORK.sendPacket(p);
            }

            this.packets.clear();
         }

         if (this.serverModel != null) {
            this.serverModel.despawnPlayer();
         }

      }
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE && (Boolean)this.pulseConfig.getValue() && (float)this.packets.size() > (Float)this.factorConfig.getValue() * 10.0F) {
         this.blinking = true;
         if (!this.packets.isEmpty()) {
            Iterator var2 = this.packets.iterator();

            while(var2.hasNext()) {
               class_2596<?> p = (class_2596)var2.next();
               Managers.NETWORK.sendPacket(p);
            }
         }

         this.packets.clear();
         if (this.serverModel != null) {
            this.serverModel.method_5719(mc.field_1724);
            this.serverModel.method_5847(mc.field_1724.field_6241);
         }

         this.blinking = false;
      }

   }

   @EventListener
   public void onDisconnectEvent(DisconnectEvent event) {
      this.disable();
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (mc.field_1724 != null && !mc.field_1724.method_3144() && !this.blinking) {
         if (event.getPacket() instanceof class_2846 || event.getPacket() instanceof class_2828 || event.getPacket() instanceof class_2848 || event.getPacket() instanceof class_2879 || event.getPacket() instanceof class_2824 || event.getPacket() instanceof class_2885 || event.getPacket() instanceof class_2886) {
            event.cancel();
            this.packets.add(event.getPacket());
         }

      }
   }

   public static enum LagMode {
      BLINK;

      // $FF: synthetic method
      private static FakeLagModule.LagMode[] $values() {
         return new FakeLagModule.LagMode[]{BLINK};
      }
   }
}
