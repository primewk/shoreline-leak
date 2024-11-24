package net.shoreline.client.util;

import java.util.concurrent.ConcurrentLinkedDeque;
import org.jetbrains.annotations.NotNull;

public class EvictingQueue<E> extends ConcurrentLinkedDeque<E> {
   private final int limit;

   public EvictingQueue(int limit) {
      this.limit = limit;
   }

   public boolean add(@NotNull E element) {
      boolean add = super.add(element);

      while(add && this.size() > this.limit) {
         super.remove();
      }

      return add;
   }

   public void addFirst(@NotNull E element) {
      super.addFirst(element);

      while(this.size() > this.limit) {
         super.removeLast();
      }

   }

   public int limit() {
      return this.limit;
   }
}
