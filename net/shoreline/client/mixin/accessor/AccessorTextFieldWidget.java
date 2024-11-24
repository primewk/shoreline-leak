package net.shoreline.client.mixin.accessor;

import net.minecraft.class_342;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_342.class})
public interface AccessorTextFieldWidget {
   @Accessor("drawsBackground")
   boolean isDrawsBackground();
}
