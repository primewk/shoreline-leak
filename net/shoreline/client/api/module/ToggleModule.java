package net.shoreline.client.api.module;

import net.shoreline.client.api.Hideable;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.MacroConfig;
import net.shoreline.client.api.config.setting.ToggleConfig;
import net.shoreline.client.api.macro.Macro;
import net.shoreline.client.util.render.animation.Animation;
import net.shoreline.client.util.render.animation.Easing;

public class ToggleModule extends Module implements Hideable {
   private final Animation animation;
   Config<Boolean> enabledConfig;
   Config<Macro> keybindingConfig;
   Config<Boolean> hiddenConfig;

   public ToggleModule(String name, String desc, ModuleCategory category) {
      super(name, desc, category);
      this.animation = new Animation(false, 300.0F, Easing.CUBIC_IN_OUT);
      this.enabledConfig = new ToggleConfig("Enabled", "The module enabled state. This state is true when the module is running.", false);
      this.keybindingConfig = new MacroConfig("Keybind", "The module keybinding. Pressing this key will toggle the module enabled state. Press [BACKSPACE] to delete the keybind.", new Macro(this.getId(), -1, () -> {
         this.toggle();
      }));
      this.hiddenConfig = new BooleanConfig("Hidden", "The hidden state of the module in the Arraylist", false);
      this.register(new Config[]{this.keybindingConfig, this.enabledConfig, this.hiddenConfig});
   }

   public ToggleModule(String name, String desc, ModuleCategory category, Integer keycode) {
      this(name, desc, category);
      this.keybind(keycode);
   }

   public boolean isHidden() {
      return (Boolean)this.hiddenConfig.getValue();
   }

   public void setHidden(boolean hidden) {
      this.hiddenConfig.setValue(hidden);
   }

   public void toggle() {
      if (this.isEnabled()) {
         this.disable();
      } else {
         this.enable();
      }

   }

   public void enable() {
      this.enabledConfig.setValue(true);
      this.onEnable();
   }

   public void disable() {
      this.enabledConfig.setValue(false);
      this.onDisable();
   }

   protected void onEnable() {
   }

   protected void onDisable() {
   }

   public void keybind(int keycode) {
      this.keybindingConfig.setContainer(this);
      ((MacroConfig)this.keybindingConfig).setValue(keycode);
   }

   public boolean isEnabled() {
      return (Boolean)this.enabledConfig.getValue();
   }

   public Macro getKeybinding() {
      return (Macro)this.keybindingConfig.getValue();
   }

   public Animation getAnimation() {
      return this.animation;
   }
}
