package net.shoreline.client.mixin;

import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_636;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_310.class_8764;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.impl.event.AttackCooldownEvent;
import net.shoreline.client.impl.event.EntityOutlineEvent;
import net.shoreline.client.impl.event.FinishLoadingEvent;
import net.shoreline.client.impl.event.FramerateLimitEvent;
import net.shoreline.client.impl.event.ItemMultitaskEvent;
import net.shoreline.client.impl.event.RunTickEvent;
import net.shoreline.client.impl.event.ScreenOpenEvent;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.entity.EntityDeathEvent;
import net.shoreline.client.impl.imixin.IMinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_310.class})
public abstract class MixinMinecraftClient implements IMinecraftClient {
   @Shadow
   public class_638 field_1687;
   @Shadow
   public class_746 field_1724;
   @Shadow
   @Nullable
   public class_636 field_1761;
   @Shadow
   protected int field_1771;
   @Unique
   private boolean leftClick;
   @Unique
   private boolean rightClick;
   @Unique
   private boolean doAttackCalled;
   @Unique
   private boolean doItemUseCalled;

   @Shadow
   protected abstract void method_1583();

   @Shadow
   protected abstract boolean method_1536();

   public void leftClick() {
      this.leftClick = true;
   }

   public void rightClick() {
      this.rightClick = true;
   }

   @Inject(
      method = {"run"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/MinecraftClient;render(Z)V",
   shift = Shift.BEFORE
)}
   )
   private void hookRun(CallbackInfo ci) {
      RunTickEvent runTickEvent = new RunTickEvent();
      Shoreline.EVENT_HANDLER.dispatch(runTickEvent);
   }

   @Inject(
      method = {"onInitFinished"},
      at = {@At("RETURN")}
   )
   private void hookOnInitFinished(class_8764 loadingContext, CallbackInfoReturnable<Runnable> cir) {
      FinishLoadingEvent finishLoadingEvent = new FinishLoadingEvent();
      Shoreline.EVENT_HANDLER.dispatch(finishLoadingEvent);
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   private void hookTickPre(CallbackInfo ci) {
      this.doAttackCalled = false;
      this.doItemUseCalled = false;
      if (this.field_1724 != null && this.field_1687 != null) {
         TickEvent tickPreEvent = new TickEvent();
         tickPreEvent.setStage(EventStage.PRE);
         Shoreline.EVENT_HANDLER.dispatch(tickPreEvent);
      }

      if (this.field_1761 != null) {
         if (this.leftClick && !this.doAttackCalled) {
            this.method_1536();
         }

         if (this.rightClick && !this.doItemUseCalled) {
            this.method_1583();
         }

         this.leftClick = false;
         this.rightClick = false;
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("TAIL")}
   )
   private void hookTickPost(CallbackInfo ci) {
      if (this.field_1724 != null && this.field_1687 != null) {
         TickEvent tickPostEvent = new TickEvent();
         tickPostEvent.setStage(EventStage.POST);
         Shoreline.EVENT_HANDLER.dispatch(tickPostEvent);
         this.field_1687.method_18112().forEach((entity) -> {
            if (entity instanceof class_1309) {
               class_1309 e = (class_1309)entity;
               if (e.method_29504()) {
                  EntityDeathEvent entityDeathEvent = new EntityDeathEvent(e);
                  Shoreline.EVENT_HANDLER.dispatch(entityDeathEvent);
               }
            }

         });
      }

   }

   @Inject(
      method = {"setScreen"},
      at = {@At("TAIL")}
   )
   private void hookSetScreen(class_437 screen, CallbackInfo ci) {
      ScreenOpenEvent screenOpenEvent = new ScreenOpenEvent(screen);
      Shoreline.EVENT_HANDLER.dispatch(screenOpenEvent);
   }

   @Inject(
      method = {"doItemUse"},
      at = {@At("HEAD")}
   )
   private void hookDoItemUse(CallbackInfo ci) {
      this.doItemUseCalled = true;
   }

   @Inject(
      method = {"doAttack"},
      at = {@At("HEAD")}
   )
   private void hookDoAttack(CallbackInfoReturnable<Boolean> cir) {
      this.doAttackCalled = true;
      AttackCooldownEvent attackCooldownEvent = new AttackCooldownEvent();
      Shoreline.EVENT_HANDLER.dispatch(attackCooldownEvent);
      if (attackCooldownEvent.isCanceled()) {
         this.field_1771 = 0;
      }

   }

   @Redirect(
      method = {"handleBlockBreaking"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
)
   )
   private boolean hookIsUsingItem(class_746 instance) {
      ItemMultitaskEvent itemMultitaskEvent = new ItemMultitaskEvent();
      Shoreline.EVENT_HANDLER.dispatch(itemMultitaskEvent);
      return !itemMultitaskEvent.isCanceled() && instance.method_6115();
   }

   @Redirect(
      method = {"doItemUse"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"
)
   )
   private boolean hookIsBreakingBlock(class_636 instance) {
      ItemMultitaskEvent itemMultitaskEvent = new ItemMultitaskEvent();
      Shoreline.EVENT_HANDLER.dispatch(itemMultitaskEvent);
      return !itemMultitaskEvent.isCanceled() && instance.method_2923();
   }

   @Inject(
      method = {"getFramerateLimit"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetFramerateLimit(CallbackInfoReturnable<Integer> cir) {
      FramerateLimitEvent framerateLimitEvent = new FramerateLimitEvent();
      Shoreline.EVENT_HANDLER.dispatch(framerateLimitEvent);
      if (framerateLimitEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(framerateLimitEvent.getFramerateLimit());
      }

   }

   @Inject(
      method = {"hasOutline"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookHasOutline(class_1297 entity, CallbackInfoReturnable<Boolean> cir) {
      EntityOutlineEvent entityOutlineEvent = new EntityOutlineEvent(entity);
      Shoreline.EVENT_HANDLER.dispatch(entityOutlineEvent);
      if (entityOutlineEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(true);
      }

   }
}
