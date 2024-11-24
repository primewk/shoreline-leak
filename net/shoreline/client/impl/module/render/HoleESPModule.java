package net.shoreline.client.impl.module.render;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_238;
import net.minecraft.class_3532;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.impl.manager.combat.hole.Hole;
import net.shoreline.client.impl.manager.combat.hole.HoleType;
import net.shoreline.client.init.Managers;

public class HoleESPModule extends ToggleModule {
   Config<Float> rangeConfig = new NumberConfig("Range", "Range to display holes", 3.0F, 5.0F, 25.0F);
   Config<Float> heightConfig = new NumberConfig("Size", "Render height of holes", -1.0F, 1.0F, 1.0F);
   Config<Boolean> obsidianCheckConfig = new BooleanConfig("Obsidian", "Displays obsidian holes", true);
   Config<Boolean> obsidianBedrockConfig = new BooleanConfig("Obsidian-Bedrock", "Displays mixed obsidian and bedrock holes", true);
   Config<Boolean> doubleConfig = new BooleanConfig("Double", "Displays double holes where the player can stand in the middle of two blocks to block explosion damage", false);
   Config<Boolean> quadConfig = new BooleanConfig("Quad", "Displays quad holes where the player can stand in the middle of four blocks to block explosion damage", false);
   Config<Boolean> voidConfig = new BooleanConfig("Void", "Displays void holes in the world", false);
   Config<Boolean> fadeConfig = new BooleanConfig("Fade", "Fades the opacity of holes based on distance", false);
   Config<Color> obsidianConfig = new ColorConfig("ObsidianColor", "The color for rendering obsidian holes", new Color(255, 0, 0, 100), () -> {
      return (Boolean)this.obsidianCheckConfig.getValue();
   });
   Config<Color> mixedConfig = new ColorConfig("Obsidian-BedrockColor", "The color for rendering mixed holes", new Color(255, 255, 0, 100), () -> {
      return (Boolean)this.obsidianBedrockConfig.getValue();
   });
   Config<Color> bedrockConfig = new ColorConfig("BedrockColor", "The color for rendering bedrock holes", new Color(0, 255, 0, 100));
   Config<Color> voidColorConfig = new ColorConfig("VoidColor", "The color for rendering bedrock holes", new Color(255, 0, 0, 160), () -> {
      return (Boolean)this.voidConfig.getValue();
   });

   public HoleESPModule() {
      super("HoleESP", "Displays nearby blast resistant holes", ModuleCategory.RENDER);
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (mc.field_1724 != null) {
         Iterator var2 = Managers.HOLE.getHoles().iterator();

         while(true) {
            Hole hole;
            do {
               do {
                  do {
                     do {
                        do {
                           if (!var2.hasNext()) {
                              return;
                           }

                           hole = (Hole)var2.next();
                        } while((hole.isDoubleX() || hole.isDoubleZ()) && !(Boolean)this.doubleConfig.getValue());
                     } while(hole.isQuad() && !(Boolean)this.quadConfig.getValue());
                  } while(hole.getSafety() == HoleType.VOID && !(Boolean)this.voidConfig.getValue());
               } while(hole.getSafety() == HoleType.OBSIDIAN && !(Boolean)this.obsidianCheckConfig.getValue());
            } while(hole.getSafety() == HoleType.OBSIDIAN_BEDROCK && !(Boolean)this.obsidianBedrockConfig.getValue());

            double dist = hole.squaredDistanceTo(mc.field_1724);
            if (!(dist > ((NumberConfig)this.rangeConfig).getValueSq())) {
               double x = hole.method_10216();
               double y = hole.method_10214();
               double z = hole.method_10215();
               class_238 render = null;
               if (hole.getSafety() == HoleType.VOID) {
                  render = new class_238(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
               } else if (hole.isDoubleX()) {
                  render = new class_238(x, y, z, x + 2.0D, y + (double)(Float)this.heightConfig.getValue(), z + 1.0D);
               } else if (hole.isDoubleZ()) {
                  render = new class_238(x, y, z, x + 1.0D, y + (double)(Float)this.heightConfig.getValue(), z + 2.0D);
               } else if (hole.isQuad()) {
                  render = new class_238(x, y, z, x + 2.0D, y + (double)(Float)this.heightConfig.getValue(), z + 2.0D);
               } else if (hole.isStandard()) {
                  render = new class_238(x, y, z, x + 1.0D, y + (double)(Float)this.heightConfig.getValue(), z + 1.0D);
               }

               if (render == null) {
                  return;
               }

               double alpha = 1.0D;
               if ((Boolean)this.fadeConfig.getValue()) {
                  double fadeRange = (double)(Float)this.rangeConfig.getValue() - 1.0D;
                  double fadeRangeSq = fadeRange * fadeRange;
                  alpha = (fadeRangeSq + 9.0D - mc.field_1724.method_5649(hole.method_10216(), hole.method_10214(), hole.method_10215())) / fadeRangeSq;
                  alpha = class_3532.method_15350(alpha, 0.0D, 1.0D);
               }

               RenderManager.renderBox(event.getMatrices(), render, this.getHoleColor(hole.getSafety(), alpha));
               RenderManager.renderBoundingBox(event.getMatrices(), render, 1.5F, this.getHoleColor(hole.getSafety(), (int)(alpha * 145.0D)));
            }
         }
      }
   }

   private int getHoleColor(HoleType holeType, double alpha) {
      ColorConfig obsidian = (ColorConfig)this.obsidianConfig;
      ColorConfig mixed = (ColorConfig)this.mixedConfig;
      ColorConfig bedrock = (ColorConfig)this.bedrockConfig;
      ColorConfig voidColor = (ColorConfig)this.voidColorConfig;
      int var10000;
      switch(holeType) {
      case OBSIDIAN:
         var10000 = obsidian.getRgb((int)((double)obsidian.getAlpha() * alpha));
         break;
      case OBSIDIAN_BEDROCK:
         var10000 = mixed.getRgb((int)((double)mixed.getAlpha() * alpha));
         break;
      case BEDROCK:
         var10000 = bedrock.getRgb((int)((double)bedrock.getAlpha() * alpha));
         break;
      case VOID:
         var10000 = voidColor.getRgb((int)((double)voidColor.getAlpha() * alpha));
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   private int getHoleColor(HoleType holeType, int alpha) {
      int var10000;
      switch(holeType) {
      case OBSIDIAN:
         var10000 = ((ColorConfig)this.obsidianConfig).getRgb(alpha);
         break;
      case OBSIDIAN_BEDROCK:
         var10000 = ((ColorConfig)this.mixedConfig).getRgb(alpha);
         break;
      case BEDROCK:
         var10000 = ((ColorConfig)this.bedrockConfig).getRgb(alpha);
         break;
      case VOID:
         var10000 = ((ColorConfig)this.voidColorConfig).getRgb(alpha);
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   public double getRange() {
      return (double)(Float)this.rangeConfig.getValue();
   }
}
