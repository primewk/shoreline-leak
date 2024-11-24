package net.shoreline.client.mixin.accessor;

import net.minecraft.class_1309;
import net.minecraft.class_1671;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1671.class})
public interface AccessorFireworkRocketEntity {
   @Accessor("shooter")
   class_1309 getShooter();

   @Invoker("wasShotByEntity")
   boolean hookWasShotByEntity();

   @Invoker("explodeAndRemove")
   void hookExplodeAndRemove();
}
