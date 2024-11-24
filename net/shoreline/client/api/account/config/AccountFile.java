package net.shoreline.client.api.account.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Iterator;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.account.type.MinecraftAccount;
import net.shoreline.client.api.account.type.impl.CrackedAccount;
import net.shoreline.client.api.account.type.impl.MicrosoftAccount;
import net.shoreline.client.api.file.ConfigFile;
import net.shoreline.client.init.Managers;

public class AccountFile extends ConfigFile {
   public AccountFile(Path dir, String path) {
      super(dir, path);
   }

   public AccountFile(Path dir) {
      this(dir, "accounts");
   }

   public void save() {
      try {
         Path filepath = this.getFilepath();
         if (!Files.exists(filepath, new LinkOption[0])) {
            Files.createFile(filepath);
         }

         this.write(filepath, this.saveAs());
      } catch (IOException var2) {
         Shoreline.error("Could not save file for accounts.json!");
         var2.printStackTrace();
      }

   }

   public void load() {
      try {
         Path filepath = this.getFilepath();
         if (Files.exists(filepath, new LinkOption[0])) {
            this.readFrom(this.read(filepath));
         }
      } catch (IOException var2) {
         Shoreline.error("Could not read file for accounts.json!");
         var2.printStackTrace();
      }

   }

   protected String saveAs() {
      JsonArray array = new JsonArray();
      Iterator var2 = Managers.ACCOUNT.getAccounts().iterator();

      while(var2.hasNext()) {
         MinecraftAccount account = (MinecraftAccount)var2.next();

         try {
            array.add(account.toJSON());
         } catch (RuntimeException var5) {
            Shoreline.error(var5.getMessage());
         }
      }

      return this.serialize(array);
   }

   protected void readFrom(String content) {
      JsonArray json = this.parseArray(content);
      if (json != null) {
         Iterator var3 = json.asList().iterator();

         while(true) {
            while(true) {
               JsonElement element;
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  element = (JsonElement)var3.next();
               } while(!(element instanceof JsonObject));

               JsonObject object = (JsonObject)element;
               MinecraftAccount account = null;
               if (object.has("email") && object.has("password")) {
                  account = new MicrosoftAccount(object.get("email").getAsString(), object.get("password").getAsString());
                  if (object.has("username")) {
                     ((MicrosoftAccount)account).setUsername(object.get("username").getAsString());
                  }
               } else if (object.has("token")) {
                  if (!object.has("username")) {
                     Shoreline.error("Browser account does not have a username set?");
                     continue;
                  }

                  account = new MicrosoftAccount(object.get("token").getAsString());
                  ((MicrosoftAccount)account).setUsername(object.get("username").getAsString());
               } else if (object.has("username")) {
                  account = new CrackedAccount(object.get("username").getAsString());
               }

               if (account != null) {
                  Managers.ACCOUNT.register((MinecraftAccount)account);
               } else {
                  Shoreline.error("Could not parse account JSON.\nRaw: {}", object.toString());
               }
            }
         }
      }
   }
}
