package net.shoreline.client.impl.event.world;

import net.minecraft.class_1923;
import net.shoreline.client.api.event.StageEvent;

public class ChunkLoadEvent extends StageEvent {
   private final class_1923 pos;

   public ChunkLoadEvent(class_1923 pos) {
      this.pos = pos;
   }

   public class_1923 getPos() {
      return this.pos;
   }
}
