package net.shoreline.client.impl.gui.click.impl.config.setting;

import java.util.Arrays;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.gui.click.impl.config.CategoryFrame;
import net.shoreline.client.impl.gui.click.impl.config.ModuleButton;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.string.EnumFormatter;

public class DropdownButton extends ConfigButton<Enum<?>> {
   private int index;

   public DropdownButton(CategoryFrame frame, ModuleButton moduleButton, Config<Enum<?>> config, float x, float y) {
      super(frame, moduleButton, config, x, y);
   }

   public void render(class_332 context, float ix, float iy, float mouseX, float mouseY, float delta) {
      this.x = ix;
      this.y = iy;
      String val = EnumFormatter.formatEnum((Enum)this.config.getValue());
      this.rectGradient(context, Modules.CLICK_GUI.getColor(), Modules.CLICK_GUI.getColor1());
      RenderManager.renderText(context, this.config.getName() + class_124.field_1080 + " " + val, ix + 2.0F, iy + 4.0F, -1);
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isWithin(mouseX, mouseY)) {
         Enum<?> val = (Enum)this.config.getValue();
         String[] values = (String[])Arrays.stream((Enum[])val.getClass().getEnumConstants()).map(Enum::name).toArray((x$0) -> {
            return new String[x$0];
         });
         if (button == 0) {
            this.index = this.index + 1 > values.length - 1 ? 0 : this.index + 1;
            this.config.setValue(Enum.valueOf(val.getClass(), values[this.index]));
         } else if (button == 1) {
            this.index = this.index - 1 < 0 ? values.length - 1 : this.index - 1;
            this.config.setValue(Enum.valueOf(val.getClass(), values[this.index]));
         }
      }

   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }
}
