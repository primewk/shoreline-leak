package net.shoreline.client.impl.gui.click.impl.config.setting;

import net.minecraft.class_332;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.impl.gui.click.component.Button;
import net.shoreline.client.impl.gui.click.impl.config.CategoryFrame;
import net.shoreline.client.impl.gui.click.impl.config.ModuleButton;

public abstract class ConfigButton<T> extends Button {
   protected final Config<T> config;
   protected final ModuleButton moduleButton;

   public ConfigButton(CategoryFrame frame, ModuleButton moduleButton, Config<T> config, float x, float y) {
      super(frame, x, y, 99.0F, 13.0F);
      this.moduleButton = moduleButton;
      this.config = config;
   }

   public void render(class_332 context, float mouseX, float mouseY, float delta) {
      this.render(context, this.x, this.y, mouseX, mouseY, delta);
   }

   public abstract void render(class_332 var1, float var2, float var3, float var4, float var5, float var6);

   public Config<T> getConfig() {
      return this.config;
   }
}
