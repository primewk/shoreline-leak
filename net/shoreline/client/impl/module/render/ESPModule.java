package net.shoreline.client.impl.module.render;

import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2611;
import net.minecraft.class_2614;
import net.minecraft.class_2627;
import net.minecraft.class_3866;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.EntityOutlineEvent;
import net.shoreline.client.impl.event.entity.decoration.TeamColorEvent;
import net.shoreline.client.util.world.EntityUtil;

public class ESPModule extends ToggleModule {
   Config<ESPModule.ESPMode> modeConfig;
   Config<Float> widthConfig;
   Config<Boolean> playersConfig;
   Config<Boolean> selfConfig;
   Config<Color> playersColorConfig;
   Config<Boolean> monstersConfig;
   Config<Color> monstersColorConfig;
   Config<Boolean> animalsConfig;
   Config<Color> animalsColorConfig;
   Config<Boolean> vehiclesConfig;
   Config<Color> vehiclesColorConfig;
   Config<Boolean> itemsConfig;
   Config<Color> itemsColorConfig;
   Config<Boolean> crystalsConfig;
   Config<Color> crystalsColorConfig;
   Config<Boolean> chestsConfig;
   Config<Color> chestsColorConfig;
   Config<Boolean> echestsConfig;
   Config<Color> echestsColorConfig;
   Config<Boolean> shulkersConfig;
   Config<Color> shulkersColorConfig;
   Config<Boolean> hoppersConfig;
   Config<Color> hoppersColorConfig;
   Config<Boolean> furnacesConfig;
   Config<Color> furnacesColorConfig;

   public ESPModule() {
      super("ESP", "See entities and objects through walls", ModuleCategory.RENDER);
      this.modeConfig = new EnumConfig("Mode", "ESP rendering mode", ESPModule.ESPMode.GLOW, ESPModule.ESPMode.values());
      this.widthConfig = new NumberConfig("Linewidth", "ESP rendering line width", 0.1F, 1.25F, 5.0F);
      this.playersConfig = new BooleanConfig("Players", "Render players through walls", true);
      this.selfConfig = new BooleanConfig("Self", "Render self through walls", true);
      this.playersColorConfig = new ColorConfig("PlayersColor", "The render color for players", new Color(200, 60, 60), false, () -> {
         return (Boolean)this.playersConfig.getValue() || (Boolean)this.selfConfig.getValue();
      });
      this.monstersConfig = new BooleanConfig("Monsters", "Render monsters through walls", true);
      this.monstersColorConfig = new ColorConfig("MonstersColor", "The render color for monsters", new Color(200, 60, 60), false, () -> {
         return (Boolean)this.monstersConfig.getValue();
      });
      this.animalsConfig = new BooleanConfig("Animals", "Render animals through walls", true);
      this.animalsColorConfig = new ColorConfig("AnimalsColor", "The render color for animals", new Color(0, 200, 0), false, () -> {
         return (Boolean)this.animalsConfig.getValue();
      });
      this.vehiclesConfig = new BooleanConfig("Vehicles", "Render vehicles through walls", false);
      this.vehiclesColorConfig = new ColorConfig("VehiclesColor", "The render color for vehicles", new Color(200, 100, 0), false, () -> {
         return (Boolean)this.vehiclesConfig.getValue();
      });
      this.itemsConfig = new BooleanConfig("Items", "Render dropped items through walls", false);
      this.itemsColorConfig = new ColorConfig("ItemsColor", "The render color for items", new Color(200, 100, 0), false, () -> {
         return (Boolean)this.itemsConfig.getValue();
      });
      this.crystalsConfig = new BooleanConfig("EndCrystals", "Render end crystals through walls", false);
      this.crystalsColorConfig = new ColorConfig("EndCrystalsColor", "The render color for end crystals", new Color(200, 100, 200), false, () -> {
         return (Boolean)this.crystalsConfig.getValue();
      });
      this.chestsConfig = new BooleanConfig("Chests", "Render players through walls", true);
      this.chestsColorConfig = new ColorConfig("ChestsColor", "The render color for chests", new Color(200, 200, 101), false, () -> {
         return (Boolean)this.chestsConfig.getValue();
      });
      this.echestsConfig = new BooleanConfig("EnderChests", "Render players through walls", true);
      this.echestsColorConfig = new ColorConfig("EnderChestsColor", "The render color for ender chests", new Color(155, 0, 200), false, () -> {
         return (Boolean)this.echestsConfig.getValue();
      });
      this.shulkersConfig = new BooleanConfig("Shulkers", "Render players through walls", true);
      this.shulkersColorConfig = new ColorConfig("ShulkersColor", "The render color for shulkers", new Color(200, 0, 106), false, () -> {
         return (Boolean)this.shulkersConfig.getValue();
      });
      this.hoppersConfig = new BooleanConfig("Hoppers", "Render players through walls", false);
      this.hoppersColorConfig = new ColorConfig("HoppersColor", "The render color for hoppers", new Color(100, 100, 100), false, () -> {
         return (Boolean)this.hoppersConfig.getValue();
      });
      this.furnacesConfig = new BooleanConfig("Furnaces", "Render players through walls", false);
      this.furnacesColorConfig = new ColorConfig("FurnacesColor", "The render color for furnaces", new Color(100, 100, 100), () -> {
         return (Boolean)this.furnacesConfig.getValue();
      });
   }

