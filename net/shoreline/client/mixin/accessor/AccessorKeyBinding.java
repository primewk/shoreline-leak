package net.shoreline.client.mixin.accessor;

import net.minecraft.class_304;
import net.minecraft.class_3675.class_306;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_304.class})
public interface AccessorKeyBinding {
   @Accessor("boundKey")
   class_306 getBoundKey();
}
