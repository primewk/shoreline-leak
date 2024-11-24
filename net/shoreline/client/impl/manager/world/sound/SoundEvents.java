package net.shoreline.client.impl.manager.world.sound;

import net.minecraft.class_2960;

public enum SoundEvents {
   CLICK("gui_click");

   private final class_2960 id;

   private SoundEvents(String id) {
      this.id = new class_2960("caspian", String.format("sounds/%s.ogg", id));
   }

   public class_2960 getId() {
      return this.id;
   }

   // $FF: synthetic method
   private static SoundEvents[] $values() {
      return new SoundEvents[]{CLICK};
   }
}
