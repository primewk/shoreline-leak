package net.shoreline.client.impl.event;

import net.minecraft.class_4184;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class PerspectiveEvent extends Event {
   public class_4184 camera;

   public PerspectiveEvent(class_4184 camera) {
      this.camera = camera;
   }

   public class_4184 getCamera() {
      return this.camera;
   }
}
