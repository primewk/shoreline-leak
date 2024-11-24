package net.shoreline.client.impl.gui.click.impl.config.setting;

import net.minecraft.class_332;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.gui.click.impl.config.CategoryFrame;
import net.shoreline.client.impl.gui.click.impl.config.ModuleButton;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.render.animation.Animation;

public class CheckboxButton extends ConfigButton<Boolean> {
   public CheckboxButton(CategoryFrame frame, ModuleButton moduleButton, Config<Boolean> config, float x, float y) {
      super(frame, moduleButton, config, x, y);
      config.getAnimation().setState((Boolean)config.getValue());
   }

   public void render(class_332 context, float ix, float iy, float mouseX, float mouseY, float delta) {
      this.x = ix;
      this.y = iy;
      Animation checkboxAnimation = this.config.getAnimation();
      this.rectGradient(context, checkboxAnimation.getFactor() > 0.009999999776482582D ? Modules.CLICK_GUI.getColor((float)checkboxAnimation.getFactor()) : 0, checkboxAnimation.getFactor() > 0.009999999776482582D ? Modules.CLICK_GUI.getColor1((float)checkboxAnimation.getFactor()) : 0);
      RenderManager.renderText(context, this.config.getName(), ix + 2.0F, iy + 4.0F, -1);
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isWithin(mouseX, mouseY) && button == 0) {
         boolean val = (Boolean)this.config.getValue();
         this.config.setValue(!val);
      }

   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }
}
