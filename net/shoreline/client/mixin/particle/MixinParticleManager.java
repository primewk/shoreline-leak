package net.shoreline.client.mixin.particle;

import net.minecraft.class_1297;
import net.minecraft.class_2394;
import net.minecraft.class_702;
import net.minecraft.class_703;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.particle.ParticleEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_702.class})
public class MixinParticleManager {
   @Inject(
      method = {"addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookAddParticle(class_2394 parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<class_703> cir) {
      ParticleEvent particleEvent = new ParticleEvent(parameters);
      Shoreline.EVENT_HANDLER.dispatch(particleEvent);
      if (particleEvent.isCanceled()) {
         cir.setReturnValue((Object)null);
         cir.cancel();
      }

   }

   @Inject(
      method = {"addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookAddEmitter(class_1297 entity, class_2394 parameters, int maxAge, CallbackInfo ci) {
      ParticleEvent.Emitter particleEvent = new ParticleEvent.Emitter(parameters);
      Shoreline.EVENT_HANDLER.dispatch(particleEvent);
      if (particleEvent.isCanceled()) {
         ci.cancel();
      }

   }
}
