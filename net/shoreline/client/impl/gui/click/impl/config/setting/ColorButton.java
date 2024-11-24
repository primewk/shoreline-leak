package net.shoreline.client.impl.gui.click.impl.config.setting;

import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.gui.click.ClickGuiScreen;
import net.shoreline.client.impl.gui.click.impl.config.CategoryFrame;
import net.shoreline.client.impl.gui.click.impl.config.ModuleButton;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.render.animation.Animation;
import net.shoreline.client.util.render.animation.Easing;

public class ColorButton extends ConfigButton<Color> {
   private boolean open;
   private final Animation pickerAnimation;
   private float[] selectedColor;

   public ColorButton(CategoryFrame frame, ModuleButton moduleButton, Config<Color> config, float x, float y) {
      super(frame, moduleButton, config, x, y);
      this.pickerAnimation = new Animation(false, 200.0F, Easing.CUBIC_IN_OUT);
      float[] hsb = ((ColorConfig)config).getHsb();
      this.selectedColor = new float[]{hsb[0], hsb[1], 1.0F - hsb[2], hsb[3]};
   }

   public void render(class_332 context, float ix, float iy, float mouseX, float mouseY, float delta) {
      this.x = ix;
      this.y = iy;
      this.fill(context, (double)(ix + this.width - 11.0F), (double)(iy + 2.0F), 10.0D, 10.0D, ((ColorConfig)this.config).getRgb());
      RenderManager.renderText(context, this.config.getName(), ix + 2.0F, iy + 4.0F, -1);
      if (this.pickerAnimation.getFactor() > 0.009999999776482582D) {
         ColorConfig colorConfig = (ColorConfig)this.config;
         if (ClickGuiScreen.MOUSE_LEFT_HOLD) {
            if (this.isMouseOver((double)mouseX, (double)mouseY, (double)(this.x + 1.0F), (double)(this.y + this.height + 2.0F), (double)(this.width - 2.0F), (double)this.width) && !colorConfig.isGlobal()) {
               this.selectedColor[1] = (mouseX - (this.x + 1.0F)) / (this.width - 1.0F);
               this.selectedColor[2] = (mouseY - (this.y + this.height + 2.0F)) / this.width;
            }

            if (this.isMouseOver((double)mouseX, (double)mouseY, (double)(this.x + 1.0F), (double)(this.y + this.height + 4.0F + this.width), (double)(this.width - 2.0F), 10.0D) && !colorConfig.isGlobal()) {
               this.selectedColor[0] = (mouseX - (this.x + 1.0F)) / (this.width - 1.0F);
            }

            if (colorConfig.allowAlpha() && this.isMouseOver((double)mouseX, (double)mouseY, (double)(this.x + 1.0F), (double)(this.y + this.height + 17.0F + this.width), (double)(this.width - 2.0F), 10.0D)) {
               this.selectedColor[3] = (mouseX - (this.x + 1.0F)) / (this.width - 1.0F);
            }

            Color color = Color.getHSBColor(class_3532.method_15363(this.selectedColor[0], 0.001F, 0.999F), class_3532.method_15363(this.selectedColor[1], 0.001F, 0.999F), 1.0F - class_3532.method_15363(this.selectedColor[2], 0.001F, 0.999F));
            color = new Color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, class_3532.method_15363(this.selectedColor[3], 0.0F, 1.0F));
            colorConfig.setValue(color);
         }

         float[] hsb = colorConfig.getHsb();
         int color = Color.HSBtoRGB(hsb[0], 1.0F, 1.0F);
         this.enableScissor((int)this.x, (int)(this.y + this.height), (int)(this.x + this.width), (int)(this.y + this.height + this.getPickerHeight() * this.getScaledTime()));

         for(float i = 0.0F; i < this.width - 2.0F; ++i) {
            float hue = i / (this.width - 2.0F);
            this.fill(context, (double)(this.x + 1.0F + i), (double)(this.y + this.height + 4.0F + this.width), 1.0D, 10.0D, Color.getHSBColor(hue, 1.0F, 1.0F).getRGB());
         }

         this.fill(context, (double)(this.x + 1.0F + (this.width - 2.0F) * hsb[0]), (double)(this.y + this.height + 4.0F + this.width), 1.0D, 10.0D, -1);
         this.fillGradientQuad(context, this.x + 1.0F, this.y + this.height + 2.0F, this.x + this.width - 1.0F, this.y + this.height + 2.0F + this.width, -1, color, true);
         this.fillGradientQuad(context, this.x + 1.0F, this.y + this.height + 2.0F, this.x + this.width - 1.0F, this.y + this.height + 2.0F + this.width, 0, -16777216, false);
         this.fill(context, (double)(this.x + this.width * hsb[1]), (double)(this.y + this.height + 1.0F + this.width * (1.0F - hsb[2])), 2.0D, 2.0D, -1);
         if (colorConfig.allowAlpha()) {
            this.fillGradient(context, (double)(this.x + 1.0F), (double)(this.y + this.height + 17.0F + this.width), (double)(this.x + this.width - 1.0F), (double)(this.y + this.height + 27.0F + this.width), color, -16777216);
            this.fill(context, (double)(this.x + 1.0F + (this.width - 2.0F) * hsb[3]), (double)(this.y + this.height + 17.0F + this.width), 1.0D, 10.0D, -1);
         }

         if (!this.config.getContainer().getName().equalsIgnoreCase("Colors")) {
            Animation globalAnimation = colorConfig.getAnimation();
            if (globalAnimation.getFactor() > 0.01D) {
               this.fill(context, (double)(this.x + 1.0F), (double)(this.y + this.height + (colorConfig.allowAlpha() ? 29.0F : 17.0F) + this.width), (double)(this.width - 2.0F), 13.0D, Modules.CLICK_GUI.getColor((float)globalAnimation.getFactor()));
            }

            RenderManager.renderText(context, "ClientColor", this.x + 3.0F, this.y + this.height + (colorConfig.allowAlpha() ? 31.0F : 21.0F) + this.width, -1);
         }

         this.moduleButton.offset((float)((double)this.getPickerHeight() * this.pickerAnimation.getFactor()));
         ((CategoryFrame)this.frame).offset((float)((double)this.getPickerHeight() * this.pickerAnimation.getFactor() * (double)this.moduleButton.getScaledTime()));
         this.disableScissor();
      }

   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isWithin(mouseX, mouseY) && button == 1) {
         this.open = !this.open;
         this.pickerAnimation.setState(this.open);
      }

      if (!this.config.getContainer().getName().equalsIgnoreCase("Colors") && this.isMouseOver(mouseX, mouseY, (double)(this.x + 1.0F), (double)(this.y + this.height + (((ColorConfig)this.config).allowAlpha() ? 29.0F : 17.0F) + this.width), (double)(this.width - 2.0F), 13.0D) && button == 0) {
         ColorConfig colorConfig = (ColorConfig)this.config;
         boolean val = !colorConfig.isGlobal();
         colorConfig.setGlobal(val);
         float[] hsb = ((ColorConfig)this.config).getHsb();
         this.selectedColor = new float[]{hsb[0], hsb[1], 1.0F - hsb[2], hsb[3]};
      }

   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }

   public float getPickerHeight() {
      float pickerHeight = 16.0F;
      if (((ColorConfig)this.config).allowAlpha()) {
         pickerHeight += 12.0F;
      }

      if (!this.config.getContainer().getName().equalsIgnoreCase("Colors")) {
         pickerHeight += 15.0F;
      }

      return pickerHeight + this.width;
   }

   public float getScaledTime() {
      return (float)this.pickerAnimation.getFactor();
   }
}
