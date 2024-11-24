package net.shoreline.client.mixin.network;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_7648;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.init.Modules;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2535.class})
public class MixinClientConnection {
   @Shadow
   @Nullable
   private volatile class_2547 field_11652;
   @Shadow
   @Final
   private static Logger field_11642;

   @Inject(
      method = {"exceptionCaught"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookExceptionCaught(ChannelHandlerContext context, Throwable ex, CallbackInfo ci) {
      if (Modules.SERVER.isPacketKick()) {
         field_11642.error("Exception caught on network thread:", ex);
         ci.cancel();
      }

   }

   @Inject(
      method = {"sendImmediately"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookSendImmediately(class_2596<?> packet, @Nullable class_7648 callbacks, boolean flush, CallbackInfo ci) {
      PacketEvent.Outbound packetOutboundEvent = new PacketEvent.Outbound(packet);
      Shoreline.EVENT_HANDLER.dispatch(packetOutboundEvent);
      if (packetOutboundEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookChannelRead0(ChannelHandlerContext channelHandlerContext, class_2596<?> packet, CallbackInfo ci) {
      PacketEvent.Inbound packetInboundEvent = new PacketEvent.Inbound(this.field_11652, packet);
      Shoreline.EVENT_HANDLER.dispatch(packetInboundEvent);
      if (packetInboundEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"disconnect"},
      at = {@At("HEAD")}
   )
   private void hookDisconnect(class_2561 disconnectReason, CallbackInfo ci) {
      DisconnectEvent disconnectEvent = new DisconnectEvent();
      Shoreline.EVENT_HANDLER.dispatch(disconnectEvent);
   }
}
