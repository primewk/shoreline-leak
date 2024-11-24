package net.shoreline.client.mixin.item;

import net.minecraft.class_1268;
import net.minecraft.class_1271;
import net.minecraft.class_1657;
import net.minecraft.class_1781;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.item.FireworkUseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1781.class})
public class MixinFireworkRocketItem {
   @Inject(
      method = {"use"},
      at = {@At("HEAD")}
   )
   private void hookUse(class_1937 world, class_1657 user, class_1268 hand, CallbackInfoReturnable<class_1271<class_1799>> cir) {
      FireworkUseEvent fireworkUseEvent = new FireworkUseEvent();
      Shoreline.EVENT_HANDLER.dispatch(fireworkUseEvent);
   }
}
