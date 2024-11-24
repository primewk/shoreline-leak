package net.shoreline.client.impl.gui.click;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.impl.gui.click.component.ScissorStack;
import net.shoreline.client.impl.gui.click.impl.config.CategoryFrame;
import net.shoreline.client.impl.gui.click.impl.config.ModuleButton;
import net.shoreline.client.impl.gui.click.impl.config.setting.BindButton;
import net.shoreline.client.impl.gui.click.impl.config.setting.ConfigButton;
import net.shoreline.client.impl.module.client.ClickGuiModule;
import net.shoreline.client.util.Globals;

public class ClickGuiScreen extends class_437 implements Globals {
   public static int MOUSE_X;
   public static int MOUSE_Y;
   public static boolean MOUSE_RIGHT_CLICK;
   public static boolean MOUSE_RIGHT_HOLD;
   public static boolean MOUSE_LEFT_CLICK;
   public static boolean MOUSE_LEFT_HOLD;
   public static final ScissorStack SCISSOR_STACK = new ScissorStack();
   private final List<CategoryFrame> frames = new CopyOnWriteArrayList();
   private final ClickGuiModule module;
   private CategoryFrame focus;
   private boolean closeOnEscape = true;

   public ClickGuiScreen(ClickGuiModule module) {
      super(class_2561.method_43470("ClickGui"));
      this.module = module;
      float x = 2.0F;
      ModuleCategory[] var3 = ModuleCategory.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ModuleCategory category = var3[var5];
         CategoryFrame frame = new CategoryFrame(category, x, 15.0F);
         this.frames.add(frame);
         x += frame.getWidth() + 2.0F;
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      Iterator var5 = this.frames.iterator();

      while(true) {
         CategoryFrame frame;
         float scale;
         do {
            if (!var5.hasNext()) {
               MOUSE_LEFT_CLICK = false;
               MOUSE_RIGHT_CLICK = false;
               MOUSE_X = mouseX;
               MOUSE_Y = mouseY;
               return;
            }

            frame = (CategoryFrame)var5.next();
            if (frame.isWithinTotal((float)mouseX, (float)mouseY)) {
               this.focus = frame;
            }

            if (frame.isWithin((float)mouseX, (float)mouseY) && MOUSE_LEFT_HOLD && this.checkDragging()) {
               frame.setDragging(true);
            }

            frame.render(context, (float)mouseX, (float)mouseY, delta);
            scale = this.module.getScale();
         } while(scale == 1.0F);

         frame.setDimensions(frame.getWidth() * scale, frame.getHeight() * scale);
         Iterator var8 = frame.getModuleButtons().iterator();

         while(var8.hasNext()) {
            ModuleButton button = (ModuleButton)var8.next();
            button.setDimensions(button.getWidth() * scale, button.getHeight() * scale);

            ConfigButton component;
            for(Iterator var10 = button.getConfigButtons().iterator(); var10.hasNext(); component.setDimensions(component.getWidth() * scale, component.getHeight() * scale)) {
               component = (ConfigButton)var10.next();
               if (component instanceof BindButton) {
                  BindButton bindButton = (BindButton)component;
                  if (bindButton.isListening() && !button.isOpen()) {
                     bindButton.setListening(false);
                     this.setCloseOnEscape(true);
                  }
               }
            }
         }
      }
   }

   public boolean method_25402(double mouseX, double mouseY, int mouseButton) {
      if (mouseButton == 0) {
         MOUSE_LEFT_CLICK = true;
         MOUSE_LEFT_HOLD = true;
      } else if (mouseButton == 1) {
         MOUSE_RIGHT_CLICK = true;
         MOUSE_RIGHT_HOLD = true;
      }

      Iterator var6 = this.frames.iterator();

      while(var6.hasNext()) {
         CategoryFrame frame = (CategoryFrame)var6.next();
         frame.mouseClicked(mouseX, mouseY, mouseButton);
      }

      return super.method_25402(mouseX, mouseY, mouseButton);
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      if (button == 0) {
         MOUSE_LEFT_HOLD = false;
      } else if (button == 1) {
         MOUSE_RIGHT_HOLD = false;
      }

      Iterator var6 = this.frames.iterator();

      while(var6.hasNext()) {
         CategoryFrame frame = (CategoryFrame)var6.next();
         frame.mouseReleased(mouseX, mouseY, button);
      }

      return super.method_25406(mouseX, mouseY, button);
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.focus != null) {
         this.focus.setPos(this.focus.getX(), (float)((double)this.focus.getY() + verticalAmount * 50.0D));
      }

      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 82 && (modifiers & 2) != 0) {
      }

      Iterator var4 = this.frames.iterator();

      while(var4.hasNext()) {
         CategoryFrame frame = (CategoryFrame)var4.next();
         frame.keyPressed(keyCode, scanCode, modifiers);
      }

      return super.method_25404(keyCode, scanCode, modifiers);
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25419() {
      this.module.disable();
      MOUSE_LEFT_CLICK = false;
      MOUSE_LEFT_HOLD = false;
      MOUSE_RIGHT_CLICK = false;
      MOUSE_RIGHT_HOLD = false;
      super.method_25419();
   }

   public boolean method_25422() {
      return this.closeOnEscape;
   }

   private boolean checkDragging() {
      Iterator var1 = this.frames.iterator();

      CategoryFrame frame;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         frame = (CategoryFrame)var1.next();
      } while(!frame.isDragging());

      return false;
   }

   public void setCloseOnEscape(boolean closeOnEscape) {
      this.closeOnEscape = closeOnEscape;
   }
}
