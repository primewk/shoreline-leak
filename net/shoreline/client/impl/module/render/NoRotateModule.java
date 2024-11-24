package net.shoreline.client.impl.module.render;

import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2709;
import net.minecraft.class_434;
import net.minecraft.class_2828.class_2830;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.mixin.accessor.AccessorPlayerMoveC2SPacket;
import net.shoreline.client.mixin.accessor.AccessorPlayerPositionLookS2CPacket;

public class NoRotateModule extends ToggleModule {
   Config<Boolean> positionAdjustConfig = new BooleanConfig("PositionAdjust", "Adjusts outgoing rotation packets", false);
   private float yaw;
   private float pitch;
   private boolean cancelRotate;

   public NoRotateModule() {
      super("NoRotate", "Prevents server from forcing rotations", ModuleCategory.RENDER);
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1724 != null && !(mc.field_1755 instanceof class_434)) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2708) {
            class_2708 packet = (class_2708)var3;
            this.yaw = packet.method_11736();
            this.pitch = packet.method_11739();
            ((AccessorPlayerPositionLookS2CPacket)packet).setYaw(mc.field_1724.method_36454());
            ((AccessorPlayerPositionLookS2CPacket)packet).setPitch(mc.field_1724.method_36455());
            packet.method_11733().remove(class_2709.field_12397);
            packet.method_11733().remove(class_2709.field_12401);
            this.cancelRotate = true;
         }

      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2830) {
         class_2830 packet = (class_2830)var3;
         if (this.cancelRotate) {
            if ((Boolean)this.positionAdjustConfig.getValue()) {
               ((AccessorPlayerMoveC2SPacket)packet).hookSetYaw(this.yaw);
               ((AccessorPlayerMoveC2SPacket)packet).hookSetPitch(this.pitch);
            }

            this.cancelRotate = false;
         }
      }

   }
}
