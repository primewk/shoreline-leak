package net.shoreline.client.util.math.timer;

import java.util.concurrent.TimeUnit;

public class CacheTimer implements Timer {
   private long time = System.nanoTime();

   public boolean passed(Number time) {
      if (time.longValue() <= 0L) {
         return true;
      } else {
         return this.getElapsedTime() > time.longValue();
      }
   }

   public boolean passed(Number time, TimeUnit unit) {
      return this.passed(unit.toMillis(time.longValue()));
   }

   public long getElapsedTime() {
      return this.toMillis(System.nanoTime() - this.time);
   }

   public void setElapsedTime(Number time) {
      this.time = time.longValue() == -255L ? 0L : System.nanoTime() - time.longValue();
   }

   public long getElapsedTime(TimeUnit unit) {
      return unit.convert(this.getElapsedTime(), TimeUnit.MILLISECONDS);
   }

   public void reset() {
      this.time = System.nanoTime();
   }

   private long toMillis(long nanos) {
      return nanos / 1000000L;
   }
}
