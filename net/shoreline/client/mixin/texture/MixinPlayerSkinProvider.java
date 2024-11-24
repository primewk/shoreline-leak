package net.shoreline.client.mixin.texture;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.class_1071.class_8687;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_8687.class})
public class MixinPlayerSkinProvider {
   @Shadow
   @Final
   private Type field_45641;
}
