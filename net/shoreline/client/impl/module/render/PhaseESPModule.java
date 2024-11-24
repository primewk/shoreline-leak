package net.shoreline.client.impl.module.render;

import java.awt.Color;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.util.world.BlastResistantBlocks;

public class PhaseESPModule extends ToggleModule {
   Config<Boolean> safeConfig = new BooleanConfig("Safe", "Highlights safe phase blocks", false);
   Config<Color> unsafeConfig = new ColorConfig("UnsafeColor", "The color for rendering unsafe phase blocks", new Color(255, 0, 0), false, false);
   Config<Color> obsidianConfig = new ColorConfig("ObsidianColor", "The color for rendering obsidian phase blocks", new Color(255, 255, 0), false, false, () -> {
      return (Boolean)this.safeConfig.getValue();
   });
   Config<Color> bedrockConfig = new ColorConfig("BedrockColor", "The color for rendering bedrock phase blocks", new Color(0, 255, 0), false, false, () -> {
      return (Boolean)this.safeConfig.getValue();
   });

   public PhaseESPModule() {
      super("PhaseESP", "Displays safe phase blocks", ModuleCategory.RENDER);
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1724.method_24828()) {
         class_2338 playerPos = mc.field_1724.method_24515();
         class_2350[] var3 = class_2350.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            class_2350 direction = var3[var5];
            if (direction.method_10166().method_10179()) {
               class_2338 blockPos = playerPos.method_10093(direction);
               if (!mc.field_1687.method_8320(blockPos).method_45474()) {
                  class_243 pos = mc.field_1724.method_19538();
                  class_2680 state = mc.field_1687.method_8320(blockPos.method_10074());
                  Color color = null;
                  if (state.method_45474()) {
                     color = (Color)this.unsafeConfig.getValue();
                  } else if ((Boolean)this.safeConfig.getValue()) {
                     if (BlastResistantBlocks.isUnbreakable(state.method_26204())) {
                        color = (Color)this.bedrockConfig.getValue();
                     } else {
                        color = (Color)this.obsidianConfig.getValue();
                     }
                  }

                  if (color != null) {
                     double x = (double)blockPos.method_10263();
                     double y = (double)blockPos.method_10264();
                     double z = (double)blockPos.method_10260();
                     double dx = pos.method_10216() - (double)playerPos.method_10263();
                     double dz = pos.method_10215() - (double)playerPos.method_10260();
                     if (direction == class_2350.field_11034 && dx >= 0.65D) {
                        RenderManager.drawLine(event.getMatrices(), x, y, z, x, y, z + 1.0D, color.getRGB());
                     } else if (direction == class_2350.field_11039 && dx <= 0.35D) {
                        RenderManager.drawLine(event.getMatrices(), x + 1.0D, y, z, x + 1.0D, y, z + 1.0D, color.getRGB());
                     } else if (direction == class_2350.field_11035 && dz >= 0.65D) {
                        RenderManager.drawLine(event.getMatrices(), x, y, z, x + 1.0D, y, z, color.getRGB());
                     } else if (direction == class_2350.field_11043 && dz <= 0.35D) {
                        RenderManager.drawLine(event.getMatrices(), x, y, z + 1.0D, x + 1.0D, y, z + 1.0D, color.getRGB());
                     }
                  }
               }
            }
         }

      }
   }
}
