package net.shoreline.client.impl.module.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.ItemListConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.file.ConfigFile;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;

public class InvCleanerModule extends ToggleModule {
   Config<List<class_1792>> blacklistConfig = new ItemListConfig("Blacklist", "The items to throw", new class_1792[0]);
   Config<Float> delayConfig = new NumberConfig("Delay", "The delay between removing items from the inventory", 0.05F, 0.0F, 1.0F);
   Config<Boolean> hotbarConfig = new BooleanConfig("Hotbar", "Cleans the hotbar inventory slots", true);
   private final Timer invCleanTimer = new CacheTimer();

   public InvCleanerModule() {
      super("InvCleaner", "Automatically cleans the player inventory", ModuleCategory.MISCELLANEOUS);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         Iterator var2 = ((List)this.blacklistConfig.getValue()).iterator();

         while(true) {
            while(var2.hasNext()) {
               class_1792 item = (class_1792)var2.next();

               for(int i = 35; i >= ((Boolean)this.hotbarConfig.getValue() ? 0 : 9); --i) {
                  class_1799 stack = mc.field_1724.method_31548().method_5438(i);
                  if (!stack.method_7960() && stack.method_7909() == item && this.invCleanTimer.passed((Float)this.delayConfig.getValue() * 1000.0F)) {
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, i, 0, class_1713.field_7790, mc.field_1724);
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, -999, 0, class_1713.field_7790, mc.field_1724);
                     this.invCleanTimer.reset();
                     break;
                  }
               }
            }

            return;
         }
      }
   }

   public ConfigFile getBlacklistFile(Path clientDir) {
      return new InvCleanerModule.InvCleanerFile(clientDir);
   }

   public class InvCleanerFile extends ConfigFile {
      public InvCleanerFile(Path clientDir) {
         super(clientDir, "inv-cleaner");
      }

      public void save() {
         try {
            Path filepath = this.getFilepath();
            if (!Files.exists(filepath, new LinkOption[0])) {
               Files.createFile(filepath);
            }

            JsonObject json = new JsonObject();
            JsonArray itemArray = new JsonArray();
            Iterator var4 = ((List)InvCleanerModule.this.blacklistConfig.getValue()).iterator();

            while(var4.hasNext()) {
               class_1792 item = (class_1792)var4.next();
               itemArray.add(item.method_7876());
            }

            json.add("items", itemArray);
            this.write(filepath, this.serialize(json));
         } catch (IOException var6) {
            Shoreline.error("Could not save file for inv cleaner!");
            var6.printStackTrace();
         }

      }

      public void load() {
         try {
            Path filepath = this.getFilepath();
            if (Files.exists(filepath, new LinkOption[0])) {
               String content = this.read(filepath);
               JsonObject object = this.parseObject(content);
               if (object != null && object.has("items")) {
                  JsonArray jsonArray = object.getAsJsonArray("items");

                  JsonElement var6;
                  for(Iterator var5 = jsonArray.iterator(); var5.hasNext(); var6 = (JsonElement)var5.next()) {
                  }
               }
            }
         } catch (IOException var7) {
            Shoreline.error("Could not read file for inv cleaner!");
            var7.printStackTrace();
         }

      }
   }
}
