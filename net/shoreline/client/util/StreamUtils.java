package net.shoreline.client.util;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

public class StreamUtils {
   public static <T, U extends Comparable<? super U>> Stream<T> sortCached(Stream<T> stream, Function<? super T, ? extends U> keyExtractor) {
      return stream.map((t) -> {
         U key = (Comparable)keyExtractor.apply(t);
         return new StreamUtils.Intermediary(t, key);
      }).sorted(Comparator.comparing(StreamUtils.Intermediary::key)).map(StreamUtils.Intermediary::value);
   }

   private static record Intermediary<T, U extends Comparable<? super U>>(T value, U key) {
      private Intermediary(T value, U key) {
         this.value = value;
         this.key = key;
      }

      public T value() {
         return this.value;
      }

      public U key() {
         return this.key;
      }
   }
}
