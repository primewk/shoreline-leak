package net.shoreline.client.mixin.network.packet.c2s;

import io.netty.buffer.Unpooled;
import net.minecraft.class_1297;
import net.minecraft.class_2540;
import net.minecraft.class_2824;
import net.shoreline.client.impl.imixin.IPlayerInteractEntityC2SPacket;
import net.shoreline.client.util.Globals;
import net.shoreline.client.util.network.InteractType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_2824.class})
public abstract class MixinPlayerInteractEntityC2SPacket implements IPlayerInteractEntityC2SPacket, Globals {
   @Shadow
   @Final
   private int field_12870;

   @Shadow
   public abstract void method_11052(class_2540 var1);

   public class_1297 getEntity() {
      return mc.field_1687.method_8469(this.field_12870);
   }

   public InteractType getType() {
      class_2540 packetBuf = new class_2540(Unpooled.buffer());
      this.method_11052(packetBuf);
      packetBuf.method_10816();
      return (InteractType)packetBuf.method_10818(InteractType.class);
   }
}
