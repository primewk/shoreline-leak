package net.shoreline.client.mixin.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.class_2960;
import net.minecraft.class_640;
import net.minecraft.class_8685;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_640.class})
public class MixinPlayerListEntry implements Globals {
   @Unique
   private class_2960 capeTexture;
   @Unique
   private boolean capeTextureLoaded;

   @Inject(
      method = {"<init>(Lcom/mojang/authlib/GameProfile;Z)V"},
      at = {@At("TAIL")}
   )
   private void hookInit(GameProfile profile, boolean secureChatEnforced, CallbackInfo ci) {
      if (!this.capeTextureLoaded) {
         Managers.CAPES.loadPlayerCape(profile, (identifier) -> {
            this.capeTexture = identifier;
         });
         this.capeTextureLoaded = true;
      }
   }

   @Inject(
      method = {"getSkinTextures"},
      at = {@At("TAIL")},
      cancellable = true
   )
   private void hookGetSkinTextures(CallbackInfoReturnable<class_8685> cir) {
      if (this.capeTexture != null && Modules.CAPES.isEnabled() && (Boolean)Modules.CAPES.getOptifineConfig().getValue()) {
         class_8685 t = (class_8685)cir.getReturnValue();
         class_8685 customCapeTexture = new class_8685(t.comp_1626(), t.comp_1911(), this.capeTexture, this.capeTexture, t.comp_1629(), t.comp_1630());
         cir.setReturnValue(customCapeTexture);
      }

   }
}
