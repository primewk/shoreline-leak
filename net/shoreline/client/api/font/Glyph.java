package net.shoreline.client.api.font;

public record Glyph(char c, int textureX, int textureY, int width, int height) {
   public Glyph(char c, int textureX, int textureY, int width, int height) {
      this.c = c;
      this.textureX = textureX;
      this.textureY = textureY;
      this.width = width;
      this.height = height;
   }

   public char c() {
      return this.c;
   }

   public int textureX() {
      return this.textureX;
   }

   public int textureY() {
      return this.textureY;
   }

   public int width() {
      return this.width;
   }

   public int height() {
      return this.height;
   }
}
