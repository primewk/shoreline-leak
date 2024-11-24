package net.shoreline.client.api.render.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import org.jetbrains.annotations.NotNull;

public abstract class Program {
   protected final int id = GlStateManager.glCreateProgram();
   private boolean isInitialisedUniforms = false;

   public Program(@NotNull Shader... shaders) {
      Shader[] var2 = shaders;
      int var3 = shaders.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Shader shader = var2[var4];
         GlStateManager.glAttachShader(this.id, shader.getId());
         GlStateManager.glLinkProgram(this.id);
         GlStateManager.glDeleteShader(shader.getId());
      }

   }

   public abstract void initUniforms();

   public abstract void updateUniforms();

   public void use() {
      GlStateManager._glUseProgram(this.id);
      if (!this.isInitialisedUniforms) {
         this.initUniforms();
         this.isInitialisedUniforms = true;
      }

      this.updateUniforms();
   }

   public void stopUse() {
      GlStateManager._glUseProgram(0);
   }

   public int getId() {
      return this.id;
   }
}
