package net.shoreline.client.impl.shaders;

import net.minecraft.class_241;
import net.shoreline.client.api.render.shader.Program;
import net.shoreline.client.api.render.shader.Shader;
import net.shoreline.client.api.render.shader.Uniform;
import org.lwjgl.opengl.GL20;

public class GradientProgram extends Program {
   Uniform<class_241> resolution = new Uniform("resolution");

   public GradientProgram() {
      super(new Shader("gradient.frag", 35632));
   }

   public void initUniforms() {
      this.resolution.init(this.id);
   }

   public void updateUniforms() {
      GL20.glUniform2f(this.resolution.getId(), ((class_241)this.resolution.get()).field_1343, ((class_241)this.resolution.get()).field_1342);
   }

   public void setUniforms(class_241 resolution) {
      this.resolution.set(resolution);
   }
}
