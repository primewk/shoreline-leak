package net.shoreline.client.mixin.accessor;

import net.minecraft.class_4668;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_4668.class})
public interface AccessorRenderPhase {
   @Mutable
   @Accessor("beginAction")
   void hookSetBeginAction(Runnable var1);

   @Mutable
   @Accessor("endAction")
   void hookSetEndAction(Runnable var1);
}
