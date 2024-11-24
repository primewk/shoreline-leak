package net.shoreline.client.impl.module.render;

import net.minecraft.class_1657;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.render.entity.RenderEntityInvisibleEvent;

public final class TrueSightModule extends ToggleModule {
   Config<Boolean> onlyPlayersConfig = new BooleanConfig("OnlyPlayers", "If to only reveal invisible players", true);

   public TrueSightModule() {
      super("TrueSight", "Allows you to see invisible entities", ModuleCategory.RENDER);
   }

   @EventListener
   public void onRenderEntityInvisible(RenderEntityInvisibleEvent event) {
      if (event.getEntity().method_5767() && (!(Boolean)this.onlyPlayersConfig.getValue() || event.getEntity() instanceof class_1657)) {
         event.cancel();
      }

   }
}
