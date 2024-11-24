package net.shoreline.client.impl.gui.click.impl.config;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_332;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.macro.Macro;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.gui.click.component.Button;
import net.shoreline.client.impl.gui.click.impl.config.setting.BindButton;
import net.shoreline.client.impl.gui.click.impl.config.setting.CheckboxButton;
import net.shoreline.client.impl.gui.click.impl.config.setting.ColorButton;
import net.shoreline.client.impl.gui.click.impl.config.setting.ConfigButton;
import net.shoreline.client.impl.gui.click.impl.config.setting.DropdownButton;
import net.shoreline.client.impl.gui.click.impl.config.setting.SliderButton;
import net.shoreline.client.impl.gui.click.impl.config.setting.TextButton;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.render.animation.Animation;
import net.shoreline.client.util.render.animation.Easing;

public class ModuleButton extends Button {
   private final Module module;
   private final List<ConfigButton<?>> configComponents = new CopyOnWriteArrayList();
   private float off;
   private boolean open;
   private final Animation settingsAnimation;

   public ModuleButton(Module module, CategoryFrame frame, float x, float y) {
      super(frame, x, y, 103.0F, 13.0F);
      this.settingsAnimation = new Animation(false, 200.0F, Easing.CUBIC_IN_OUT);
      this.module = module;
      Iterator var5 = module.getConfigs().iterator();

      while(var5.hasNext()) {
         Config<?> config = (Config)var5.next();
         if (!config.getName().equalsIgnoreCase("Enabled")) {
            if (config.getValue() instanceof Boolean) {
               this.configComponents.add(new CheckboxButton(frame, this, config, x, y));
            } else if (config.getValue() instanceof Double) {
               this.configComponents.add(new SliderButton(frame, this, config, x, y));
            } else if (config.getValue() instanceof Float) {
               this.configComponents.add(new SliderButton(frame, this, config, x, y));
            } else if (config.getValue() instanceof Integer) {
               this.configComponents.add(new SliderButton(frame, this, config, x, y));
            } else if (config.getValue() instanceof Enum) {
               this.configComponents.add(new DropdownButton(frame, this, config, x, y));
            } else if (config.getValue() instanceof String) {
               this.configComponents.add(new TextButton(frame, this, config, x, y));
            } else if (config.getValue() instanceof Macro) {
               this.configComponents.add(new BindButton(frame, this, config, x, y));
            } else if (config.getValue() instanceof Color) {
               this.configComponents.add(new ColorButton(frame, this, config, x, y));
            }
         }
      }

      this.open = false;
   }

   public void render(class_332 context, float mouseX, float mouseY, float delta) {
      this.render(context, this.x, this.y, mouseX, mouseY, delta);
   }

   public void render(class_332 context, float ix, float iy, float mouseX, float mouseY, float delta) {
      float scaledTime;
      boolean var10000;
      label68: {
         this.x = ix;
         this.y = iy;
         scaledTime = 1.0F;
         Module var10 = this.module;
         if (var10 instanceof ToggleModule) {
            ToggleModule t = (ToggleModule)var10;
            if (!((scaledTime = (float)t.getAnimation().getFactor()) > 0.01F)) {
               var10000 = false;
               break label68;
            }
         }

         var10000 = true;
      }

      boolean fill = var10000;
      scaledTime *= 1.7F;
      if (this.module.getName().equalsIgnoreCase("ClickGui")) {
         scaledTime = 1.7F;
      }

      this.rectGradient(context, fill ? Modules.CLICK_GUI.getColor(scaledTime) : 5592405, fill ? Modules.CLICK_GUI.getColor1(scaledTime) : 5592405);
      RenderManager.renderText(context, this.module.getName(), ix + 2.0F, iy + 3.5F, scaledTime > 0.99F ? -1 : 11184810);
      if (this.settingsAnimation.getFactor() > 0.009999999776482582D) {
         this.off = this.y + this.height + 1.0F;
         float fheight = 0.0F;
         Iterator var14 = this.configComponents.iterator();

         ConfigButton configButton;
         while(var14.hasNext()) {
            configButton = (ConfigButton)var14.next();
            if (configButton.getConfig().isVisible()) {
               fheight += configButton.getHeight();
               if (configButton instanceof ColorButton) {
                  ColorButton colorPicker = (ColorButton)configButton;
                  if (colorPicker.getScaledTime() > 0.01F) {
                     fheight += colorPicker.getPickerHeight() * colorPicker.getScaledTime() * this.getScaledTime();
                  }
               }
            }
         }

         this.enableScissor((int)this.x, (int)(this.off - 1.0F), (int)(this.x + this.width), (int)((double)(this.off + 2.0F) + (double)fheight * this.settingsAnimation.getFactor()));
         var14 = this.configComponents.iterator();

         while(var14.hasNext()) {
            configButton = (ConfigButton)var14.next();
            if (configButton.getConfig().isVisible()) {
               configButton.render(context, ix + 2.0F, this.off, mouseX, mouseY, delta);
               ((CategoryFrame)this.frame).offset((float)((double)configButton.getHeight() * this.settingsAnimation.getFactor()));
               this.off += configButton.getHeight();
            }
         }

         if (fill) {
            this.fill(context, (double)ix, (double)(this.y + this.height), 1.0D, (double)(this.off - (this.y + this.height) + 1.0F), Modules.CLICK_GUI.getColor1(scaledTime));
            this.fill(context, (double)(ix + this.width - 1.0F), (double)(this.y + this.height), 1.0D, (double)(this.off - (this.y + this.height) + 1.0F), Modules.CLICK_GUI.getColor(scaledTime));
            this.fillGradient(context, (double)ix, (double)(this.off + 1.0F), (double)(ix + this.width), (double)(this.off + 2.0F), Modules.CLICK_GUI.getColor(scaledTime), Modules.CLICK_GUI.getColor1(scaledTime));
         }

         this.disableScissor();
         ((CategoryFrame)this.frame).offset((float)(3.0D * this.settingsAnimation.getFactor()));
      }

   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isWithin(mouseX, mouseY)) {
         label31: {
            if (button == 0) {
               Module var7 = this.module;
               if (var7 instanceof ToggleModule) {
                  ToggleModule t = (ToggleModule)var7;
                  t.toggle();
                  break label31;
               }
            }

            if (button == 1) {
               this.open = !this.open;
               this.settingsAnimation.setState(this.open);
            }
         }
      }

      if (this.open) {
         Iterator var8 = this.configComponents.iterator();

         while(var8.hasNext()) {
            ConfigButton<?> component = (ConfigButton)var8.next();
            component.mouseClicked(mouseX, mouseY, button);
         }
      }

   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
      if (this.open) {
         Iterator var6 = this.configComponents.iterator();

         while(var6.hasNext()) {
            ConfigButton<?> component = (ConfigButton)var6.next();
            component.mouseReleased(mouseX, mouseY, button);
         }
      }

   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.open) {
         Iterator var4 = this.configComponents.iterator();

         while(var4.hasNext()) {
            ConfigButton<?> component = (ConfigButton)var4.next();
            component.keyPressed(keyCode, scanCode, modifiers);
         }
      }

   }

   public void offset(float in) {
      this.off += in;
   }

   public boolean isOpen() {
      return this.open;
   }

   public float getScaledTime() {
      return (float)this.settingsAnimation.getFactor();
   }

   public Module getModule() {
      return this.module;
   }

   public List<ConfigButton<?>> getConfigButtons() {
      return this.configComponents;
   }
}
