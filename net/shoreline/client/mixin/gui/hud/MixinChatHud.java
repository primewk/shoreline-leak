package net.shoreline.client.mixin.gui.hud;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_303;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_338;
import net.minecraft.class_5481;
import net.minecraft.class_7469;
import net.minecraft.class_7591;
import net.minecraft.class_303.class_7590;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.gui.hud.ChatMessageEvent;
import net.shoreline.client.impl.event.gui.hud.ChatTextEvent;
import net.shoreline.client.impl.imixin.IChatHud;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.render.animation.TimeAnimation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_338.class})
public abstract class MixinChatHud implements IChatHud {
   @Shadow
   @Final
   private List<class_303> field_2061;
   @Shadow
   @Final
   private List<class_7590> field_2064;
   private class_303 current = null;
   private int currentId;

   @Shadow
   public abstract void method_1812(class_2561 var1);

   @Inject(
      method = {"render"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;addedTime()I"
)}
   )
   private void hookTimeAdded(CallbackInfo ci, @Local(ordinal = 13) int chatLineIndex) {
      try {
         this.current = (class_303)this.field_2061.get(chatLineIndex);
      } catch (Exception var4) {
      }

   }

   @Redirect(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"
)
   )
   private int drawTextWithShadowHook(class_332 instance, class_327 textRenderer, class_5481 text, int x, int y, int color) {
      TimeAnimation animation = null;
      if (this.current != null && Modules.BETTER_CHAT.animationMap.containsKey(this.current)) {
         animation = (TimeAnimation)Modules.BETTER_CHAT.animationMap.get(this.current);
      }

      if (animation != null) {
         animation.setState(true);
      }

      ChatTextEvent chatTextEvent = new ChatTextEvent(text);
      Shoreline.EVENT_HANDLER.dispatch(chatTextEvent);
      return chatTextEvent.isCanceled() ? instance.method_35720(textRenderer, chatTextEvent.getText(), (int)(animation != null && Modules.BETTER_CHAT.isEnabled() && (Boolean)Modules.BETTER_CHAT.getAnimationConfig().getValue() ? animation.getCurrent() : 0.0D), y, color) : instance.method_35720(textRenderer, text, (int)(animation != null && Modules.BETTER_CHAT.isEnabled() && (Boolean)Modules.BETTER_CHAT.getAnimationConfig().getValue() ? animation.getCurrent() : 0.0D), y, color);
   }

   @ModifyExpressionValue(
      method = {"render"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;indicator()Lnet/minecraft/client/gui/hud/MessageIndicator;"
)}
   )
   private class_7591 hookRender(class_7591 original) {
      return (Boolean)Modules.BETTER_CHAT.getNoSignatureConfig().getValue() ? null : original;
   }

   @Inject(
      method = {"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V"},
      at = {@At("HEAD")}
   )
   private void hookAddMessage(class_2561 message, class_7469 signature, int ticks, class_7591 indicator, boolean refresh, CallbackInfo ci) {
      ChatMessageEvent chatMessageEvent = new ChatMessageEvent(message);
      Shoreline.EVENT_HANDLER.dispatch(chatMessageEvent);
   }

   public void addMessage(String message, int id) {
      this.currentId = id;
      this.method_1812(class_2561.method_30163(message));
      this.currentId = -1;
   }
}
