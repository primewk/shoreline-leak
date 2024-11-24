package net.shoreline.client.mixin.accessor;

import net.minecraft.class_8080;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_8080.class})
public interface AccessorLimbAnimator {
   @Accessor("pos")
   void hookSetPos(float var1);

   @Accessor("speed")
   void hookSetSpeed(float var1);
}
