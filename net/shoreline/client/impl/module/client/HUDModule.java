package net.shoreline.client.impl.module.client;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.class_1041;
import net.minecraft.class_1291;
import net.minecraft.class_1292;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2350;
import net.minecraft.class_2561;
import net.minecraft.class_3486;
import net.minecraft.class_408;
import net.minecraft.class_2350.class_2352;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.ConfigContainer;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.ScreenOpenEvent;
import net.shoreline.client.impl.event.gui.hud.RenderOverlayEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.StreamUtils;
import net.shoreline.client.util.render.ColorUtil;
import net.shoreline.client.util.render.animation.Animation;
import net.shoreline.client.util.render.animation.Easing;
import net.shoreline.client.util.string.EnumFormatter;

public class HUDModule extends ToggleModule {
   Config<Boolean> watermarkConfig = new BooleanConfig("Watermark", "Displays client name and version watermark", true);
   Config<Boolean> directionConfig = new BooleanConfig("Direction", "Displays facing direction", true);
   Config<Boolean> armorConfig = new BooleanConfig("Armor", "Displays player equipped armor and durability", true);
   Config<HUDModule.VanillaHud> potionHudConfig;
   Config<HUDModule.VanillaHud> itemNameConfig;
   Config<Boolean> potionEffectsConfig;
   Config<Boolean> potionColorsConfig;
   Config<Boolean> durabilityConfig;
   Config<Boolean> coordsConfig;
   Config<Boolean> netherCoordsConfig;
   Config<Boolean> serverBrandConfig;
   Config<Boolean> speedConfig;
   Config<Boolean> pingConfig;
   Config<Boolean> tpsConfig;
   Config<Boolean> fpsConfig;
   Config<Boolean> arraylistConfig;
   Config<HUDModule.Ordering> orderingConfig;
   Config<HUDModule.Rendering> renderingConfig;
   Config<HUDModule.RainbowMode> rainbowModeConfig;
   Config<Float> rainbowSpeedConfig;
   Config<Integer> rainbowSaturationConfig;
   Config<Integer> rainbowBrightnessConfig;
   Config<Float> rainbowDifferenceConfig;
   private final DecimalFormat decimal;
   private int rainbowOffset;
   private float topLeft;
   private float topRight;
   private float bottomLeft;
   private float bottomRight;
   private boolean renderingUp;
   private final Animation chatOpenAnimation;

   public HUDModule() {
      super("HUD", "Displays the HUD (heads up display) screen.", ModuleCategory.CLIENT);
      this.potionHudConfig = new EnumConfig("PotionHud", "Renders the Minecraft potion Hud", HUDModule.VanillaHud.HIDE, HUDModule.VanillaHud.values());
      this.itemNameConfig = new EnumConfig("ItemName", "Renders the Minecraft item name display", HUDModule.VanillaHud.HIDE, HUDModule.VanillaHud.values());
      this.potionEffectsConfig = new BooleanConfig("PotionEffects", "Displays active potion effects", true);
      this.potionColorsConfig = new BooleanConfig("PotionColors", "Displays active potion colors", true);
      this.durabilityConfig = new BooleanConfig("Durability", "Displays the current held items durability", false);
      this.coordsConfig = new BooleanConfig("Coords", "Displays world coordinates", true);
      this.netherCoordsConfig = new BooleanConfig("NetherCoords", "Displays nether coordinates", true, () -> {
         return (Boolean)this.coordsConfig.getValue();
      });
      this.serverBrandConfig = new BooleanConfig("ServerBrand", "Displays the current server brand", false);
      this.speedConfig = new BooleanConfig("Speed", "Displays the current movement speed of the player in kmh", true);
      this.pingConfig = new BooleanConfig("Ping", "Display server response time in ms", true);
      this.tpsConfig = new BooleanConfig("TPS", "Displays server ticks per second", true);
      this.fpsConfig = new BooleanConfig("FPS", "Displays game FPS", true);
      this.arraylistConfig = new BooleanConfig("Arraylist", "Displays a list of all active modules", true);
      this.orderingConfig = new EnumConfig("Ordering", "The ordering of the arraylist", HUDModule.Ordering.LENGTH, HUDModule.Ordering.values(), () -> {
         return (Boolean)this.arraylistConfig.getValue();
      });
      this.renderingConfig = new EnumConfig("Rendering", "The rendering mode of the HUD", HUDModule.Rendering.UP, HUDModule.Rendering.values());
      this.rainbowModeConfig = new EnumConfig("Rainbow", "The rendering mode for rainbow", HUDModule.RainbowMode.OFF, HUDModule.RainbowMode.values());
      this.rainbowSpeedConfig = new NumberConfig("Rainbow-Speed", "The speed for the rainbow color cycling", 0.1F, 50.0F, 100.0F);
      this.rainbowSaturationConfig = new NumberConfig("Rainbow-Saturation", "The saturation of rainbow colors", 0, 35, 100);
      this.rainbowBrightnessConfig = new NumberConfig("Rainbow-Brightness", "The brightness of rainbow colors", 0, 100, 100);
      this.rainbowDifferenceConfig = new NumberConfig("Rainbow-Difference", "The difference offset for rainbow colors", 0.1F, 40.0F, 100.0F);
      this.decimal = new DecimalFormat("0.0");
      this.chatOpenAnimation = new Animation(false, 200.0F, Easing.LINEAR);
   }

