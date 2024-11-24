package net.shoreline.client.api.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.Identifiable;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.ItemListConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.config.setting.StringConfig;
import net.shoreline.client.api.config.setting.ToggleConfig;
import net.shoreline.client.api.macro.Macro;
import net.shoreline.client.util.Globals;

public class ConfigContainer implements Identifiable, Serializable<Config<?>>, Globals {
   protected final String name;
   private final Map<String, Config<?>> configurations = Collections.synchronizedMap(new LinkedHashMap());

   public ConfigContainer(String name) {
      this.name = name;
   }

   protected void register(Config<?> config) {
      config.setContainer(this);
      this.configurations.put(config.getId(), config);
   }

   protected void register(Config<?>... configs) {
      Config[] var2 = configs;
      int var3 = configs.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Config<?> config = var2[var4];
         this.register(config);
      }

   }

   protected void unregister(Config<?> config) {
      this.configurations.remove(config.getId());
   }

   protected void unregister(Config<?>... configs) {
      Config[] var2 = configs;
      int var3 = configs.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Config<?> config = var2[var4];
         this.unregister(config);
      }

   }

   public void reflectConfigs() {
      ConfigFactory factory = new ConfigFactory(this);
      Field[] var2 = this.getClass().getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (Config.class.isAssignableFrom(field.getType())) {
            Config<?> config = factory.build(field);
            if (config == null) {
               Shoreline.error("Value for field {} is null!", field);
            } else {
               this.register(config);
            }
         }
      }

   }

   public JsonObject toJson() {
      JsonObject out = new JsonObject();
      out.addProperty("name", this.getName());
      out.addProperty("id", this.getId());
      JsonArray array = new JsonArray();
      Iterator var3 = this.getConfigs().iterator();

      while(var3.hasNext()) {
         Config<?> config = (Config)var3.next();
         if (!(config.getValue() instanceof Macro)) {
            array.add(config.toJson());
         }
      }

      out.add("configs", array);
      return out;
   }

   public Config<?> fromJson(JsonObject jsonObj) {
      if (jsonObj.has("configs")) {
         JsonElement element = jsonObj.get("configs");
         if (!element.isJsonArray()) {
            return null;
         }

         Iterator var3 = element.getAsJsonArray().iterator();

         while(var3.hasNext()) {
            JsonElement je = (JsonElement)var3.next();
            if (je.isJsonObject()) {
               JsonObject configObj = je.getAsJsonObject();
               JsonElement id = configObj.get("id");
               Config<?> config = this.getConfig(id.getAsString());
               if (config != null) {
                  try {
                     Boolean val;
                     if (config instanceof ToggleConfig) {
                        ToggleConfig cfg = (ToggleConfig)config;
                        val = cfg.fromJson(configObj);
                        if (mc.field_1687 != null) {
                           if (val) {
                              cfg.enable();
                           } else {
                              cfg.disable();
                           }
                        } else {
                           cfg.setValue(val);
                        }
                     } else if (config instanceof BooleanConfig) {
                        BooleanConfig cfg = (BooleanConfig)config;
                        val = cfg.fromJson(configObj);
                        cfg.setValue(val);
                     } else if (config instanceof ColorConfig) {
                        ColorConfig cfg = (ColorConfig)config;
                        Color val = cfg.fromJson(configObj);
                        cfg.setValue(val);
                     } else if (config instanceof EnumConfig) {
                        EnumConfig cfg = (EnumConfig)config;
                        Enum<?> val = cfg.fromJson(configObj);
                        if (val != null) {
                           cfg.setValue(val);
                        }
                     } else if (config instanceof ItemListConfig) {
                        ItemListConfig cfg = (ItemListConfig)config;
                        List<?> val = cfg.fromJson(configObj);
                        cfg.setValue(val);
                     } else if (config instanceof NumberConfig) {
                        NumberConfig cfg = (NumberConfig)config;
                        Number val = cfg.fromJson(configObj);
                        cfg.setValue(val);
                     } else if (config instanceof StringConfig) {
                        StringConfig cfg = (StringConfig)config;
                        String val = cfg.fromJson(configObj);
                        cfg.setValue(val);
                     }
                  } catch (Exception var16) {
                     Shoreline.error("Couldn't parse Json for {}!", config.getName());
                     var16.printStackTrace();
                  }
               }
            }
         }
      }

      return null;
   }

   public String getName() {
      return this.name;
   }

   public String getId() {
      return String.format("%s-container", this.name.toLowerCase());
   }

   public Config<?> getConfig(String id) {
      return (Config)this.configurations.get(id);
   }

   public Collection<Config<?>> getConfigs() {
      return this.configurations.values();
   }
}
