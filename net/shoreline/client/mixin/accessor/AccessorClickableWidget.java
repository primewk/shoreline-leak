package net.shoreline.client.mixin.accessor;

import net.minecraft.class_2561;
import net.minecraft.class_339;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_339.class})
public interface AccessorClickableWidget {
   @Accessor("message")
   void setMessage(class_2561 var1);
}
