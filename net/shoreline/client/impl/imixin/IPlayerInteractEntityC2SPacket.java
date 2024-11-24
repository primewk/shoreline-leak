package net.shoreline.client.impl.imixin;

import net.minecraft.class_1297;
import net.shoreline.client.util.network.InteractType;

public interface IPlayerInteractEntityC2SPacket {
   class_1297 getEntity();

   InteractType getType();
}
