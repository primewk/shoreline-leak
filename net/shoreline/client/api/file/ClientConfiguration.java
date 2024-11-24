package net.shoreline.client.api.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.macro.MacroFile;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.api.module.file.ModuleConfigFile;
import net.shoreline.client.api.module.file.ModuleFile;
import net.shoreline.client.api.social.SocialFile;
import net.shoreline.client.api.social.SocialRelation;
import net.shoreline.client.api.waypoint.WaypointFile;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.Globals;

public class ClientConfiguration implements Globals {
   private final Set<ConfigFile> files = new HashSet();
   private Path clientDir;

   public ClientConfiguration() {
      Path runningDir = mc.field_1697.toPath();
      boolean var15 = false;

      label226: {
         Path configDir;
         label227: {
            try {
               var15 = true;
               File homeDir = new File(System.getProperty("user.home"));
               this.clientDir = homeDir.toPath();
               var15 = false;
               break label227;
            } catch (Exception var22) {
               Shoreline.error("Could not access home dir, defaulting to running dir");
               var22.printStackTrace();
               this.clientDir = runningDir;
               var15 = false;
            } finally {
               if (var15) {
                  if (this.clientDir == null || !Files.exists(this.clientDir, new LinkOption[0]) || !Files.isWritable(this.clientDir)) {
                     this.clientDir = runningDir;
                  }

                  this.clientDir = this.clientDir.resolve("Shoreline");
                  if (!Files.exists(this.clientDir, new LinkOption[0])) {
                     try {
                        Files.createDirectory(this.clientDir);
                     } catch (IOException var17) {
                        Shoreline.error("Could not create client dir");
                        var17.printStackTrace();
                     }
                  }

                  Path configDir = this.clientDir.resolve("Configs");
                  if (!Files.exists(configDir, new LinkOption[0])) {
                     try {
                        Files.createDirectory(configDir);
                     } catch (IOException var16) {
                        Shoreline.error("Could not create config dir");
                        var16.printStackTrace();
                     }
                  }

               }
            }

            if (this.clientDir == null || !Files.exists(this.clientDir, new LinkOption[0]) || !Files.isWritable(this.clientDir)) {
               this.clientDir = runningDir;
            }

            this.clientDir = this.clientDir.resolve("Shoreline");
            if (!Files.exists(this.clientDir, new LinkOption[0])) {
               try {
                  Files.createDirectory(this.clientDir);
               } catch (IOException var19) {
                  Shoreline.error("Could not create client dir");
                  var19.printStackTrace();
               }
            }

            configDir = this.clientDir.resolve("Configs");
            if (!Files.exists(configDir, new LinkOption[0])) {
               try {
                  Files.createDirectory(configDir);
               } catch (IOException var18) {
                  Shoreline.error("Could not create config dir");
                  var18.printStackTrace();
               }
            }
            break label226;
         }

         if (this.clientDir == null || !Files.exists(this.clientDir, new LinkOption[0]) || !Files.isWritable(this.clientDir)) {
            this.clientDir = runningDir;
         }

         this.clientDir = this.clientDir.resolve("Shoreline");
         if (!Files.exists(this.clientDir, new LinkOption[0])) {
            try {
               Files.createDirectory(this.clientDir);
            } catch (IOException var21) {
               Shoreline.error("Could not create client dir");
               var21.printStackTrace();
            }
         }

         configDir = this.clientDir.resolve("Configs");
         if (!Files.exists(configDir, new LinkOption[0])) {
            try {
               Files.createDirectory(configDir);
            } catch (IOException var20) {
               Shoreline.error("Could not create config dir");
               var20.printStackTrace();
            }
         }
      }

      this.files.add(new MacroFile(this.clientDir));
      Iterator var25 = Managers.MODULE.getModules().iterator();

      while(var25.hasNext()) {
         Module module = (Module)var25.next();
         this.files.add(new ModuleFile(this.clientDir.resolve("Modules"), module));
      }

      this.files.add(Modules.INV_CLEANER.getBlacklistFile(this.clientDir));
      SocialRelation[] var26 = SocialRelation.values();
      int var27 = var26.length;

      for(int var4 = 0; var4 < var27; ++var4) {
         SocialRelation relation = var26[var4];
         this.files.add(new SocialFile(this.clientDir, relation));
      }

   }

   public void saveClient() {
      Iterator var1 = Managers.WAYPOINT.getIps().iterator();

      while(var1.hasNext()) {
         String ip = (String)var1.next();
         this.files.add(new WaypointFile(this.clientDir.resolve("Waypoints"), ip));
      }

      var1 = this.files.iterator();

      while(var1.hasNext()) {
         ConfigFile file = (ConfigFile)var1.next();
         file.save();
      }

   }

   public void loadClient() {
      Iterator var1 = this.files.iterator();

      while(var1.hasNext()) {
         ConfigFile file = (ConfigFile)var1.next();
         file.load();
      }

   }

   public void saveModuleConfiguration(String configFile) {
      ModuleConfigFile file = new ModuleConfigFile(this.clientDir.resolve("Configs"), configFile);
      file.save();
   }

   public void loadModuleConfiguration(String configFile) {
      ModuleConfigFile file = new ModuleConfigFile(this.clientDir.resolve("Configs"), configFile);
      file.load();
   }

   public Set<ConfigFile> getFiles() {
      return this.files;
   }

   public void addFile(ConfigFile configFile) {
      this.files.add(configFile);
   }

   public void removeFile(ConfigFile configFile) {
      this.files.remove(configFile);
   }

   public Path getClientDirectory() {
      return this.clientDir;
   }
}
