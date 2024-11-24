package net.shoreline.client.api.account.type;

import com.google.gson.JsonObject;
import net.minecraft.class_320;

public interface MinecraftAccount {
   class_320 login();

   String username();

   default JsonObject toJSON() {
      JsonObject object = new JsonObject();
      object.addProperty("username", this.username());
      return object;
   }
}
