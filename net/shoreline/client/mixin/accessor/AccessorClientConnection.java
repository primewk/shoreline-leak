package net.shoreline.client.mixin.accessor;

import net.minecraft.class_2535;
import net.minecraft.class_2596;
import net.minecraft.class_7648;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_2535.class})
public interface AccessorClientConnection {
   @Invoker("sendInternal")
   void hookSendInternal(class_2596<?> var1, @Nullable class_7648 var2, boolean var3);
}
