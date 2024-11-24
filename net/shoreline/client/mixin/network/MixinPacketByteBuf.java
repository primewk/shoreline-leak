package net.shoreline.client.mixin.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import io.netty.handler.codec.DecoderException;
import net.minecraft.class_156;
import net.minecraft.class_2505;
import net.minecraft.class_2520;
import net.minecraft.class_2540;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_2540.class})
public abstract class MixinPacketByteBuf {
   @Shadow
   @Nullable
   public abstract class_2520 method_30616(class_2505 var1);

   @Inject(
      method = {"decode(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Lnet/minecraft/nbt/NbtSizeTracker;)Ljava/lang/Object;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookDecode(DynamicOps<class_2520> ops, Codec<Object> codec, class_2505 sizeTracker, CallbackInfoReturnable<Object> cir) {
      cir.cancel();

      try {
         class_2520 nbtElement = this.method_30616(sizeTracker);
         cir.setReturnValue(class_156.method_47526(codec.parse(ops, nbtElement), (error) -> {
            return new DecoderException("Failed to decode: " + error + " " + nbtElement);
         }));
      } catch (DecoderException var6) {
         cir.setReturnValue((Object)null);
      }

   }
}
