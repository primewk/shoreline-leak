package net.shoreline.client.api.font;

@FunctionalInterface
public interface GlyphVisitor {
   int draw(FontRenderer var1, char var2, double var3, double var5, int var7, boolean var8);
}
