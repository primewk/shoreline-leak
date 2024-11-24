package net.shoreline.client.mixin.accessor;

import net.minecraft.class_2828;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2828.class})
public interface AccessorPlayerMoveC2SPacket {
   @Accessor("onGround")
   @Mutable
   void hookSetOnGround(boolean var1);

   @Accessor("x")
   @Mutable
   void hookSetX(double var1);

   @Accessor("y")
   @Mutable
   void hookSetY(double var1);

   @Accessor("z")
   @Mutable
   void hookSetZ(double var1);

   @Accessor("yaw")
   @Mutable
   void hookSetYaw(float var1);

   @Accessor("pitch")
   @Mutable
   void hookSetPitch(float var1);
}
