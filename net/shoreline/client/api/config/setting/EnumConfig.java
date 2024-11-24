package net.shoreline.client.api.config.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Supplier;
import net.shoreline.client.api.config.Config;

public class EnumConfig<T extends Enum<?>> extends Config<T> {
   private final T[] values;
   private int index;

   public EnumConfig(String name, String desc, T val, T[] values) {
      super(name, desc, val);
      this.values = values;
   }

   public EnumConfig(String name, String desc, T val, T[] values, Supplier<Boolean> visible) {
      super(name, desc, val, visible);
      this.values = values;
   }

   public String getValueName() {
      return ((Enum)this.getValue()).name();
   }

   public T[] getValues() {
      return this.values;
   }

   public T getNextValue() {
      this.index = this.index + 1 > this.values.length - 1 ? 0 : this.index + 1;
      return this.values[this.index];
   }

   public T getPreviousValue() {
      this.index = this.index - 1 < 0 ? this.values.length - 1 : this.index - 1;
      return this.values[this.index];
   }

   public JsonObject toJson() {
      JsonObject configObj = super.toJson();
      configObj.addProperty("value", this.getValueName());
      return configObj;
   }

   public T fromJson(JsonObject jsonObj) {
      if (jsonObj.has("value")) {
         JsonElement element = jsonObj.get("value");

         try {
            return Enum.valueOf(((Enum)this.getValue()).getClass(), element.getAsString());
         } catch (IllegalArgumentException var4) {
            return null;
         }
      } else {
         return (Enum)this.getValue();
      }
   }
}
