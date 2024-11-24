package net.shoreline.client.mixin.particle;

import net.minecraft.class_703;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_703.class})
public abstract class MixinParticle {
   @Shadow
   public abstract void method_3084(float var1, float var2, float var3);
}
