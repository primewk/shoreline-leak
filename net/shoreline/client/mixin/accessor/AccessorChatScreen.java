package net.shoreline.client.mixin.accessor;

import net.minecraft.class_342;
import net.minecraft.class_408;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_408.class})
public interface AccessorChatScreen {
   @Accessor("chatField")
   class_342 getChatField();
}
