package net.shoreline.client.mixin.accessor;

import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_746.class})
public interface AccessorClientPlayerEntity {
   @Accessor("lastX")
   double getLastX();

   @Accessor("lastBaseY")
   double getLastBaseY();

   @Accessor("lastZ")
   double getLastZ();
}
