package net.shoreline.client.mixin.accessor;

import net.minecraft.class_2743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2743.class})
public interface AccessorEntityVelocityUpdateS2CPacket {
   @Accessor("velocityX")
   @Mutable
   void setVelocityX(int var1);

   @Accessor("velocityY")
   @Mutable
   void setVelocityY(int var1);

   @Accessor("velocityZ")
   @Mutable
   void setVelocityZ(int var1);
}
