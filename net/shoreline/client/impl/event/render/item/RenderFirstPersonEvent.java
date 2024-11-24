package net.shoreline.client.impl.event.render.item;

import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.shoreline.client.api.event.Event;

public class RenderFirstPersonEvent extends Event {
   public final class_1268 hand;
   public final class_1799 item;
   public final float equipProgress;
   public final class_4587 matrices;

   public RenderFirstPersonEvent(class_1268 hand, class_1799 item, float equipProgress, class_4587 matrices) {
      this.hand = hand;
      this.item = item;
      this.equipProgress = equipProgress;
      this.matrices = matrices;
   }
}
