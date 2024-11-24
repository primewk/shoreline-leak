package net.shoreline.client.mixin.accessor;

import java.util.Optional;
import net.minecraft.class_1291;
import net.minecraft.class_2866;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2866.class})
public interface AccessorUpdateBeaconC2SPacket {
   @Accessor("primaryEffectId")
   @Mutable
   void setPrimaryEffect(Optional<class_1291> var1);

   @Accessor("secondaryEffectId")
   @Mutable
   void setSecondaryEffect(Optional<class_1291> var1);
}
