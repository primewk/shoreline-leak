package net.shoreline.client.impl.module.render;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.render.CameraClipEvent;

public class ViewClipModule extends ToggleModule {
   Config<Float> distanceConfig = new NumberConfig("Distance", "The third-person camera clip distance", 1.0F, 3.5F, 20.0F);

   public ViewClipModule() {
      super("ViewClip", "Clips your third-person camera through blocks", ModuleCategory.RENDER);
   }

   @EventListener
   public void onCameraClip(CameraClipEvent event) {
      event.cancel();
      event.setDistance((double)(Float)this.distanceConfig.getValue());
   }
}
