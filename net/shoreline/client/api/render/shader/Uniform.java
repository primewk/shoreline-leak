package net.shoreline.client.api.render.shader;

import com.mojang.blaze3d.platform.GlStateManager;

public class Uniform<T> {
   private final String name;
   private int id;
   private T value;

   public Uniform(String name) {
      this.name = name;
   }

   public void init(int programId) {
      this.id = GlStateManager._glGetUniformLocation(programId, this.name);
   }

   public void set(T value) {
      this.value = value;
   }

   public T get() {
      return this.value;
   }

   public int getId() {
      return this.id;
   }
}
