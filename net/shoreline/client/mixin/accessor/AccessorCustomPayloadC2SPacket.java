package net.shoreline.client.mixin.accessor;

import net.minecraft.class_2817;
import net.minecraft.class_8710;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2817.class})
public interface AccessorCustomPayloadC2SPacket {
   @Accessor("payload")
   @Mutable
   void hookSetData(class_8710 var1);
}
