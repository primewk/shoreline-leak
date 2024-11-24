package net.shoreline.client.impl.module.render;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.world.EntityUtil;

public class TracersModule extends ToggleModule {
   Config<Boolean> playersConfig = new BooleanConfig("Players", "Render tracers to player", true);
   Config<Color> playersColorConfig = new ColorConfig("PlayersColor", "The render color for players", new Color(200, 60, 60), false, () -> {
      return (Boolean)this.playersConfig.getValue();
   });
   Config<Boolean> invisiblesConfig = new BooleanConfig("Invisibles", "Render tracers to invisible entities", true);
   Config<Color> invisiblesColorConfig = new ColorConfig("InvisiblesColor", "The render color for invisibles", new Color(200, 100, 0), false, () -> {
      return (Boolean)this.invisiblesConfig.getValue();
   });
   Config<Boolean> monstersConfig = new BooleanConfig("Monsters", "Render tracers to monsters", true);
   Config<Color> monstersColorConfig = new ColorConfig("MonstersColor", "The render color for monsters", new Color(200, 60, 60), false, () -> {
      return (Boolean)this.monstersConfig.getValue();
   });
   Config<Boolean> animalsConfig = new BooleanConfig("Animals", "Render tracers to animals", true);
   Config<Color> animalsColorConfig = new ColorConfig("AnimalsColor", "The render color for animals", new Color(0, 200, 0), false, () -> {
      return (Boolean)this.animalsConfig.getValue();
   });
   Config<Boolean> vehiclesConfig = new BooleanConfig("Vehicles", "Render tracers to vehicles", false);
   Config<Color> vehiclesColorConfig = new ColorConfig("VehiclesColor", "The render color for vehicles", new Color(200, 100, 0), false, () -> {
      return (Boolean)this.vehiclesConfig.getValue();
   });
   Config<Boolean> itemsConfig = new BooleanConfig("Items", "Render tracers to items", false);
   Config<Color> itemsColorConfig = new ColorConfig("ItemsColor", "The render color for items", new Color(255, 255, 255), false, () -> {
      return (Boolean)this.itemsConfig.getValue();
   });
   Config<TracersModule.Target> targetConfig;
   Config<Float> widthConfig;

   public TracersModule() {
      super("Tracers", "Draws a tracer to all entities in render distance", ModuleCategory.RENDER);
      this.targetConfig = new EnumConfig("Target", "The body part of the entity to target", TracersModule.Target.FEET, TracersModule.Target.values());
      this.widthConfig = new NumberConfig("Width", "The line width of the tracer", 1.0F, 1.5F, 10.0F);
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (mc.field_1724 != null) {
         boolean prevBobView = (Boolean)mc.field_1690.method_42448().method_41753();
         mc.field_1690.method_42448().method_41748(false);
         class_4184 cameraPos = mc.field_1773.method_19418();
         class_243 pos = (new class_243(0.0D, 0.0D, 1.0D)).method_1037(-((float)Math.toRadians((double)cameraPos.method_19329()))).method_1024(-((float)Math.toRadians((double)cameraPos.method_19330()))).method_1019(mc.field_1719.method_33571());
         Iterator var5 = mc.field_1687.method_18112().iterator();

         while(var5.hasNext()) {
            class_1297 entity = (class_1297)var5.next();
            if (entity != null && entity.method_5805() && entity != mc.field_1724) {
               Color color = this.getTracerColor(entity);
               if (color != null) {
               }
            }
         }

         mc.field_1690.method_42448().method_41748(prevBobView);
      }
   }

   private Color getTracerColor(class_1297 entity) {
      if (entity.method_5767() && (Boolean)this.invisiblesConfig.getValue()) {
         return (Color)this.invisiblesColorConfig.getValue();
      } else {
         if (entity instanceof class_1657) {
            class_1657 player = (class_1657)entity;
            if ((Boolean)this.playersConfig.getValue()) {
               if (Managers.SOCIAL.isFriend(player.method_5477())) {
                  return new Color(85, 200, 200, 255);
               }

               return (Color)this.playersColorConfig.getValue();
            }
         }

         if (EntityUtil.isMonster(entity) && (Boolean)this.monstersConfig.getValue()) {
            return (Color)this.monstersColorConfig.getValue();
         } else if ((EntityUtil.isPassive(entity) || EntityUtil.isNeutral(entity)) && (Boolean)this.animalsConfig.getValue()) {
            return (Color)this.animalsColorConfig.getValue();
         } else if (EntityUtil.isVehicle(entity) && (Boolean)this.vehiclesConfig.getValue()) {
            return (Color)this.vehiclesColorConfig.getValue();
         } else {
            return entity instanceof class_1542 && (Boolean)this.itemsConfig.getValue() ? (Color)this.itemsColorConfig.getValue() : null;
         }
      }
   }

   private double getTargetY(class_1297 entity) {
      double var10000;
      switch((TracersModule.Target)this.targetConfig.getValue()) {
      case FEET:
         var10000 = 0.0D;
         break;
      case TORSO:
         var10000 = (double)entity.method_17682() / 2.0D;
         break;
      case HEAD:
         var10000 = (double)entity.method_5751();
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   public static enum Target {
      FEET,
      TORSO,
      HEAD;

      // $FF: synthetic method
      private static TracersModule.Target[] $values() {
         return new TracersModule.Target[]{FEET, TORSO, HEAD};
      }
   }
}
