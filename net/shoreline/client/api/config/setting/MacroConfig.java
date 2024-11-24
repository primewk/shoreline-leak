package net.shoreline.client.api.config.setting;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.macro.Macro;
import net.shoreline.client.init.Managers;

public class MacroConfig extends Config<Macro> {
   public MacroConfig(String name, String desc, Macro val) {
      super(name, desc, val);
   }

   public void setValue(int keycode) {
      ((Macro)this.getValue()).setKeycode(keycode);
      if (Managers.isInitialized()) {
         Managers.MACRO.setMacro((Macro)this.getValue(), keycode);
      }

   }

   public String getMacroId() {
      return ((Macro)this.value).getName();
   }

   public Runnable getRunnable() {
      return ((Macro)this.value).getRunnable();
   }

   public int getKeycode() {
      return ((Macro)this.value).getKeycode();
   }

   public String getKeyName() {
      return ((Macro)this.value).getKeyName();
   }
}
