package net.shoreline.client.impl.module.combat;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import net.minecraft.class_2604;
import net.minecraft.class_2663;
import net.minecraft.class_2664;
import net.minecraft.class_2670;
import net.minecraft.class_2743;
import net.minecraft.class_2749;
import net.minecraft.class_2767;
import net.minecraft.class_2770;
import net.minecraft.class_2777;
import net.minecraft.class_5900;
import net.minecraft.class_6373;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.math.timer.TickTimer;
import net.shoreline.client.util.math.timer.Timer;

public final class BackTrackModule extends ToggleModule {
   Config<Integer> delayConfig = new NumberConfig("Delay", "The delay before throttling packets again", 0, 100, 1000);
   private final Queue<class_2596<?>> packetQueue = new ConcurrentLinkedQueue();
   private boolean blockingPackets;
   private final Timer timer = new TickTimer();
   private class_1309 attackingEntity;
   private class_2547 packetListener;
   private class_243 lastServerPos;
   private class_243 serverPos;
   private class_238 hitBox;

   public BackTrackModule() {
      super("Backtrack", "funny", ModuleCategory.COMBAT);
   }

   protected void onDisable() {
      super.onDisable();
      if (this.packetListener != null) {
         this.emptyPackets();
      }

      this.packetQueue.clear();
      this.packetListener = null;
      this.serverPos = null;
      this.attackingEntity = null;
      this.hitBox = null;
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1687 != null && mc.field_1724 != null) {
         this.packetListener = event.getPacketListener();
      } else {
         this.packetQueue.clear();
         this.blockingPackets = false;
      }

      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2777) {
         class_2777 packet = (class_2777)var3;
         if (this.attackingEntity != null && this.attackingEntity.method_5628() == packet.method_11916()) {
            this.lastServerPos = this.serverPos;
            this.serverPos = new class_243(packet.method_11917(), packet.method_11919(), packet.method_11918());
         }
      }

      if (this.blockingPackets && this.packetListener != null) {
         if (this.shouldCancelPacket(event.getPacket())) {
            event.setCanceled(true);
            this.packetQueue.add(event.getPacket());
         }

      }
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      AuraModule auraModule = Modules.AURA;
      if (auraModule.isEnabled()) {
         class_1297 var4 = auraModule.getEntityTarget();
         if (var4 instanceof class_1309) {
            class_1309 auraTarget = (class_1309)var4;
            this.attackingEntity = auraTarget;
            if (!this.timer.passed((Integer)this.delayConfig.getValue() / 50)) {
               return;
            }

            if (this.hitBox != null) {
               class_243 eyes = Managers.POSITION.getEyePos();
               double dist = eyes.method_1022(this.attackingEntity.method_19538());
               if (!auraModule.isInAttackRange(dist, eyes, this.attackingEntity.method_19538()) && dist <= (double)(Float)auraModule.searchRangeConfig.getValue()) {
                  this.blockingPackets = true;
               } else {
                  this.blockingPackets = false;
                  this.timer.reset();
                  this.emptyPackets();
               }
            }

            return;
         }
      }

      this.attackingEntity = null;
      if (this.blockingPackets) {
         this.emptyPackets();
      }

      this.blockingPackets = false;
      this.serverPos = null;
      this.lastServerPos = null;
      this.hitBox = null;
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         this.hitBox = this.getBackTrackedBox(event.getTickDelta());
         if (this.hitBox != null) {
            RenderManager.renderBox(event.getMatrices(), this.hitBox, Modules.COLORS.getRGB(120));
            RenderManager.renderBoundingBox(event.getMatrices(), this.hitBox, 1.5F, Modules.COLORS.getRGB());
         }

      }
   }

   private class_238 getBackTrackedBox(float tickDelta) {
      return this.serverPos != null && this.attackingEntity != null ? this.attackingEntity.method_18377(this.attackingEntity.method_18376()).method_30231(this.serverPos.field_1352, this.serverPos.field_1351, this.serverPos.field_1350) : null;
   }

   private boolean shouldCancelPacket(class_2596<?> packet) {
      if (Modules.VELOCITY.isEnabled()) {
         return !(packet instanceof class_2743) && !(packet instanceof class_2664);
      } else {
         return !(packet instanceof class_2749) && !(packet instanceof class_2767) && !(packet instanceof class_2770) && !(packet instanceof class_2604) && !(packet instanceof class_2663) && !(packet instanceof class_5900) && !(packet instanceof class_6373) && !(packet instanceof class_2670);
      }
   }

   private void emptyPackets() {
      if (!this.packetQueue.isEmpty() && this.packetListener != null) {
         while(!this.packetQueue.isEmpty()) {
            class_2596<?> polled = (class_2596)this.packetQueue.poll();
            if (polled == null) {
               break;
            }

            this.packetListener.method_52413(polled);
         }

      }
   }
}
