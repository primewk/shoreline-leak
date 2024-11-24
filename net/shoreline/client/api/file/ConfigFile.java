package net.shoreline.client.api.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.shoreline.client.Shoreline;

public abstract class ConfigFile {
   protected static final Gson GSON = (new GsonBuilder()).setLenient().setPrettyPrinting().create();
   private final String fileName;
   private final Path filepath;

   public ConfigFile(Path dir, String path) {
      if (!Files.exists(dir, new LinkOption[0])) {
         try {
            Files.createDirectory(dir);
         } catch (IOException var4) {
            Shoreline.error("Could not create {} dir", dir);
            var4.printStackTrace();
         }
      }

      this.fileName = dir.getFileName().toString();
      this.filepath = dir.resolve(this.toJsonPath(path));
   }

   protected String read(Path path) throws IOException {
      StringBuilder content = new StringBuilder();
      InputStream in = Files.newInputStream(path);

      int b;
      while((b = in.read()) != -1) {
         content.append((char)b);
      }

      in.close();
      return content.toString();
   }

   protected String serialize(Object obj) {
      return GSON.toJson(obj);
   }

   protected JsonObject parseObject(String json) {
      return (JsonObject)this.parse(json, JsonObject.class);
   }

   protected JsonArray parseArray(String json) {
      return (JsonArray)this.parse(json, JsonArray.class);
   }

   protected <T> T parse(String json, Class<T> type) {
      try {
         return GSON.fromJson(json, type);
      } catch (JsonSyntaxException var4) {
         Shoreline.error("Invalid json syntax!");
         var4.printStackTrace();
         return null;
      }
   }

   protected void write(Path path, String content) throws IOException {
      OutputStream out = Files.newOutputStream(path);
      byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
      out.write(bytes, 0, bytes.length);
      out.close();
   }

   public String getFileName() {
      return this.fileName;
   }

   public Path getFilepath() {
      return this.filepath;
   }

   public abstract void save();

   public abstract void load();

   private String toJsonPath(String fileName) {
      return String.format("%s.json", fileName).toLowerCase();
   }
}
