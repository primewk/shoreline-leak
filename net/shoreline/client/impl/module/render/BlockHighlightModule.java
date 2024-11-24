package net.shoreline.client.impl.module.render;

import java.text.DecimalFormat;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_239.class_240;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.BoxRender;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.render.RenderBlockOutlineEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;

public class BlockHighlightModule extends ToggleModule {
   Config<BoxRender> boxModeConfig;
   Config<Boolean> entitiesConfig;
   private double distance;

   public BlockHighlightModule() {
      super("BlockHighlight", "Highlights the block the player is facing", ModuleCategory.RENDER);
      this.boxModeConfig = new EnumConfig("BoxMode", "Box rendering mode", BoxRender.OUTLINE, BoxRender.values());
      this.entitiesConfig = new BooleanConfig("Debug-Entities", "Highlights entity bounding boxes for debug purposes", false);
   }

   public String getModuleData() {
      DecimalFormat decimal = new DecimalFormat("0.0");
      return decimal.format(this.distance);
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (mc.field_1687 != null) {
         class_238 render = null;
         class_239 result = mc.field_1765;
         if (result != null) {
            class_243 pos = Managers.POSITION.getEyePos();
            if ((Boolean)this.entitiesConfig.getValue() && result.method_17783() == class_240.field_1331) {
               class_1297 entity = ((class_3966)result).method_17782();
               render = entity.method_5829();
               this.distance = pos.method_1022(entity.method_19538());
            } else if (result.method_17783() == class_240.field_1332) {
               class_2338 hpos = ((class_3965)result).method_17777();
               class_2680 state = mc.field_1687.method_8320(hpos);
               class_265 outlineShape = state.method_26218(mc.field_1687, hpos);
               if (outlineShape.method_1110()) {
                  return;
               }

               class_238 render1 = outlineShape.method_1107();
               render = new class_238((double)hpos.method_10263() + render1.field_1323, (double)hpos.method_10264() + render1.field_1322, (double)hpos.method_10260() + render1.field_1321, (double)hpos.method_10263() + render1.field_1320, (double)hpos.method_10264() + render1.field_1325, (double)hpos.method_10260() + render1.field_1324);
               this.distance = pos.method_1022(hpos.method_46558());
            }
         }

         if (render != null) {
            switch((BoxRender)this.boxModeConfig.getValue()) {
            case FILL:
               RenderManager.renderBox(event.getMatrices(), render, Modules.COLORS.getRGB(60));
               RenderManager.renderBoundingBox(event.getMatrices(), render, 2.5F, Modules.COLORS.getRGB(145));
               break;
            case OUTLINE:
               RenderManager.renderBoundingBox(event.getMatrices(), render, 2.5F, Modules.COLORS.getRGB(145));
            }
         }

      }
   }

   @EventListener
   public void onRenderBlockOutline(RenderBlockOutlineEvent event) {
      event.cancel();
   }
}
