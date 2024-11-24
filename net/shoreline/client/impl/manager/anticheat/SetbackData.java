package net.shoreline.client.impl.manager.anticheat;

import net.minecraft.class_243;

public record SetbackData(class_243 position, long timeMS, int teleportID) {
   public SetbackData(class_243 position, long timeMS, int teleportID) {
      this.position = position;
      this.timeMS = timeMS;
      this.teleportID = teleportID;
   }

   public long timeSince() {
      return System.currentTimeMillis() - this.timeMS;
   }

   public class_243 position() {
      return this.position;
   }

   public long timeMS() {
      return this.timeMS;
   }

   public int teleportID() {
      return this.teleportID;
   }
}
