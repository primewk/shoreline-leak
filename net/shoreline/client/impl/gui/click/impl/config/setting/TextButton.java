package net.shoreline.client.impl.gui.click.impl.config.setting;

import net.minecraft.class_332;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.gui.click.impl.config.CategoryFrame;
import net.shoreline.client.impl.gui.click.impl.config.ModuleButton;

public class TextButton extends ConfigButton<String> {
   private StringBuilder text;
   private boolean typing;

   public TextButton(CategoryFrame frame, ModuleButton moduleButton, Config<String> config, float x, float y) {
      super(frame, moduleButton, config, x, y);
      this.text = new StringBuilder((String)config.getValue());
   }

   public void render(class_332 context, float ix, float iy, float mouseX, float mouseY, float delta) {
      RenderManager.renderText(context, (String)this.config.getValue(), ix + 3.0F, iy + 3.0F, -1);
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isWithin(mouseX, mouseY) && button == 0) {
         this.typing = !this.typing;
      }

   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.typing) {
      }

   }
}
