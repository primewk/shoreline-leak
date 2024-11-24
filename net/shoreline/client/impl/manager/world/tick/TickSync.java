package net.shoreline.client.impl.manager.world.tick;

public enum TickSync {
   CURRENT,
   AVERAGE,
   MINIMAL,
   NONE;

   // $FF: synthetic method
   private static TickSync[] $values() {
      return new TickSync[]{CURRENT, AVERAGE, MINIMAL, NONE};
   }
}
