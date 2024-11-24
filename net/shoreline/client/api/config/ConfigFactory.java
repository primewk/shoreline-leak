package net.shoreline.client.api.config;

import java.lang.reflect.Field;
import net.shoreline.client.Shoreline;

public class ConfigFactory {
   protected final Object configObj;

   public ConfigFactory(Object configObj) {
      this.configObj = configObj;
   }

   public Config<?> build(Field f) {
      f.setAccessible(true);

      try {
         return (Config)f.get(this.configObj);
      } catch (IllegalAccessException | IllegalArgumentException var3) {
         Shoreline.error("Failed to build config from field {}!", f.getName());
         var3.printStackTrace();
         throw new RuntimeException("Invalid field!");
      }
   }
}
