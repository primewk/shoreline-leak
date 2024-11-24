package net.shoreline.client.impl.module.render;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_243;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;

public class BreadcrumbsModule extends ToggleModule {
   private final Map<class_243, Long> positions = new ConcurrentHashMap();
   Config<Boolean> infiniteConfig = new BooleanConfig("Infinite", "Renders breadcrumbs for all positions since toggle", true);
   Config<Float> maxTimeConfig = new NumberConfig("MaxPosition", "The maximum time for a given position", 1.0F, 2.0F, 20.0F);

   public BreadcrumbsModule() {
      super("Breadcrumbs", "Renders a line connecting all previous positions", ModuleCategory.RENDER);
   }

   public void onDisable() {
      this.positions.clear();
   }

   @EventListener
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (event.getStage() == EventStage.PRE) {
         this.positions.put(new class_243(mc.field_1724.method_23317(), mc.field_1724.method_5829().field_1322, mc.field_1724.method_23321()), System.currentTimeMillis());
         if (!(Boolean)this.infiniteConfig.getValue()) {
            this.positions.forEach((p, t) -> {
               if ((float)(System.currentTimeMillis() - t) >= (Float)this.maxTimeConfig.getValue() * 1000.0F) {
                  this.positions.remove(p);
               }

            });
         }

      }
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
   }
}
