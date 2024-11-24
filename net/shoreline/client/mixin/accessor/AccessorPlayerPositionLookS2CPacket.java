package net.shoreline.client.mixin.accessor;

import net.minecraft.class_2708;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2708.class})
public interface AccessorPlayerPositionLookS2CPacket {
   @Accessor("yaw")
   @Mutable
   void setYaw(float var1);

   @Accessor("pitch")
   @Mutable
   void setPitch(float var1);
}
