package net.shoreline.client.impl.gui.click.component;

import net.minecraft.class_332;

public class Frame extends Component implements Interactable {
   protected float px;
   protected float py;
   protected float fheight;

   public Frame(float x, float y, float width, float height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void render(class_332 context, float mouseX, float mouseY, float delta) {
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }
}
