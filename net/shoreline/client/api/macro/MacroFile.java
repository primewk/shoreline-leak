package net.shoreline.client.api.macro;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Iterator;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.file.ConfigFile;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.init.Managers;

public class MacroFile extends ConfigFile {
   public MacroFile(Path dir) {
      super(dir, "macros");
   }

   public void save() {
      try {
         Path filepath = this.getFilepath();
         if (!Files.exists(filepath, new LinkOption[0])) {
            Files.createFile(filepath);
         }

         JsonArray object = new JsonArray();
         Iterator var3 = Managers.MACRO.getMacros().iterator();

         while(var3.hasNext()) {
            Macro macro = (Macro)var3.next();
            object.add(macro.toJson());
         }

         this.write(filepath, this.serialize(object));
      } catch (IOException var5) {
         Shoreline.error("Could not save macro file!");
         var5.printStackTrace();
      }

   }

   public void load() {
      try {
         Path filepath = this.getFilepath();
         if (Files.exists(filepath, new LinkOption[0])) {
            String content = this.read(filepath);
            JsonArray object = this.parseArray(content);
            Iterator var4 = object.getAsJsonArray().iterator();

            while(var4.hasNext()) {
               JsonElement element = (JsonElement)var4.next();
               JsonObject jsonObject = element.getAsJsonObject();
               if (jsonObject.has("id")) {
                  String id = jsonObject.get("id").getAsString();
                  Macro macro = Managers.MACRO.getMacro((m) -> {
                     return m.getId().equals(id);
                  });
                  if (macro != null) {
                     macro.fromJson(jsonObject);
                     Module module = Managers.MODULE.getModule(id.substring(0, id.length() - 6));
                     if (module instanceof ToggleModule) {
                        ToggleModule t = (ToggleModule)module;
                        t.keybind(jsonObject.get("value").getAsInt());
                     }
                  }
               }
            }
         }
      } catch (IOException var11) {
         Shoreline.error("Could not read macro file!");
         var11.printStackTrace();
      }

   }
}
