package net.shoreline.client.mixin.gui.hud;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import java.util.Comparator;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_268;
import net.minecraft.class_310;
import net.minecraft.class_355;
import net.minecraft.class_5250;
import net.minecraft.class_640;
import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.gui.hud.PlayerListColumnsEvent;
import net.shoreline.client.impl.event.gui.hud.PlayerListEvent;
import net.shoreline.client.impl.event.gui.hud.PlayerListNameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_355.class})
public abstract class MixinPlayerListHud {
   @Shadow
   @Final
   private static Comparator<class_640> field_2156;
   @Shadow
   @Final
   private class_310 field_2155;

   @Shadow
   protected abstract List<class_640> method_48213();

   @Shadow
   protected abstract class_2561 method_27538(class_640 var1, class_5250 var2);

   @Inject(
      method = {"getPlayerName"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetPlayerName(class_640 entry, CallbackInfoReturnable<class_2561> cir) {
      class_2561 text;
      if (entry.method_2971() != null) {
         text = this.method_27538(entry, entry.method_2971().method_27661());
      } else {
         text = this.method_27538(entry, class_268.method_1142(entry.method_2955(), class_2561.method_43470(entry.method_2966().getName())));
      }

      PlayerListNameEvent playerListNameEvent = new PlayerListNameEvent(text, entry.method_2966().getId());
      Shoreline.EVENT_HANDLER.dispatch(playerListNameEvent);
      if (playerListNameEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(playerListNameEvent.getPlayerName());
      }

   }

   @Inject(
      method = {"collectPlayerEntries"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookCollectPlayerEntries(CallbackInfoReturnable<List<class_640>> cir) {
      PlayerListEvent playerListEvent = new PlayerListEvent();
      Shoreline.EVENT_HANDLER.dispatch(playerListEvent);
      if (playerListEvent.isCanceled()) {
         cir.cancel();
         cir.setReturnValue(this.field_2155.field_1724.field_3944.method_45732().stream().sorted(field_2156).limit((long)playerListEvent.getSize()).toList());
      }

   }

   @Inject(
      method = {"render"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/lang/Math;min(II)I",
   shift = Shift.BEFORE
)}
   )
   private void hookRender(CallbackInfo ci, @Local(ordinal = 5) LocalIntRef o, @Local(ordinal = 6) LocalIntRef p) {
      int newP = 1;
      int newO;
      int totalPlayers = newO = this.method_48213().size();
      PlayerListColumnsEvent playerListColumsEvent = new PlayerListColumnsEvent();
      Shoreline.EVENT_HANDLER.dispatch(playerListColumsEvent);
      if (playerListColumsEvent.isCanceled()) {
         while(true) {
            if (newO <= playerListColumsEvent.getTabHeight()) {
               o.set(newO);
               p.set(newP);
               break;
            }

            ++newP;
            newO = (totalPlayers + newP - 1) / newP;
         }
      }

   }
}
