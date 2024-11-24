package net.shoreline.client.mixin.gui.screen;

import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_408;
import net.minecraft.class_6379;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.gui.chat.ChatInputEvent;
import net.shoreline.client.impl.event.gui.chat.ChatKeyInputEvent;
import net.shoreline.client.impl.event.gui.chat.ChatMessageEvent;
import net.shoreline.client.impl.event.gui.chat.ChatRenderEvent;
import net.shoreline.client.init.Modules;
import net.shoreline.client.mixin.accessor.AccessorTextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_408.class})
public class MixinChatScreen extends MixinScreen {
   @Shadow
   protected class_342 field_2382;

   @Inject(
      method = {"onChatFieldUpdate"},
      at = {@At("TAIL")}
   )
   private void hookOnChatFieldUpdate(String chatText, CallbackInfo ci) {
      ChatInputEvent chatInputEvent = new ChatInputEvent(chatText);
      Shoreline.EVENT_HANDLER.dispatch(chatInputEvent);
   }

   @Inject(
      method = {"keyPressed"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
      ChatKeyInputEvent keyInputEvent = new ChatKeyInputEvent(keyCode, this.field_2382.method_1882());
      Shoreline.EVENT_HANDLER.dispatch(keyInputEvent);
      if (keyInputEvent.isCanceled()) {
         cir.cancel();
         this.field_2382.method_1852(keyInputEvent.getChatText());
      }

   }

   @Inject(
      method = {"sendMessage"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookSendMessage(String chatText, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
      ChatMessageEvent.Client chatMessageEvent = new ChatMessageEvent.Client(chatText);
      Shoreline.EVENT_HANDLER.dispatch(chatMessageEvent);
      if (chatMessageEvent.isCanceled()) {
         cir.setReturnValue(true);
         cir.cancel();
      }

   }

   @Inject(
      method = {"render"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V",
   shift = Shift.BEFORE
)}
   )
   private void hookRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      float x = ((AccessorTextFieldWidget)this.field_2382).isDrawsBackground() ? (float)(this.field_2382.method_46426() + 6) : (float)(this.field_2382.method_46426() + 2);
      float y = ((AccessorTextFieldWidget)this.field_2382).isDrawsBackground() ? (float)this.field_2382.method_46427() + (float)(this.field_2382.method_25364() - 8) / 2.0F : (float)this.field_2382.method_46427();
      ChatRenderEvent chatTextRenderEvent = new ChatRenderEvent(context, x, y);
      Shoreline.EVENT_HANDLER.dispatch(chatTextRenderEvent);
   }

   @Redirect(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"
)
   )
   private void hookFill(class_332 instance, int x1, int y1, int x2, int y2, int color) {
      float openAnimation = Modules.HUD.isEnabled() ? 12.0F * Modules.HUD.getChatAnimation() : 12.0F;
      RenderManager.rect(instance.method_51448(), 2.0D, (double)((float)this.field_22790 - 2.0F), (double)(this.field_22789 - 4), (double)(-openAnimation), this.field_22787.field_1690.method_19344(Integer.MIN_VALUE));
   }

   protected <T extends class_364 & class_4068 & class_6379> T method_37063(T drawableElement) {
      return null;
   }
}
