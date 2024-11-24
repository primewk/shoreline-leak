package net.shoreline.client.mixin.gui.screen;

import net.minecraft.class_310;
import net.minecraft.class_412;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.shoreline.client.init.Managers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_412.class})
public class MixinConnectScreen {
   @Inject(
      method = {"connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V"},
      at = {@At("HEAD")}
   )
   private void onConnect(class_310 client, class_639 address, class_642 info, CallbackInfo ci) {
      Managers.NETWORK.setAddress(address);
      Managers.NETWORK.setInfo(info);
   }
}
