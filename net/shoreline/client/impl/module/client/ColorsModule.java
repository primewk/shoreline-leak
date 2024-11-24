package net.shoreline.client.impl.module.client;

import java.awt.Color;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.module.ConcurrentModule;
import net.shoreline.client.api.module.ModuleCategory;

public class ColorsModule extends ConcurrentModule {
   Config<Color> colorConfig = new ColorConfig("Color", "The primary client color", new Color(255, 0, 0), false, false);
   Config<Boolean> rainbowConfig = new BooleanConfig("Rainbow", "Renders rainbow colors for modules", false);

   public ColorsModule() {
      super("Colors", "Client color scheme", ModuleCategory.CLIENT);
   }

   public Color getColor() {
      return (Color)this.colorConfig.getValue();
   }

   public Color getColor(float alpha) {
      ColorConfig config = (ColorConfig)this.colorConfig;
      return new Color((float)config.getRed() / 255.0F, (float)config.getGreen() / 255.0F, (float)config.getBlue() / 255.0F, alpha);
   }

   public Color getColor(int alpha) {
      ColorConfig config = (ColorConfig)this.colorConfig;
      return new Color(config.getRed(), config.getGreen(), config.getBlue(), alpha);
   }

   public Integer getRGB() {
      return this.getColor().getRGB();
   }

   public int getRGB(int a) {
      return this.getColor(a).getRGB();
   }
}
