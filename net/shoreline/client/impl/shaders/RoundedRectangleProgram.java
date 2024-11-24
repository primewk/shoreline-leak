package net.shoreline.client.impl.shaders;

import java.awt.Color;
import net.minecraft.class_241;
import net.shoreline.client.api.render.shader.Program;
import net.shoreline.client.api.render.shader.Shader;
import net.shoreline.client.api.render.shader.Uniform;
import net.shoreline.client.util.Globals;
import org.lwjgl.opengl.GL20;

public final class RoundedRectangleProgram extends Program implements Globals {
   Uniform<Float> radius = new Uniform("radius");
   Uniform<Float> softness = new Uniform("softness");
   Uniform<class_241> size = new Uniform("size");
   Uniform<Color> color = new Uniform("color");

   public RoundedRectangleProgram() {
      super(new Shader("roundedrect.frag", 35632));
   }

   public void initUniforms() {
      this.radius.init(this.id);
      this.softness.init(this.id);
      this.size.init(this.id);
      this.color.init(this.id);
   }

   public void updateUniforms() {
      float SCALE_FACTOR = (float)mc.method_22683().method_4495();
      GL20.glUniform2f(this.size.getId(), ((class_241)this.size.get()).field_1343 * SCALE_FACTOR, ((class_241)this.size.get()).field_1342 * SCALE_FACTOR);
      GL20.glUniform4f(this.color.getId(), (float)((Color)this.color.get()).getRed() / 255.0F, (float)((Color)this.color.get()).getGreen() / 255.0F, (float)((Color)this.color.get()).getBlue() / 255.0F, (float)((Color)this.color.get()).getAlpha() / 255.0F);
      GL20.glUniform1f(this.radius.getId(), (Float)this.radius.get());
      GL20.glUniform1f(this.softness.getId(), (Float)this.softness.get());
   }

   public void setDimensions(float width, float height) {
      this.size.set(new class_241(width, height));
   }

   public void setColor(Color rectColor) {
      this.color.set(rectColor);
   }

   public void setRadius(float rectRadius) {
      this.radius.set(rectRadius);
   }

   public void setSoftness(float edgeSoftness) {
      this.softness.set(edgeSoftness);
   }
}
