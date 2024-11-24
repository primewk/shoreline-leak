package net.shoreline.client.impl.gui.click.component;

public abstract class Button extends Component implements Interactable {
   protected final Frame frame;

   public Button(Frame frame, float x, float y, float width, float height) {
      this.frame = frame;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public Button(Frame frame) {
      this.frame = frame;
   }

   public abstract void mouseClicked(double var1, double var3, int var5);

   public abstract void mouseReleased(double var1, double var3, int var5);

   public abstract void keyPressed(int var1, int var2, int var3);

   public Frame getFrame() {
      return this.frame;
   }
}
