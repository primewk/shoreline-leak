package net.shoreline.client.mixin.accessor;

import net.minecraft.class_310;
import net.minecraft.class_320;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_310.class})
public interface AccessorMinecraftClient {
   @Accessor("itemUseCooldown")
   void hookSetItemUseCooldown(int var1);

   @Accessor("itemUseCooldown")
   int hookGetItemUseCooldown();

   @Accessor("attackCooldown")
   void hookSetAttackCooldown(int var1);

   @Accessor("session")
   @Final
   @Mutable
   void setSession(class_320 var1);
}
