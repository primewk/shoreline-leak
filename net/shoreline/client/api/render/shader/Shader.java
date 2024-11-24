package net.shoreline.client.api.render.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL20C;

public class Shader {
   private final int id;

   public Shader(String shaderName, int shaderMode) {
      this.id = GlStateManager.glCreateShader(shaderMode);

      try {
         GlStateManager.glShaderSource(this.id, loadShader(shaderName));
         GlStateManager.glCompileShader(this.id);
         if (GL20C.glGetShaderi(this.id, 35713) == 0) {
            int var10002 = this.id;
            throw new RuntimeException("Shader compilation error!\n" + GL20C.glGetShaderInfoLog(var10002, GL20C.glGetShaderi(this.id, 35716)));
         }
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   private static List<String> loadShader(String name) throws IOException {
      InputStream stream = Shader.class.getClassLoader().getResourceAsStream("assets/shoreline/shader/" + name);
      if (stream == null) {
         throw new IOException("Shader with name " + name + " not found");
      } else {
         return IOUtils.readLines(stream, Charset.defaultCharset()).stream().map((line) -> {
            return line + "\n";
         }).toList();
      }
   }

   public int getId() {
      return this.id;
   }
}
