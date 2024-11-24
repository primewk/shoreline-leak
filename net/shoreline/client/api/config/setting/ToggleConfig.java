package net.shoreline.client.api.config.setting;

import net.shoreline.client.Shoreline;
import net.shoreline.client.api.config.ConfigContainer;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.util.render.animation.Animation;

public class ToggleConfig extends BooleanConfig {
   public ToggleConfig(String name, String desc, Boolean val) {
      super(name, desc, val);
   }

   public void setValue(Boolean val) {
      super.setValue(val);
      ConfigContainer container = this.getContainer();
      if (container instanceof ToggleModule) {
         ToggleModule toggle = (ToggleModule)container;
         Animation anim = toggle.getAnimation();
         anim.setState(val);
         if (val) {
            Shoreline.EVENT_HANDLER.subscribe(toggle);
         } else {
            Shoreline.EVENT_HANDLER.unsubscribe(toggle);
         }
      }

   }

   public void enable() {
      ConfigContainer container = this.getContainer();
      if (container instanceof ToggleModule) {
         ToggleModule toggle = (ToggleModule)container;
         toggle.enable();
      }

   }

   public void disable() {
      ConfigContainer container = this.getContainer();
      if (container instanceof ToggleModule) {
         ToggleModule toggle = (ToggleModule)container;
         toggle.disable();
      }

   }
}
