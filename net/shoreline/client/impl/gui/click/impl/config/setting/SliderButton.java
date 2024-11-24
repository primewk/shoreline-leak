package net.shoreline.client.impl.gui.click.impl.config.setting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.gui.click.ClickGuiScreen;
import net.shoreline.client.impl.gui.click.impl.config.CategoryFrame;
import net.shoreline.client.impl.gui.click.impl.config.ModuleButton;
import net.shoreline.client.init.Modules;

public class SliderButton<T extends Number> extends ConfigButton<T> {
   private final int scale;

   public SliderButton(CategoryFrame frame, ModuleButton moduleButton, Config<T> config, float x, float y) {
      super(frame, moduleButton, config, x, y);
      String sval = String.valueOf(config.getValue());
      this.scale = sval.substring(sval.indexOf(".") + 1).length();
   }

   public void render(class_332 context, float ix, float iy, float mouseX, float mouseY, float delta) {
      this.x = ix;
      this.y = iy;
      Number min = ((NumberConfig)this.config).getMin();
      Number max = ((NumberConfig)this.config).getMax();
      float fillv;
      if (this.isWithin(mouseX, mouseY) && ClickGuiScreen.MOUSE_LEFT_HOLD) {
         fillv = (mouseX - ix) / this.width;
         float lower;
         float upper;
         if (this.config.getValue() instanceof Integer) {
            lower = min.floatValue() + fillv * (float)(max.intValue() - min.intValue());
            int bval = (int)class_3532.method_15363(lower, (float)min.intValue(), (float)max.intValue());
            ((NumberConfig)this.config).setValue((Number)bval);
         } else if (this.config.getValue() instanceof Float) {
            lower = min.floatValue() + fillv * (max.floatValue() - min.floatValue());
            upper = class_3532.method_15363(lower, min.floatValue(), max.floatValue());
            BigDecimal bigDecimal = new BigDecimal((double)upper);
            upper = bigDecimal.setScale(this.scale, RoundingMode.HALF_UP).floatValue();
            ((NumberConfig)this.config).setValue((Number)upper);
         } else if (this.config.getValue() instanceof Double) {
            double val = min.doubleValue() + (double)fillv * (max.doubleValue() - min.doubleValue());
            double bval = class_3532.method_15350(val, min.doubleValue(), max.doubleValue());
            BigDecimal bigDecimal = new BigDecimal(bval);
            bval = bigDecimal.setScale(this.scale, RoundingMode.HALF_UP).doubleValue();
            ((NumberConfig)this.config).setValue((Number)bval);
         }

         lower = ix + 1.0F;
         upper = ix + this.width - 1.0F;
         if (mouseX < lower) {
            this.config.setValue(min);
         } else if (mouseX > upper) {
            this.config.setValue(max);
         }
      }

      fillv = (((Number)this.config.getValue()).floatValue() - min.floatValue()) / (max.floatValue() - min.floatValue());
      this.fillGradient(context, (double)ix, (double)iy, (double)(ix + fillv * this.width), (double)(iy + this.height), Modules.CLICK_GUI.getColor(), Modules.CLICK_GUI.getColor1());
      RenderManager.renderText(context, this.config.getName() + class_124.field_1080 + " " + this.config.getValue(), ix + 2.0F, iy + 4.0F, -1);
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }
}
