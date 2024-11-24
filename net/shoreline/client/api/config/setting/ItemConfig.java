package net.shoreline.client.api.config.setting;

import com.google.gson.JsonObject;
import net.minecraft.class_1792;
import net.minecraft.class_7923;
import net.shoreline.client.api.config.Config;

public class ItemConfig extends Config<class_1792> {
   private final class_1792[] values;

   public ItemConfig(String name, String desc, class_1792 value, class_1792[] values) {
      super(name, desc, value);
      this.values = values;
   }

   public ItemConfig(String name, String desc, class_1792 value) {
      this(name, desc, value, (class_1792[])class_7923.field_41178.method_10220().toArray());
   }

   public JsonObject toJson() {
      return null;
   }

   public class_1792 fromJson(JsonObject jsonObj) {
      return null;
   }
}
