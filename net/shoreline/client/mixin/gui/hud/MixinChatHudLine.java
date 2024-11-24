package net.shoreline.client.mixin.gui.hud;

import net.minecraft.class_2561;
import net.minecraft.class_303;
import net.minecraft.class_7469;
import net.minecraft.class_7591;
import net.shoreline.client.impl.imixin.IChatHudLine;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.Globals;
import net.shoreline.client.util.render.animation.TimeAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_303.class})
public abstract class MixinChatHudLine implements IChatHudLine, Globals {
   @Unique
   private int id;

   @Inject(
      method = {"<init>"},
      at = {@At("RETURN")}
   )
   private void hookCtr(int creationTick, class_2561 text, class_7469 messageSignatureData, class_7591 messageIndicator, CallbackInfo info) {
      Modules.BETTER_CHAT.animationMap.put((class_303)class_303.class.cast(this), new TimeAnimation(false, (double)(-mc.field_1772.method_1727(text.getString())), 0.0D, (float)(Integer)Modules.BETTER_CHAT.getTimeConfig().getValue(), Modules.BETTER_CHAT.getEasingConfig()));
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }
}
