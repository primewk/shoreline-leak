package net.shoreline.client.api.waypoint;

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
import net.shoreline.client.init.Managers;

public class WaypointFile extends ConfigFile {
   private final String serverIp;

   public WaypointFile(Path dir, String serverIp) {
      super(dir, serverIp);
      this.serverIp = serverIp;
   }

   public void save() {
      try {
         Path filepath = this.getFilepath();
         if (!Files.exists(filepath, new LinkOption[0])) {
            Files.createFile(filepath);
         }

         JsonArray array = new JsonArray();
         Iterator var3 = Managers.WAYPOINT.getWaypoints().iterator();

         while(var3.hasNext()) {
            Waypoint point = (Waypoint)var3.next();
            if (point.getIp().equalsIgnoreCase(this.serverIp)) {
               JsonObject obj = new JsonObject();
               obj.addProperty("tag", point.getName());
               obj.addProperty("x", point.method_10216());
               obj.addProperty("y", point.method_10214());
               obj.addProperty("z", point.method_10215());
               array.add(obj);
            }
         }

         this.write(filepath, this.serialize(array));
      } catch (IOException var6) {
         Shoreline.error("Could not save file for {}.json!", this.serverIp);
         var6.printStackTrace();
      }

   }

   public void load() {
      try {
         Path filepath = this.getFilepath();
         if (Files.exists(filepath, new LinkOption[0])) {
            String content = this.read(filepath);
            JsonArray array = this.parseArray(content);
            if (array == null) {
               return;
            }

            Iterator var4 = array.asList().iterator();

            while(var4.hasNext()) {
               JsonElement e = (JsonElement)var4.next();
               JsonObject obj = e.getAsJsonObject();
               JsonElement tag = obj.get("tag");
               JsonElement x = obj.get("x");
               JsonElement y = obj.get("y");
               JsonElement z = obj.get("z");
               Managers.WAYPOINT.register(new Waypoint(tag.getAsString(), this.serverIp, x.getAsDouble(), y.getAsDouble(), z.getAsDouble()));
            }
         }
      } catch (IOException var11) {
         Shoreline.error("Could not read file for {}.json!", this.serverIp);
         var11.printStackTrace();
      }

   }
}
