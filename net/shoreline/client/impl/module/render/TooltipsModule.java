package net.shoreline.client.impl.module.render;

import net.minecraft.class_1262;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_2371;
import net.minecraft.class_2487;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.gui.RenderTooltipEvent;
import net.shoreline.client.init.Modules;

public class TooltipsModule extends ToggleModule {
   Config<Boolean> shulkersConfig = new BooleanConfig("Shulkers", "Renders all the contents of shulkers in tooltips", true);
   Config<Boolean> mapsConfig = new BooleanConfig("Maps", "Renders a preview of maps in tooltips", false);

   public TooltipsModule() {
      super("Tooltips", "Renders detailed tooltips showing items", ModuleCategory.RENDER);
   }

   @EventListener
   public void onRenderTooltip(RenderTooltipEvent event) {
      class_1799 stack = event.getStack();
      if (!stack.method_7960()) {
         class_2487 nbtCompound = class_1747.method_38072(stack);
         if ((Boolean)this.shulkersConfig.getValue() && nbtCompound != null && nbtCompound.method_10573("Items", 9)) {
            event.cancel();
            event.context.method_51448().method_22903();
            event.context.method_51448().method_46416(0.0F, 0.0F, 600.0F);
            class_2371<class_1799> defaultedList = class_2371.method_10213(27, class_1799.field_8037);
            class_1262.method_5429(nbtCompound, defaultedList);
            RenderManager.rect(event.context.method_51448(), (double)event.getX() + 8.0D, (double)event.getY() - 21.0D, 150.0D, 13.0D, Modules.COLORS.getRGB(170));
            RenderManager.renderText(event.getContext(), stack.method_7964().getString(), (float)event.getX() + 11.0F, (float)event.getY() - 18.0F, -1);
            RenderManager.rect(event.context.method_51448(), (double)event.getX() + 8.0D, (double)event.getY() - 7.0D, 150.0D, 55.0D, 1996488704);

            for(int i = 0; i < defaultedList.size(); ++i) {
               event.context.method_51427((class_1799)defaultedList.get(i), event.getX() + i % 9 * 16 + 9, event.getY() + i / 9 * 16 - 5);
               event.context.method_51431(mc.field_1772, (class_1799)defaultedList.get(i), event.getX() + i % 9 * 16 + 9, event.getY() + i / 9 * 16 - 5);
            }

            event.context.method_51448().method_22909();
         }

      }
   }
}
