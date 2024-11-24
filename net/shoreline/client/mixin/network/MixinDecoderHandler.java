package net.shoreline.client.mixin.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import net.minecraft.class_2543;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.network.DecodePacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2543.class})
public class MixinDecoderHandler {
   @Inject(
      method = {"decode"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/network/NetworkState;getId()Ljava/lang/String;",
   shift = Shift.AFTER
)},
      cancellable = true
   )
   private void hookDecode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> objects, CallbackInfo ci) {
      DecodePacketEvent decodePacketEvent = new DecodePacketEvent();
      Shoreline.EVENT_HANDLER.dispatch(decodePacketEvent);
      if (decodePacketEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
