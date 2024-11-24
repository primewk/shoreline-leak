package net.shoreline.client.api.config.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.util.function.Supplier;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.init.Modules;

public class ColorConfig extends Config<Color> {
   private final boolean allowAlpha;
   private boolean global;

   public ColorConfig(String name, String desc, Color value, boolean allowAlpha, boolean global) {
      super(name, desc, value);
      this.allowAlpha = allowAlpha;
      this.setGlobal(global);
   }

   public ColorConfig(String name, String desc, Color value) {
      this(name, desc, value, true, true);
   }

   public ColorConfig(String name, String desc, Color value, boolean allowAlpha, boolean global, Supplier<Boolean> visible) {
      super(name, desc, value, visible);
      this.allowAlpha = allowAlpha;
      this.setGlobal(global);
   }

   public ColorConfig(String name, String desc, Color value, boolean allowAlpha, Supplier<Boolean> visible) {
      this(name, desc, value, allowAlpha, true, visible);
   }

   public ColorConfig(String name, String desc, Color value, Supplier<Boolean> visible) {
      this(name, desc, value, true, visible);
   }

   public ColorConfig(String name, String desc, Integer rgb, boolean allowAlpha) {
      this(name, desc, new Color(rgb, (rgb & -16777216) != -16777216), allowAlpha, true);
   }

   public ColorConfig(String name, String desc, Integer value) {
      this(name, desc, value, false);
   }

   public Color getValue() {
      return Modules.COLORS != null && this.global ? Modules.COLORS.getColor(this.getAlpha()) : new Color(((Color)this.value).getRed(), ((Color)this.value).getGreen(), ((Color)this.value).getBlue(), this.allowAlpha ? ((Color)this.value).getAlpha() : 255);
   }

   public Color getValue(int alpha) {
      return Modules.COLORS != null && this.global ? Modules.COLORS.getColor(alpha) : new Color(((Color)this.value).getRed(), ((Color)this.value).getGreen(), ((Color)this.value).getBlue(), alpha);
   }

   public void setValue(int val) {
      Color color = new Color(val, (val & -16777216) != -16777216);
      this.setValue(color);
   }

   public int getRgb() {
      return this.getValue().getRGB();
   }

   public int getRgb(int alpha) {
      return this.getValue(alpha).getRGB();
   }

   public int getRed() {
      return ((Color)this.value).getRed();
   }

   public int getGreen() {
      return ((Color)this.value).getGreen();
   }

   public int getBlue() {
      return ((Color)this.value).getBlue();
   }

   public boolean allowAlpha() {
      return this.allowAlpha;
   }

   public int getAlpha() {
      return this.allowAlpha ? ((Color)this.value).getAlpha() : 255;
   }

   public float[] getHsb() {
      float[] hsbVals = Color.RGBtoHSB(((Color)this.value).getRed(), ((Color)this.value).getGreen(), ((Color)this.value).getBlue(), (float[])null);
      return new float[]{hsbVals[0], hsbVals[1], hsbVals[2], this.allowAlpha ? (float)((Color)this.value).getAlpha() / 255.0F : 1.0F};
   }

   public boolean isGlobal() {
      return this.global;
   }

   public void setGlobal() {
      this.setGlobal(true);
   }

   public void setGlobal(boolean global) {
      this.global = global;
      this.configAnimation.setState(global);
      if (Modules.COLORS != null && global) {
         this.setValue(Modules.COLORS.getColor(this.getAlpha()));
      }

   }

   public JsonObject toJson() {
      JsonObject configObj = super.toJson();
      configObj.addProperty("value", "0x" + Integer.toHexString(this.getRgb()));
      configObj.addProperty("global", this.global);
      return configObj;
   }

   public Color fromJson(JsonObject jsonObj) {
      if (jsonObj.has("value")) {
         JsonElement element = jsonObj.get("value");
         String hex = element.getAsString();
         if (jsonObj.has("global")) {
            JsonElement element1 = jsonObj.get("global");
            this.setGlobal(element1.getAsBoolean());
         }

         Color color = this.parseColor(hex);
         return color;
      } else {
         return null;
      }
   }

   private Color parseColor(String colorString) {
      if (colorString.startsWith("0x")) {
         colorString = colorString.substring(2);
         return new Color((int)Long.parseLong(colorString, 16), true);
      } else {
         throw new IllegalArgumentException("Unknown color: " + colorString);
      }
   }
}
