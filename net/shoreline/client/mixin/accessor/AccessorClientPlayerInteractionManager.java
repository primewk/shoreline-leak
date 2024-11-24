package net.shoreline.client.mixin.accessor;

import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_636.class})
public interface AccessorClientPlayerInteractionManager {
   @Invoker("syncSelectedSlot")
   void hookSyncSelectedSlot();

   @Accessor("currentBreakingProgress")
   float hookGetCurrentBreakingProgress();

   @Accessor("currentBreakingProgress")
   void hookSetCurrentBreakingProgress(float var1);
}
