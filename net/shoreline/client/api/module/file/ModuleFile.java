package net.shoreline.client.api.module.file;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.file.ConfigFile;
import net.shoreline.client.api.module.Module;

public class ModuleFile extends ConfigFile {
   private final Module module;

   public ModuleFile(Path dir, Module module) {
      super(dir, module.getId());
      this.module = module;
   }

   public void save() {
      try {
         Path filepath = this.getFilepath();
         if (!Files.exists(filepath, new LinkOption[0])) {
            Files.createFile(filepath);
         }

         JsonObject json = this.module.toJson();
         this.write(filepath, this.serialize(json));
      } catch (IOException var3) {
         Shoreline.error("Could not save file for {}!", this.module.getName());
         var3.printStackTrace();
      }

   }

   public void load() {
      try {
         Path filepath = this.getFilepath();
         if (Files.exists(filepath, new LinkOption[0])) {
            String content = this.read(filepath);
            this.module.fromJson(this.parseObject(content));
         }
      } catch (IOException var3) {
         Shoreline.error("Could not read file for {}!", this.module.getName());
         var3.printStackTrace();
      }

   }
}
