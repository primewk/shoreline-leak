package net.shoreline.client.mixin.accessor;

import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_765.class})
public interface AccessorLightmapTextureManager {
   @Accessor("dirty")
   void setUpdateLightmap(boolean var1);
}
