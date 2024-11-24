package net.shoreline.client.impl.event.render.block;

import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class RenderBlockEvent extends Event {
   private final class_2680 state;
   private final class_2338 pos;

   public RenderBlockEvent(class_2680 state, class_2338 pos) {
      this.state = state;
      this.pos = pos;
   }

   public class_2680 getState() {
      return this.state;
   }

   public class_2338 getPos() {
      return this.pos;
   }

   public class_2248 getBlock() {
      return this.state.method_26204();
   }
}