   private void arrayListRenderModule(RenderOverlayEvent.Post event, ToggleModule toggleModule) {
      Animation anim = toggleModule.getAnimation();
      float factor = (float)anim.getFactor();
      if (!(factor <= 0.01F) && !toggleModule.isHidden()) {
         String text = this.getFormattedModule(toggleModule);
         int width = RenderManager.textWidth(text);
         RenderManager.renderText(event.getContext(), text, (float)mc.method_22683().method_4486() - (float)width * factor - 1.0F, this.renderingUp ? this.topRight : this.bottomRight, this.getHudColor(this.rainbowOffset));
         if (this.renderingUp) {
            this.topRight += 9.0F;
         } else {
            this.bottomRight -= 9.0F;
         }

         ++this.rainbowOffset;
      }
   }

   @EventListener
   public void onRenderOverlayPost(RenderOverlayEvent.Post event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (mc.method_53526().method_53536()) {
            return;
         }

         class_1041 res = mc.method_22683();
         this.rainbowOffset = 0;
         this.topLeft = 2.0F;
         this.topRight = this.topLeft;
         this.bottomLeft = (float)res.method_4502() - 11.0F;
         this.bottomRight = this.bottomLeft;
         this.renderingUp = this.renderingConfig.getValue() == HUDModule.Rendering.UP;
         this.bottomLeft -= (float)(14.0D * this.chatOpenAnimation.getFactor());
         this.bottomRight -= (float)(14.0D * this.chatOpenAnimation.getFactor());
         if (this.potionHudConfig.getValue() == HUDModule.VanillaHud.MOVE && !mc.field_1724.method_6026().isEmpty()) {
            this.topRight += 27.0F;
         }

         if ((Boolean)this.watermarkConfig.getValue()) {
            RenderManager.renderText(event.getContext(), String.format("%s %s (%s%s)", "Shoreline", "1.0", "dev-7", !"72d9e9a".equals("null") ? "-72d9e9a" : ""), 2.0F, this.topLeft, this.getHudColor(this.rainbowOffset));
         }

         if ((Boolean)this.arraylistConfig.getValue()) {
            List<Module> modules = Managers.MODULE.getModules();
            Stream var10000 = modules.stream();
            Objects.requireNonNull(ToggleModule.class);
            var10000 = var10000.filter(ToggleModule.class::isInstance);
            Objects.requireNonNull(ToggleModule.class);
            Stream<ToggleModule> moduleStream = var10000.map(ToggleModule.class::cast);
            switch((HUDModule.Ordering)this.orderingConfig.getValue()) {
            case ALPHABETICAL:
               var10000 = StreamUtils.sortCached(moduleStream, ConfigContainer::getName);
               break;
            case LENGTH:
               var10000 = StreamUtils.sortCached(moduleStream, (m) -> {
                  return -RenderManager.textWidth(this.getFormattedModule(m));
               });
               break;
            default:
               throw new IncompatibleClassChangeError();
            }

            moduleStream = var10000;
            moduleStream.forEach((t) -> {
               this.arrayListRenderModule(event, t);
            });
         }

         boolean pos;
         if ((Boolean)this.potionEffectsConfig.getValue()) {
            Iterator var16 = mc.field_1724.method_6026().iterator();

            label255:
            while(true) {
               class_1291 effect;
               class_1293 e;
               do {
                  if (!var16.hasNext()) {
                     break label255;
                  }

                  e = (class_1293)var16.next();
                  effect = e.method_5579();
               } while(effect == class_1294.field_5925);

               pos = e.method_5578() > 1 && !e.method_48559();
               class_2561 duration = class_1292.method_5577(e, 1.0F, mc.field_1687.method_54719().method_54748());
               String text = String.format("%s %s§f%s", effect.method_5560().getString(), pos ? e.method_5578() + " " : "", e.method_48559() ? "" : duration.getString());
               int width = RenderManager.textWidth(text);
               RenderManager.renderText(event.getContext(), text, (float)(res.method_4486() - width) - 1.0F, this.renderingUp ? this.bottomRight : this.topRight, (Boolean)this.potionColorsConfig.getValue() ? effect.method_5556() : this.getHudColor(this.rainbowOffset));
               if (this.renderingUp) {
                  this.bottomRight -= 9.0F;
               } else {
                  this.topRight += 9.0F;
               }

               ++this.rainbowOffset;
            }
         }

         String text;
         int x;
         if ((Boolean)this.serverBrandConfig.getValue() && mc.method_1576() != null) {
            text = mc.method_1576().method_3827();
            x = RenderManager.textWidth(text);
            RenderManager.renderText(event.getContext(), text, (float)(res.method_4486() - x) - 1.0F, this.renderingUp ? this.bottomRight : this.topRight, this.getHudColor(this.rainbowOffset));
            if (this.renderingUp) {
               this.bottomRight -= 9.0F;
            } else {
               this.topRight += 9.0F;
            }

            ++this.rainbowOffset;
         }

         double x;
         double y;
         double z;
         if ((Boolean)this.speedConfig.getValue()) {
            x = mc.field_1724.method_23317() - mc.field_1724.field_6014;
            y = mc.field_1724.method_23321() - mc.field_1724.field_5969;
            z = Math.sqrt(x * x + y * y) / 1000.0D;
            double div = 1.388888888888889E-5D;
            float timer = Modules.TIMER.isEnabled() ? Modules.TIMER.getTimer() : 1.0F;
            double speed = z / div * (double)timer;
            String text = String.format("Speed §f%skm/h", this.decimal.format(speed));
            int width = RenderManager.textWidth(text);
            RenderManager.renderText(event.getContext(), text, (float)(res.method_4486() - width) - 1.0F, this.renderingUp ? this.bottomRight : this.topRight, this.getHudColor(this.rainbowOffset));
            if (this.renderingUp) {
               this.bottomRight -= 9.0F;
            } else {
               this.topRight += 9.0F;
            }

            ++this.rainbowOffset;
         }

         int latency;
         String axis;
         int n2;
         int i;
         if ((Boolean)this.durabilityConfig.getValue() && mc.field_1724.method_6047().method_7963()) {
            latency = mc.field_1724.method_6047().method_7936();
            x = mc.field_1724.method_6047().method_7919();
            axis = "Durability ";
            String text2 = String.valueOf(latency - x);
            n2 = RenderManager.textWidth(axis);
            i = RenderManager.textWidth(text2);
            Color color = ColorUtil.hslToColor((float)(latency - x) / (float)latency * 120.0F, 100.0F, 50.0F, 1.0F);
            RenderManager.renderText(event.getContext(), axis, (float)(res.method_4486() - n2 - i) - 1.0F, this.renderingUp ? this.bottomRight : this.topRight, this.getHudColor(this.rainbowOffset));
            RenderManager.renderText(event.getContext(), text2, (float)(res.method_4486() - i) - 1.0F, this.renderingUp ? this.bottomRight : this.topRight, color.getRGB());
            if (this.renderingUp) {
               this.bottomRight -= 9.0F;
            } else {
               this.topRight += 9.0F;
            }

            ++this.rainbowOffset;
         }

         String dir;
         int y;
         if ((Boolean)this.pingConfig.getValue() && !mc.method_1542()) {
            latency = Modules.FAST_LATENCY.isEnabled() ? (int)Modules.FAST_LATENCY.getLatency() : Managers.NETWORK.getClientLatency();
            dir = String.format("Ping §f%dms", latency);
            y = RenderManager.textWidth(dir);
            RenderManager.renderText(event.getContext(), dir, (float)(res.method_4486() - y) - 1.0F, this.renderingUp ? this.bottomRight : this.topRight, this.getHudColor(this.rainbowOffset));
            if (this.renderingUp) {
               this.bottomRight -= 9.0F;
            } else {
               this.topRight += 9.0F;
            }

            ++this.rainbowOffset;
         }

         int n1;
         if ((Boolean)this.tpsConfig.getValue()) {
            float curr = Managers.TICK.getTpsCurrent();
            float avg = Managers.TICK.getTpsAverage();
            axis = String.format("TPS §f%s §7[§f%s§7]", this.decimal.format((double)avg), this.decimal.format((double)curr));
            n1 = RenderManager.textWidth(axis);
            RenderManager.renderText(event.getContext(), axis, (float)(res.method_4486() - n1) - 1.0F, this.renderingUp ? this.bottomRight : this.topRight, this.getHudColor(this.rainbowOffset));
            if (this.renderingUp) {
               this.bottomRight -= 9.0F;
            } else {
               this.topRight += 9.0F;
            }

            ++this.rainbowOffset;
         }

         if ((Boolean)this.fpsConfig.getValue()) {
            text = String.format("FPS §f%d", mc.method_47599());
            x = RenderManager.textWidth(text);
            RenderManager.renderText(event.getContext(), text, (float)(res.method_4486() - x) - 1.0F, this.renderingUp ? this.bottomRight : this.topRight, this.getHudColor(this.rainbowOffset));
            ++this.rainbowOffset;
         }

         if ((Boolean)this.coordsConfig.getValue()) {
            x = mc.field_1724.method_23317();
            y = mc.field_1724.method_23318();
            z = mc.field_1724.method_23321();
            boolean nether = mc.field_1687.method_27983() == class_1937.field_25180;
            RenderManager.renderText(event.getContext(), String.format("XYZ §f%s, %s, %s " + ((Boolean)this.netherCoordsConfig.getValue() ? "§7[§f%s, %s§7]" : ""), this.decimal.format(x), this.decimal.format(y), this.decimal.format(z), nether ? this.decimal.format(x * 8.0D) : this.decimal.format(x / 8.0D), nether ? this.decimal.format(z * 8.0D) : this.decimal.format(z / 8.0D)), 2.0F, this.bottomLeft, this.getHudColor(this.rainbowOffset));
            this.bottomLeft -= 9.0F;
            ++this.rainbowOffset;
         }

         if ((Boolean)this.directionConfig.getValue()) {
            class_2350 direction = mc.field_1724.method_5735();
            dir = EnumFormatter.formatDirection(direction);
            axis = EnumFormatter.formatAxis(direction.method_10166());
            pos = direction.method_10171() == class_2352.field_11056;
            RenderManager.renderText(event.getContext(), String.format("%s §7[§f%s%s§7]", dir, axis, pos ? "+" : "-"), 2.0F, this.bottomLeft, this.getHudColor(this.rainbowOffset));
            ++this.rainbowOffset;
         }

         if ((Boolean)this.armorConfig.getValue()) {
            class_1297 riding = mc.field_1724.method_5854();
            x = res.method_4486() / 2 + 15;
            y = res.method_4502();
            n1 = mc.field_1724.method_5748();
            n2 = Math.min(mc.field_1724.method_5669(), n1);
            if (!mc.field_1724.method_5777(class_3486.field_15517) && n2 >= n1) {
               if (riding instanceof class_1309) {
                  class_1309 entity = (class_1309)riding;
                  y -= 45 + (int)Math.ceil((double)((entity.method_6063() - 1.0F) / 20.0F)) * 10;
               } else if (riding != null) {
                  y -= 45;
               } else {
                  y -= mc.field_1724.method_7337() ? (mc.field_1724.method_3144() ? 45 : 38) : 55;
               }
            } else {
               y -= 65;
            }

            for(i = 3; i >= 0; --i) {
               class_1799 armor = (class_1799)mc.field_1724.method_31548().field_7548.get(i);
               event.getContext().method_51427(armor, x, y);
               event.getContext().method_51431(mc.field_1772, armor, x, y);
               x += 18;
            }
         }
      }

   }

   @EventListener
   public void onChatOpen(ScreenOpenEvent event) {
      if (event.getScreen() == null && this.chatOpenAnimation.getState()) {
         this.chatOpenAnimation.setState(false);
      } else if (event.getScreen() instanceof class_408) {
         this.chatOpenAnimation.setState(true);
      }

   }

   @EventListener
   public void onRenderOverlayStatusEffect(RenderOverlayEvent.StatusEffect event) {
      if (this.potionHudConfig.getValue() == HUDModule.VanillaHud.HIDE) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderOverlayItemName(RenderOverlayEvent.ItemName event) {
      if (this.itemNameConfig.getValue() != HUDModule.VanillaHud.KEEP) {
         event.cancel();
      }

      if (this.itemNameConfig.getValue() == HUDModule.VanillaHud.MOVE) {
         class_1041 window = mc.method_22683();
         int x = window.method_4486() / 2 - 90;
         int y = window.method_4502() - 49;
         boolean armor = !mc.field_1724.method_31548().field_7548.isEmpty();
         if (mc.field_1724.method_6067() > 0.0F) {
            y -= 9;
         }

         if (armor) {
            y -= 9;
         }

         event.setX(x);
         event.setY(y);
      }

   }

   private int getHudColor(int rainbowOffset) {
      int var10000;
      switch((HUDModule.RainbowMode)this.rainbowModeConfig.getValue()) {
      case OFF:
         var10000 = Modules.COLORS.getRGB();
         break;
      case STATIC:
         var10000 = this.rainbow(1L);
         break;
      case GRADIENT:
         var10000 = this.rainbow((long)rainbowOffset);
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   private String getFormattedModule(Module module) {
      String metadata = module.getModuleData();
      return !metadata.equals("ARRAYLIST_INFO") ? String.format("%s §7[§f%s§7]", module.getName(), module.getModuleData()) : module.getName();
   }

   private int rainbow(long offset) {
      float hue = (float)(((double)System.currentTimeMillis() * (double)((Float)this.rainbowSpeedConfig.getValue() / 10.0F) + (double)(offset * 500L)) % (double)(30000.0F / ((Float)this.rainbowDifferenceConfig.getValue() / 100.0F)) / (double)(30000.0F / ((Float)this.rainbowDifferenceConfig.getValue() / 20.0F)));
      return Color.HSBtoRGB(hue, (float)(Integer)this.rainbowSaturationConfig.getValue() / 100.0F, (float)(Integer)this.rainbowBrightnessConfig.getValue() / 100.0F);
   }

   public static int alpha(long offset) {
      offset = offset * 2L + 10L;
      float[] hsb = new float[3];
      Color color = Modules.COLORS.getColor();
      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
      float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + 50.0F / (float)offset * 2.0F) % 2.0F - 1.0F);
      brightness = 0.5F + 0.5F * brightness;
      hsb[2] = brightness % 2.0F;
      return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
   }

   public float getChatAnimation() {
      return (float)this.chatOpenAnimation.getFactor();
   }

   public static enum VanillaHud {
      MOVE,
      HIDE,
      KEEP;

      // $FF: synthetic method
      private static HUDModule.VanillaHud[] $values() {
         return new HUDModule.VanillaHud[]{MOVE, HIDE, KEEP};
      }
   }

   public static enum Ordering {
      LENGTH,
      ALPHABETICAL;

      // $FF: synthetic method
      private static HUDModule.Ordering[] $values() {
         return new HUDModule.Ordering[]{LENGTH, ALPHABETICAL};
      }
   }

   public static enum Rendering {
      UP,
      DOWN;

      // $FF: synthetic method
      private static HUDModule.Rendering[] $values() {
         return new HUDModule.Rendering[]{UP, DOWN};
      }
   }

   public static enum RainbowMode {
      OFF,
      GRADIENT,
      STATIC;

      // $FF: synthetic method
      private static HUDModule.RainbowMode[] $values() {
         return new HUDModule.RainbowMode[]{OFF, GRADIENT, STATIC};
      }
   }
}
