package net.shoreline.client.api.config.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Supplier;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.NumberDisplay;

public class NumberConfig<T extends Number> extends Config<T> {
   private final T min;
   private final T max;
   private final NumberDisplay format;
   private final int roundingScale;

   public NumberConfig(String name, String desc, T min, T value, T max, NumberDisplay format) {
      super(name, desc, value);
      this.min = min;
      this.max = max;
      this.format = format;
      String strValue = String.valueOf(this.getValue());
      this.roundingScale = strValue.substring(strValue.indexOf(".") + 1).length();
   }

   public NumberConfig(String name, String desc, T min, T value, T max, NumberDisplay format, int roundingScale) {
      super(name, desc, value);
      this.min = min;
      this.max = max;
      this.format = format;
      this.roundingScale = roundingScale;
   }

   public NumberConfig(String name, String desc, T min, T value, T max, NumberDisplay format, Supplier<Boolean> visible) {
      super(name, desc, value, visible);
      this.min = min;
      this.max = max;
      this.format = format;
      String strValue = String.valueOf(this.getValue());
      this.roundingScale = strValue.substring(strValue.indexOf(".") + 1).length();
   }

   public NumberConfig(String name, String desc, T min, T value, T max) {
      this(name, desc, min, value, max, NumberDisplay.DEFAULT);
   }

   public NumberConfig(String name, String desc, T min, T value, T max, Supplier<Boolean> visible) {
      this(name, desc, min, value, max, NumberDisplay.DEFAULT, visible);
   }

   public T getMin() {
      return this.min;
   }

   public T getMax() {
      return this.max;
   }

   public boolean isMin() {
      return this.min.doubleValue() == ((Number)this.getValue()).doubleValue();
   }

   public boolean isMax() {
      return this.max.doubleValue() == ((Number)this.getValue()).doubleValue();
   }

   public int getRoundingScale() {
      return this.roundingScale;
   }

   public NumberDisplay getFormat() {
      return this.format;
   }

   public double getValueSq() {
      T val = (Number)this.getValue();
      return val.doubleValue() * val.doubleValue();
   }

   public void setValue(T val) {
      if (val.doubleValue() < this.min.doubleValue()) {
         super.setValue(this.min);
      } else if (val.doubleValue() > this.max.doubleValue()) {
         super.setValue(this.max);
      } else {
         super.setValue(val);
      }

   }

   public JsonObject toJson() {
      JsonObject configObj = super.toJson();
      if (this.getValue() instanceof Integer) {
         configObj.addProperty("value", (Integer)this.getValue());
      } else if (this.getValue() instanceof Float) {
         configObj.addProperty("value", (Float)this.getValue());
      } else if (this.getValue() instanceof Double) {
         configObj.addProperty("value", (Double)this.getValue());
      }

      return configObj;
   }

   public T fromJson(JsonObject jsonObj) {
      if (jsonObj.has("value")) {
         JsonElement element = jsonObj.get("value");
         if (this.getValue() instanceof Integer) {
            Integer val = element.getAsInt();
            return val;
         }

         if (this.getValue() instanceof Float) {
            Float val = element.getAsFloat();
            return val;
         }

         if (this.getValue() instanceof Double) {
            Double val = element.getAsDouble();
            return val;
         }
      }

      return null;
   }
}
