package net.shoreline.client.mixin.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import net.minecraft.class_638;
import net.minecraft.class_758;
import net.minecraft.class_758.class_4596;
import net.minecraft.class_758.class_7286;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.RenderFogEvent;
import net.shoreline.client.impl.event.world.BlindnessEvent;
import net.shoreline.client.impl.event.world.SkyboxEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_758.class})
public class MixinBackgroundRenderer {
   @Shadow
   private static float field_4034;
   @Shadow
   private static float field_4033;
   @Shadow
   private static float field_4032;

   @Inject(
      method = {"applyFog"},
      at = {@At("TAIL")}
   )
   private static void hookApplyFog(class_4184 camera, class_4596 fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
      if (fogType == class_4596.field_20946) {
         RenderFogEvent renderFogEvent = new RenderFogEvent();
         Shoreline.EVENT_HANDLER.dispatch(renderFogEvent);
         if (renderFogEvent.isCanceled()) {
            RenderSystem.setShaderFogStart(viewDistance * 4.0F);
            RenderSystem.setShaderFogEnd(viewDistance * 4.25F);
         }

      }
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void hookRender(class_4184 camera, float tickDelta, class_638 world, int viewDistance, float skyDarkness, CallbackInfo ci) {
      SkyboxEvent.Fog skyboxEvent = new SkyboxEvent.Fog(tickDelta);
      Shoreline.EVENT_HANDLER.dispatch(skyboxEvent);
      if (skyboxEvent.isCanceled()) {
         ci.cancel();
         class_243 vec3d = skyboxEvent.getColorVec();
         field_4034 = (float)vec3d.field_1352;
         field_4033 = (float)vec3d.field_1351;
         field_4032 = (float)vec3d.field_1350;
         RenderSystem.clearColor(field_4034, field_4033, field_4032, 0.0F);
      }

   }

   @Inject(
      method = {"getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetFogModifier(class_1297 entity, float tickDelta, CallbackInfoReturnable<class_7286> cir) {
      BlindnessEvent blindnessEvent = new BlindnessEvent();
      Shoreline.EVENT_HANDLER.dispatch(blindnessEvent);
      if (blindnessEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue((Object)null);
      }

   }
}
