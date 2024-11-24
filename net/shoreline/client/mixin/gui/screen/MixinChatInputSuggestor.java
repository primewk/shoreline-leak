package net.shoreline.client.mixin.gui.screen;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_2172;
import net.minecraft.class_342;
import net.minecraft.class_4717;
import net.minecraft.class_4717.class_464;
import net.shoreline.client.init.Managers;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({class_4717.class})
public abstract class MixinChatInputSuggestor {
   @Shadow
   private ParseResults<class_2172> field_21610;
   @Shadow
   @Final
   private class_342 field_21599;
   @Shadow
   private boolean field_21614;
   @Shadow
   @Nullable
   private class_464 field_21612;
   @Shadow
   @Nullable
   private CompletableFuture<Suggestions> field_21611;

   @Shadow
   protected abstract void method_23937();

   @Inject(
      method = {"refresh"},
      at = {@At(
   value = "INVOKE",
   target = "Lcom/mojang/brigadier/StringReader;canRead()Z",
   remap = false
)},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void hookRefresh(CallbackInfo ci, String string, StringReader stringReader) {
      if (stringReader.getString().startsWith(Managers.COMMAND.getPrefix(), stringReader.getCursor())) {
         stringReader.setCursor(stringReader.getCursor() + 1);
         if (this.field_21610 == null) {
            this.field_21610 = Managers.COMMAND.getDispatcher().parse(stringReader, Managers.COMMAND.getSource());
         }

         int cursor = this.field_21599.method_1881();
         if (cursor >= 1 && (this.field_21612 == null || !this.field_21614)) {
            this.field_21611 = Managers.COMMAND.getDispatcher().getCompletionSuggestions(this.field_21610, cursor);
            this.field_21611.thenRun(() -> {
               if (this.field_21611.isDone()) {
                  this.method_23937();
               }

            });
         }

         ci.cancel();
      }

   }
}
