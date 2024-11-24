package net.shoreline.client.api.config.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1792;
import net.minecraft.class_2960;
import net.minecraft.class_7923;
import net.shoreline.client.api.config.Config;

public class ItemListConfig<T extends List<class_1792>> extends Config<T> {
   public ItemListConfig(String name, String desc, class_1792... values) {
      super(name, desc, List.of(values));
   }

   public boolean contains(Object obj) {
      return obj instanceof class_1792 ? ((List)this.value).contains(obj) : false;
   }

   public JsonObject toJson() {
      JsonObject jsonObj = super.toJson();
      JsonArray array = new JsonArray();
      Iterator var3 = ((List)this.getValue()).iterator();

      while(var3.hasNext()) {
         class_1792 item = (class_1792)var3.next();
         class_2960 id = class_7923.field_41178.method_10221(item);
         array.add(id.toString());
      }

      jsonObj.add("value", array);
      return jsonObj;
   }

   public T fromJson(JsonObject jsonObj) {
      if (!jsonObj.has("value")) {
         return null;
      } else {
         JsonElement element = jsonObj.get("value");
         List<class_1792> temp = new ArrayList();
         Iterator var4 = element.getAsJsonArray().iterator();

         while(var4.hasNext()) {
            JsonElement je = (JsonElement)var4.next();
            String val = je.getAsString();
            class_1792 item = (class_1792)class_7923.field_41178.method_10223(new class_2960(val));
            temp.add(item);
         }

         return temp;
      }
   }
}
