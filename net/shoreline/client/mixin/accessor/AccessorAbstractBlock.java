package net.shoreline.client.mixin.accessor;

import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_4970.class})
public interface AccessorAbstractBlock {
   @Accessor("slipperiness")
   @Mutable
   void setSlipperiness(float var1);
}
