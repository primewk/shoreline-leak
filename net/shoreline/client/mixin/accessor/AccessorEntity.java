package net.shoreline.client.mixin.accessor;

import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1297.class})
public interface AccessorEntity {
   @Invoker("unsetRemoved")
   void hookUnsetRemoved();

   @Invoker("setFlag")
   void hookSetFlag(int var1, boolean var2);
}
