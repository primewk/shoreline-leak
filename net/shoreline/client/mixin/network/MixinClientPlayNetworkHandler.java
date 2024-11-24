package net.shoreline.client.mixin.network;

import net.minecraft.class_2535;
import net.minecraft.class_2596;
import net.minecraft.class_2649;
import net.minecraft.class_2678;
import net.minecraft.class_634;
import net.minecraft.class_7648;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.gui.chat.ChatMessageEvent;
import net.shoreline.client.impl.event.network.GameJoinEvent;
import net.shoreline.client.impl.event.network.InventoryEvent;
import net.shoreline.client.impl.imixin.IClientPlayNetworkHandler;
import net.shoreline.client.mixin.accessor.AccessorClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_634.class})
public abstract class MixinClientPlayNetworkHandler implements IClientPlayNetworkHandler {
   @Shadow
   public abstract class_2535 method_48296();

   @Inject(
      method = {"sendChatMessage"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookSendChatMessage(String content, CallbackInfo ci) {
      ChatMessageEvent.Server chatInputEvent = new ChatMessageEvent.Server(content);
      Shoreline.EVENT_HANDLER.dispatch(chatInputEvent);
      if (chatInputEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"onGameJoin"},
      at = {@At("TAIL")}
   )
   private void hookOnGameJoin(class_2678 packet, CallbackInfo ci) {
      GameJoinEvent gameJoinEvent = new GameJoinEvent();
      Shoreline.EVENT_HANDLER.dispatch(gameJoinEvent);
   }

   @Inject(
      method = {"onInventory"},
      at = {@At("TAIL")}
   )
   private void hookOnInventory(class_2649 packet, CallbackInfo ci) {
      InventoryEvent inventoryEvent = new InventoryEvent(packet);
      Shoreline.EVENT_HANDLER.dispatch(inventoryEvent);
   }

   public void sendQuietPacket(class_2596<?> packet) {
      ((AccessorClientConnection)this.method_48296()).hookSendInternal(packet, (class_7648)null, true);
   }
}
