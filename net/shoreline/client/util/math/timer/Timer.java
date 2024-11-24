package net.shoreline.client.util.math.timer;

public interface Timer {
   long MAX_TIME = -255L;

   boolean passed(Number var1);

   void reset();

   long getElapsedTime();

   void setElapsedTime(Number var1);
}
