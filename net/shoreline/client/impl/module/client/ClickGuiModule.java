package net.shoreline.client.impl.module.client;

import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.gui.click.ClickGuiScreen;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.render.animation.Animation;
import net.shoreline.client.util.render.animation.Easing;

public class ClickGuiModule extends ToggleModule {
   public static ClickGuiScreen CLICK_GUI_SCREEN;
   private final Animation openCloseAnimation;
   public float scaleConfig;

   public ClickGuiModule() {
      super("ClickGui", "Opens the clickgui screen", ModuleCategory.CLIENT, 344);
      this.openCloseAnimation = new Animation(false, 300.0F, Easing.CUBIC_IN_OUT);
      this.scaleConfig = 1.0F;
   }

   public void onEnable() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (CLICK_GUI_SCREEN == null) {
            CLICK_GUI_SCREEN = new ClickGuiScreen(this);
         }

         mc.method_1507(CLICK_GUI_SCREEN);
         this.openCloseAnimation.setState(true);
      } else {
         this.toggle();
      }
   }

   public void onDisable() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         mc.field_1724.method_3137();
         this.openCloseAnimation.setState(false);
      } else {
         this.toggle();
      }
   }

   public int getColor() {
      return Modules.COLORS.getColor((int)(100.0D * this.openCloseAnimation.getFactor())).getRGB();
   }

   public int getColor1() {
      return Modules.COLORS.getColor((int)(100.0D * this.openCloseAnimation.getFactor())).getRGB();
   }

   public int getColor(float alpha) {
      return Modules.COLORS.getColor((int)((double)(100.0F * alpha) * this.openCloseAnimation.getFactor())).getRGB();
   }

   public int getColor1(float alpha) {
      return Modules.COLORS.getColor((int)((double)(100.0F * alpha) * this.openCloseAnimation.getFactor())).getRGB();
   }

   public Float getScale() {
      return this.scaleConfig;
   }
}
