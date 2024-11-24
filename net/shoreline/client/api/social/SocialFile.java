package net.shoreline.client.api.social;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Iterator;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.file.ConfigFile;
import net.shoreline.client.init.Managers;

public class SocialFile extends ConfigFile {
   private final SocialRelation relation;

   public SocialFile(Path dir, SocialRelation relation) {
      super(dir, relation.name());
      this.relation = relation;
   }

   public void save() {
      try {
         Path filepath = this.getFilepath();
         if (!Files.exists(filepath, new LinkOption[0])) {
            Files.createFile(filepath);
         }

         JsonArray array = new JsonArray();
         Iterator var3 = Managers.SOCIAL.getRelations(this.relation).iterator();

         while(var3.hasNext()) {
            String socials = (String)var3.next();
            array.add(new JsonPrimitive(socials));
         }

         this.write(filepath, this.serialize(array));
      } catch (IOException var5) {
         Shoreline.error("Could not save file for {}.json!", this.relation.name().toLowerCase());
         var5.printStackTrace();
      }

   }

   public void load() {
      try {
         Path filepath = this.getFilepath();
         if (Files.exists(filepath, new LinkOption[0])) {
            String content = this.read(filepath);
            JsonArray json = this.parseArray(content);
            if (json == null) {
               return;
            }

            Iterator var4 = json.asList().iterator();

            while(var4.hasNext()) {
               JsonElement element = (JsonElement)var4.next();
               Managers.SOCIAL.addRelation(element.getAsString(), this.relation);
            }
         }
      } catch (IOException var6) {
         Shoreline.error("Could not read file for {}.json!", this.relation.name().toLowerCase());
         var6.printStackTrace();
      }

   }
}
