package net.shoreline.client.impl.gui.click.component;

public interface Interactable extends Drawable {
   void mouseClicked(double var1, double var3, int var5);

   void mouseReleased(double var1, double var3, int var5);

   void keyPressed(int var1, int var2, int var3);
}
