package net.shoreline.client.impl.gui.click.impl.config.setting;

import net.minecraft.class_124;
import net.minecraft.class_332;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.MacroConfig;
import net.shoreline.client.api.macro.Macro;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.gui.click.impl.config.CategoryFrame;
import net.shoreline.client.impl.gui.click.impl.config.ModuleButton;
import net.shoreline.client.impl.module.client.ClickGuiModule;

public class BindButton extends ConfigButton<Macro> {
   private boolean listening;

   public BindButton(CategoryFrame frame, ModuleButton moduleButton, Config<Macro> config, float x, float y) {
      super(frame, moduleButton, config, x, y);
   }

   public void render(class_332 context, float ix, float iy, float mouseX, float mouseY, float delta) {
      ClickGuiModule.CLICK_GUI_SCREEN.setCloseOnEscape(!this.listening);
      this.x = ix;
      this.y = iy;
      Macro macro = (Macro)this.config.getValue();
      String val = this.listening ? "..." : macro.getKeyName();
      this.rect(context, 0);
      RenderManager.renderText(context, this.config.getName() + class_124.field_1080 + " " + val, ix + 2.0F, iy + 4.0F, -1);
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isWithin(mouseX, mouseY)) {
         if (button == 0) {
            this.listening = !this.listening;
         } else if (button == 1 && !this.listening) {
            ((MacroConfig)this.config).setValue(-1);
         } else if (this.listening) {
            if (button != 1) {
               ((MacroConfig)this.config).setValue(1000 + button);
            }

            this.listening = false;
         }
      }

   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.listening) {
         if (keyCode != 256 && keyCode != 259) {
            ((MacroConfig)this.config).setValue(keyCode);
         } else {
            ((MacroConfig)this.config).setValue(-1);
         }

         this.listening = false;
      }

   }

   public boolean isListening() {
      return this.listening;
   }

   public void setListening(boolean listening) {
      this.listening = listening;
   }
}
