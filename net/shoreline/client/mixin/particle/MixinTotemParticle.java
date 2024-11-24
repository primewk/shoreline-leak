package net.shoreline.client.mixin.particle;

import java.awt.Color;
import net.minecraft.class_4002;
import net.minecraft.class_638;
import net.minecraft.class_734;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.particle.TotemParticleEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_734.class})
public abstract class MixinTotemParticle extends MixinParticle {
   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void hookInit(class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, class_4002 spriteProvider, CallbackInfo ci) {
      TotemParticleEvent totemParticleEvent = new TotemParticleEvent();
      Shoreline.EVENT_HANDLER.dispatch(totemParticleEvent);
      if (totemParticleEvent.isCanceled()) {
         Color color = totemParticleEvent.getColor();
         this.method_3084((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
      }

   }
}