   @EventListener
   public void onEntityOutline(EntityOutlineEvent event) {
      if (this.modeConfig.getValue() == ESPModule.ESPMode.GLOW && this.checkESP(event.getEntity())) {
         event.cancel();
      }

   }

   @EventListener
   public void onTeamColor(TeamColorEvent event) {
      if (this.modeConfig.getValue() == ESPModule.ESPMode.GLOW && this.checkESP(event.getEntity())) {
         event.cancel();
         event.setColor(this.getESPColor(event.getEntity()).getRGB());
      }

   }

   public Color getStorageESPColor(class_2586 tileEntity) {
      if (tileEntity instanceof class_2595) {
         return (Color)this.chestsColorConfig.getValue();
      } else if (tileEntity instanceof class_2611) {
         return (Color)this.echestsColorConfig.getValue();
      } else if (tileEntity instanceof class_2627) {
         return (Color)this.shulkersColorConfig.getValue();
      } else if (tileEntity instanceof class_2614) {
         return (Color)this.hoppersColorConfig.getValue();
      } else {
         return tileEntity instanceof class_3866 ? (Color)this.furnacesColorConfig.getValue() : null;
      }
   }

   public Color getESPColor(class_1297 entity) {
      if (entity instanceof class_1657) {
         return (Color)this.playersColorConfig.getValue();
      } else if (EntityUtil.isMonster(entity)) {
         return (Color)this.monstersColorConfig.getValue();
      } else if (!EntityUtil.isNeutral(entity) && !EntityUtil.isPassive(entity)) {
         if (EntityUtil.isVehicle(entity)) {
            return (Color)this.vehiclesColorConfig.getValue();
         } else if (entity instanceof class_1511) {
            return (Color)this.crystalsColorConfig.getValue();
         } else {
            return entity instanceof class_1542 ? (Color)this.itemsColorConfig.getValue() : null;
         }
      } else {
         return (Color)this.animalsColorConfig.getValue();
      }
   }

   public boolean checkESP(class_1297 entity) {
      if (entity instanceof class_1657 && (Boolean)this.playersConfig.getValue()) {
         return (Boolean)this.selfConfig.getValue() || entity != mc.field_1724;
      } else {
         return EntityUtil.isMonster(entity) && (Boolean)this.monstersConfig.getValue() || (EntityUtil.isNeutral(entity) || EntityUtil.isPassive(entity)) && (Boolean)this.animalsConfig.getValue() || EntityUtil.isVehicle(entity) && (Boolean)this.vehiclesConfig.getValue() || entity instanceof class_1511 && (Boolean)this.crystalsConfig.getValue() || entity instanceof class_1542 && (Boolean)this.itemsConfig.getValue();
      }
   }

   public static enum ESPMode {
      GLOW;

      // $FF: synthetic method
      private static ESPModule.ESPMode[] $values() {
         return new ESPModule.ESPMode[]{GLOW};
      }
   }
}
