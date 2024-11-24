package net.shoreline.client.mixin.accessor;

import net.minecraft.class_2664;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2664.class})
public interface AccessorExplosionS2CPacket {
   @Accessor("playerVelocityX")
   @Mutable
   void setPlayerVelocityX(float var1);

   @Accessor("playerVelocityY")
   @Mutable
   void setPlayerVelocityY(float var1);

   @Accessor("playerVelocityZ")
   @Mutable
   void setPlayerVelocityZ(float var1);
}
