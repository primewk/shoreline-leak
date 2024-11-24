package net.shoreline.client.mixin.render.entity;

import java.util.List;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1921;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_583;
import net.minecraft.class_922;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.render.entity.RenderEntityEvent;
import net.shoreline.client.impl.event.render.entity.RenderEntityInvisibleEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_922.class})
public abstract class MixinLivingEntityRenderer<T extends class_1309, M extends class_583<T>> {
   @Shadow
   protected M field_4737;
   @Shadow
   @Final
   protected List<class_3887<T, M>> field_4738;

   @Shadow
   protected abstract class_1921 method_24302(T var1, boolean var2, boolean var3, boolean var4);

   @Inject(
      method = {"render*"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookRender(class_1309 livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      RenderEntityEvent renderEntityEvent = new RenderEntityEvent(livingEntity, f, g, matrixStack, vertexConsumerProvider, i, this.field_4737, this.method_24302(livingEntity, true, false, false), this.field_4738);
      Shoreline.EVENT_HANDLER.dispatch(renderEntityEvent);
      if (renderEntityEvent.isCanceled()) {
         ci.cancel();
      }

   }

   @Redirect(
      method = {"render*"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/LivingEntity;isInvisibleTo(Lnet/minecraft/entity/player/PlayerEntity;)Z"
)
   )
   private boolean redirectRender$isInvisibleTo(class_1309 entity, class_1657 player) {
      RenderEntityInvisibleEvent event = new RenderEntityInvisibleEvent(entity);
      Shoreline.EVENT_HANDLER.dispatch(event);
      return event.isCanceled() ? false : entity.method_5756(player);
   }
}
