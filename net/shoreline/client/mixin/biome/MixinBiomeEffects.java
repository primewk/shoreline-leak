package net.shoreline.client.mixin.biome;

import java.util.Optional;
import net.minecraft.class_4761;
import net.minecraft.class_4763;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.biome.BiomeEffectsEvent;
import net.shoreline.client.impl.event.world.SkyboxEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_4763.class})
public class MixinBiomeEffects {
   @Inject(
      method = {"getSkyColor"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetSkyColor(CallbackInfoReturnable<Integer> cir) {
      SkyboxEvent.Sky skyboxEvent = new SkyboxEvent.Sky();
      Shoreline.EVENT_HANDLER.dispatch(skyboxEvent);
      if (skyboxEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(skyboxEvent.getRGB());
      }

   }

   @Inject(
      method = {"getParticleConfig"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetParticleConfig(CallbackInfoReturnable<Optional<class_4761>> cir) {
      BiomeEffectsEvent biomeEffectsEvent = new BiomeEffectsEvent();
      Shoreline.EVENT_HANDLER.dispatch(biomeEffectsEvent);
      if (biomeEffectsEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(Optional.ofNullable(biomeEffectsEvent.getParticleConfig()));
      }

   }
}
