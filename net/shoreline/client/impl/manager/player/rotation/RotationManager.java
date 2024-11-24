package net.shoreline.client.impl.manager.player.rotation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2828.class_2831;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.render.Interpolation;
import net.shoreline.client.impl.event.entity.UpdateVelocityEvent;
import net.shoreline.client.impl.event.entity.player.PlayerJumpEvent;
import net.shoreline.client.impl.event.keyboard.KeyboardTickEvent;
import net.shoreline.client.impl.event.network.MovementPacketsEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.impl.event.render.entity.RenderPlayerEvent;
import net.shoreline.client.impl.imixin.IClientPlayerEntity;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.Globals;

public class RotationManager implements Globals {
   private final List<Rotation> requests = new ArrayList();
   private float serverYaw;
   private float serverPitch;
   private float lastServerYaw;
   private float lastServerPitch;
   private float prevJumpYaw;
   private float prevYaw;
   private float prevPitch;
   boolean rotate;
   private Rotation rotation;
   private int rotateTicks;

   public RotationManager() {
      Shoreline.EVENT_HANDLER.subscribe(this);
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2828) {
            class_2828 packet = (class_2828)var3;
            if (packet.method_36172()) {
               float packetYaw = packet.method_12271(0.0F);
               float packetPitch = packet.method_12270(0.0F);
               this.serverYaw = packetYaw;
               this.serverPitch = packetPitch;
            }
         }

      }
   }

   public void onUpdate() {
      if (this.requests.isEmpty()) {
         this.rotation = null;
      } else {
         Rotation request = this.getRotationRequest();
         if (request == null) {
            if (this.isDoneRotating()) {
               this.rotation = null;
               return;
            }
         } else {
            this.rotation = request;
         }

         if (this.rotation != null) {
            this.rotateTicks = 0;
            this.rotate = true;
         }
      }
   }

   @EventListener
   public void onMovementPackets(MovementPacketsEvent event) {
      if (this.rotation != null) {
         if (this.rotate) {
            this.removeRotation(this.rotation);
            event.cancel();
            event.setYaw(this.rotation.getYaw());
            event.setPitch(this.rotation.getPitch());
            this.rotate = false;
         }

         if (this.rotation.isSnap()) {
            this.rotation = null;
         }
      }

   }

   @EventListener
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (event.getStage() == EventStage.POST) {
         this.lastServerYaw = ((IClientPlayerEntity)mc.field_1724).getLastSpoofedYaw();
         this.lastServerPitch = ((IClientPlayerEntity)mc.field_1724).getLastSpoofedPitch();
      }

   }

   @EventListener
   public void onKeyboardTick(KeyboardTickEvent event) {
      if (this.rotation != null && mc.field_1724 != null && Modules.ROTATIONS.getMovementFix()) {
         float forward = mc.field_1724.field_3913.field_3905;
         float sideways = mc.field_1724.field_3913.field_3907;
         float delta = (mc.field_1724.method_36454() - this.rotation.getYaw()) * 0.017453292F;
         float cos = class_3532.method_15362(delta);
         float sin = class_3532.method_15374(delta);
         mc.field_1724.field_3913.field_3907 = (float)Math.round(sideways * cos - forward * sin);
         mc.field_1724.field_3913.field_3905 = (float)Math.round(forward * cos + sideways * sin);
      }

   }

   @EventListener
   public void onUpdateVelocity(UpdateVelocityEvent event) {
      if (this.rotation != null && Modules.ROTATIONS.getMovementFix()) {
         event.cancel();
         event.setVelocity(this.movementInputToVelocity(this.rotation.getYaw(), event.getMovementInput(), event.getSpeed()));
      }

   }

   @EventListener
   public void onPlayerJump(PlayerJumpEvent event) {
      if (this.rotation != null && Modules.ROTATIONS.getMovementFix()) {
         if (event.getStage() == EventStage.PRE) {
            this.prevJumpYaw = mc.field_1724.method_36454();
            mc.field_1724.method_36456(this.rotation.getYaw());
         } else {
            mc.field_1724.method_36456(this.prevJumpYaw);
         }
      }

   }

   @EventListener
   public void onRenderPlayer(RenderPlayerEvent event) {
      if (event.getEntity() == mc.field_1724 && this.rotation != null) {
         event.setYaw(Interpolation.interpolateFloat(this.prevYaw, this.getServerYaw(), mc.method_1488()));
         event.setPitch(Interpolation.interpolateFloat(this.prevPitch, this.getServerPitch(), mc.method_1488()));
         this.prevYaw = event.getYaw();
         this.prevPitch = event.getPitch();
         event.cancel();
      }

   }

   public void setRotation(Rotation rotation) {
      if (rotation.getPriority() == Integer.MAX_VALUE) {
         this.rotation = rotation;
      }

      Rotation request = (Rotation)this.requests.stream().filter((r) -> {
         return rotation.getPriority() == r.getPriority();
      }).findFirst().orElse((Object)null);
      if (request == null) {
         this.requests.add(rotation);
      } else {
         request.setYaw(rotation.getYaw());
         request.setPitch(rotation.getPitch());
      }

   }

   public void setRotationClient(float yaw, float pitch) {
      if (mc.field_1724 != null) {
         mc.field_1724.method_36456(yaw);
         mc.field_1724.method_36457(pitch);
      }
   }

   public void setRotationSilent(float yaw, float pitch, boolean grim) {
      if (grim) {
         this.setRotation(new Rotation(Integer.MAX_VALUE, yaw, pitch, true));
         Managers.NETWORK.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), yaw, pitch, mc.field_1724.method_24828()));
      } else {
         Managers.NETWORK.sendPacket(new class_2831(yaw, pitch, mc.field_1724.method_24828()));
      }

   }

   public void setRotationSilentSync(boolean grim) {
      float yaw = mc.field_1724.method_36454();
      float pitch = mc.field_1724.method_36455();
      if (grim) {
         this.setRotation(new Rotation(Integer.MAX_VALUE, yaw, pitch, true));
         Managers.NETWORK.sendPacket(new class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), yaw, pitch, mc.field_1724.method_24828()));
      } else {
         Managers.NETWORK.sendPacket(new class_2831(yaw, pitch, mc.field_1724.method_24828()));
      }

   }

   public boolean removeRotation(Rotation request) {
      return this.requests.remove(request);
   }

   public boolean isRotationBlocked(int priority) {
      return this.rotation != null && priority < this.rotation.getPriority();
   }

   public boolean isDoneRotating() {
      return (float)this.rotateTicks > Modules.ROTATIONS.getPreserveTicks();
   }

   public boolean isRotating() {
      return this.rotation != null;
   }

   public float getRotationYaw() {
      return this.rotation.getYaw();
   }

   public float getRotationPitch() {
      return this.rotation.getPitch();
   }

   public float getServerYaw() {
      return this.serverYaw;
   }

   public float getWrappedYaw() {
      return class_3532.method_15393(this.serverYaw);
   }

   public float getServerPitch() {
      return this.serverPitch;
   }

   private class_243 movementInputToVelocity(float yaw, class_243 movementInput, float speed) {
      double d = movementInput.method_1027();
      if (d < 1.0E-7D) {
         return class_243.field_1353;
      } else {
         class_243 vec3d = (d > 1.0D ? movementInput.method_1029() : movementInput).method_1021((double)speed);
         float f = class_3532.method_15374(yaw * 0.017453292F);
         float g = class_3532.method_15362(yaw * 0.017453292F);
         return new class_243(vec3d.field_1352 * (double)g - vec3d.field_1350 * (double)f, vec3d.field_1351, vec3d.field_1350 * (double)g + vec3d.field_1352 * (double)f);
      }
   }

   private Rotation getRotationRequest() {
      Rotation rotationRequest = null;
      int priority = 0;
      Iterator var3 = this.requests.iterator();

      while(var3.hasNext()) {
         Rotation request = (Rotation)var3.next();
         if (request.getPriority() > priority) {
            rotationRequest = request;
            priority = request.getPriority();
         }
      }

      return rotationRequest;
   }
}
