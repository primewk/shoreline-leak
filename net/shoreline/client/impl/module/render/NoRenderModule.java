package net.shoreline.client.impl.module.render;

import com.google.common.collect.Lists;
import java.util.Iterator;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_2398;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2675;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_3486;
import net.minecraft.class_1297.class_5529;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.chunk.light.RenderSkylightEvent;
import net.shoreline.client.impl.event.gui.hud.RenderOverlayEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.particle.ParticleEvent;
import net.shoreline.client.impl.event.render.HurtCamEvent;
import net.shoreline.client.impl.event.render.RenderFloatingItemEvent;
import net.shoreline.client.impl.event.render.RenderFogEvent;
import net.shoreline.client.impl.event.render.RenderNauseaEvent;
import net.shoreline.client.impl.event.render.RenderWorldBorderEvent;
import net.shoreline.client.impl.event.render.block.RenderTileEntityEvent;
import net.shoreline.client.impl.event.render.entity.RenderArmorEvent;
import net.shoreline.client.impl.event.render.entity.RenderFireworkRocketEvent;
import net.shoreline.client.impl.event.render.entity.RenderItemEvent;
import net.shoreline.client.impl.event.render.entity.RenderWitherSkullEvent;
import net.shoreline.client.impl.event.toast.RenderToastEvent;
import net.shoreline.client.impl.event.world.BlindnessEvent;

public class NoRenderModule extends ToggleModule {
   Config<Boolean> hurtCamConfig = new BooleanConfig("NoHurtCam", "Prevents the hurt camera shake effect from rendering", true);
   Config<Boolean> antiCrashConfig = new BooleanConfig("NoServerCrash", "Prevents server packets from crashing the client", false);
   Config<Boolean> armorConfig = new BooleanConfig("Armor", "Prevents armor pieces from rendering", false);
   Config<Boolean> fireOverlayConfig = new BooleanConfig("Overlay-Fire", "Prevents the fire Hud overlay from rendering", true);
   Config<Boolean> waterOverlayConfig = new BooleanConfig("Overlay-Water", "Prevents the water Hud overlay from rendering", true);
   Config<Boolean> blockOverlayConfig = new BooleanConfig("Overlay-Block", "Prevents the block Hud overlay from rendering", true);
   Config<Boolean> spyglassOverlayConfig = new BooleanConfig("Overlay-Spyglass", "Prevents the spyglass Hud overlay from rendering", false);
   Config<Boolean> pumpkinOverlayConfig = new BooleanConfig("Overlay-Pumpkin", "Prevents the pumpkin Hud overlay from rendering", true);
   Config<Boolean> bossOverlayConfig = new BooleanConfig("Overlay-BossBar", "Prevents the boss bar Hud overlay from rendering", true);
   Config<Boolean> nauseaConfig = new BooleanConfig("Nausea", "Prevents nausea effect from rendering (includes portal effect)", false);
   Config<Boolean> blindnessConfig = new BooleanConfig("Blindness", "Prevents blindness effect from rendering", false);
   Config<Boolean> frostbiteConfig = new BooleanConfig("Frostbite", "Prevents frostbite effect from rendering", false);
   Config<Boolean> skylightConfig = new BooleanConfig("Skylight", "Prevents skylight from rendering", true);
   Config<Boolean> witherSkullsConfig = new BooleanConfig("WitherSkulls", "Prevents flying wither skulls from rendering", false);
   Config<Boolean> tileEntitiesConfig = new BooleanConfig("TileEntities", "Prevents special tile entity properties from rendering (i.e. enchantment table books or cutting table saws)", false);
   Config<Boolean> fireworksConfig = new BooleanConfig("Fireworks", "Prevents firework particles from rendering", true);
   Config<Boolean> explosionsConfig = new BooleanConfig("Explosions", "Prevents explosion particles from rendering", true);
   Config<Boolean> campfiresConfig = new BooleanConfig("Campfires", "Prevents campfire particles from rendering", false);
   Config<Boolean> totemConfig = new BooleanConfig("Totems", "Prevents totem particles from rendering", false);
   Config<Boolean> worldBorderConfig = new BooleanConfig("WorldBorder", "Prevents world border from rendering", false);
   Config<Boolean> interpolationConfig = new BooleanConfig("Interpolation", "Entities will be rendered at their server positions", false);
   Config<NoRenderModule.FogRender> fogConfig;
   Config<NoRenderModule.ItemRender> itemsConfig;
   Config<Boolean> guiToastConfig;

   public NoRenderModule() {
      super("NoRender", "Prevents certain game elements from rendering", ModuleCategory.RENDER);
      this.fogConfig = new EnumConfig("Fog", "Prevents fog from rendering in the world", NoRenderModule.FogRender.OFF, NoRenderModule.FogRender.values());
      this.itemsConfig = new EnumConfig("Items", "Prevents dropped items from rendering", NoRenderModule.ItemRender.OFF, NoRenderModule.ItemRender.values());
      this.guiToastConfig = new BooleanConfig("GuiToast", "Prevents advancements from rendering", true);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (this.itemsConfig.getValue() == NoRenderModule.ItemRender.REMOVE && event.getStage() == EventStage.PRE) {
         Iterator var2 = Lists.newArrayList(mc.field_1687.method_18112()).iterator();

         while(var2.hasNext()) {
            class_1297 entity = (class_1297)var2.next();
            if (entity instanceof class_1542) {
               mc.field_1687.method_2945(entity.method_5628(), class_5529.field_26999);
            }
         }
      }

   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1687 != null) {
         if ((Boolean)this.antiCrashConfig.getValue()) {
            class_2596 var6 = event.getPacket();
            if (var6 instanceof class_2708) {
               class_2708 packet = (class_2708)var6;
               if (packet.method_11734() > 3.0E7D || packet.method_11735() > (double)mc.field_1687.method_31600() || packet.method_11738() > 3.0E7D || packet.method_11734() < -3.0E7D || packet.method_11735() < (double)mc.field_1687.method_31607() || packet.method_11738() < -3.0E7D) {
                  event.cancel();
                  return;
               }
            }

            var6 = event.getPacket();
            if (var6 instanceof class_2664) {
               class_2664 packet = (class_2664)var6;
               if (packet.method_11475() > 3.0E7D || packet.method_11477() > (double)mc.field_1687.method_31600() || packet.method_11478() > 3.0E7D || packet.method_11475() < -3.0E7D || packet.method_11477() < (double)mc.field_1687.method_31607() || packet.method_11478() < -3.0E7D || packet.method_11476() > 1000.0F || packet.method_11479().size() > 1000 || packet.method_11472() > 1000.0F || packet.method_11473() > 1000.0F || packet.method_11474() > 1000.0F || packet.method_11472() < -1000.0F || packet.method_11473() < -1000.0F || packet.method_11474() < -1000.0F) {
                  event.cancel();
                  return;
               }
            }

            var6 = event.getPacket();
            if (var6 instanceof class_2743) {
               class_2743 packet = (class_2743)var6;
               if (packet.method_11815() > 1000 || packet.method_11816() > 1000 || packet.method_11819() > 1000 || packet.method_11815() < -1000 || packet.method_11816() < -1000 || packet.method_11819() < -1000) {
                  event.cancel();
                  return;
               }
            }

            var6 = event.getPacket();
            if (var6 instanceof class_2675) {
               class_2675 packet = (class_2675)var6;
               if (packet.method_11545() > 500) {
                  event.cancel();
               }
            }
         }

      }
   }

   @EventListener
   public void onHurtCam(HurtCamEvent event) {
      if ((Boolean)this.hurtCamConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderArmor(RenderArmorEvent event) {
      if ((Boolean)this.armorConfig.getValue() && event.getEntity() instanceof class_1657) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderOverlayFire(RenderOverlayEvent.Fire event) {
      if ((Boolean)this.fireOverlayConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderOverlayWater(RenderOverlayEvent.Water event) {
      if ((Boolean)this.waterOverlayConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderOverlayBlock(RenderOverlayEvent.Block event) {
      if ((Boolean)this.blockOverlayConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderOverlaySpyglass(RenderOverlayEvent.Spyglass event) {
      if ((Boolean)this.spyglassOverlayConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderOverlayPumpkin(RenderOverlayEvent.Pumpkin event) {
      if ((Boolean)this.pumpkinOverlayConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderOverlayBossBar(RenderOverlayEvent.BossBar event) {
      if ((Boolean)this.bossOverlayConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderOverlayFrostbite(RenderOverlayEvent.Frostbite event) {
      if ((Boolean)this.frostbiteConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderNausea(RenderNauseaEvent event) {
      if ((Boolean)this.nauseaConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onBlindness(BlindnessEvent event) {
      if ((Boolean)this.blindnessConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderSkylight(RenderSkylightEvent event) {
      if ((Boolean)this.skylightConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderWitherSkull(RenderWitherSkullEvent event) {
      if ((Boolean)this.witherSkullsConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderEnchantingTableBook(RenderTileEntityEvent.EnchantingTableBook event) {
      if ((Boolean)this.tileEntitiesConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onParticle(ParticleEvent event) {
      if ((Boolean)this.explosionsConfig.getValue() && (event.getParticleType() == class_2398.field_11236 || event.getParticleType() == class_2398.field_11221) || (Boolean)this.fireworksConfig.getValue() && event.getParticleType() == class_2398.field_11248 || (Boolean)this.campfiresConfig.getValue() && event.getParticleType() == class_2398.field_17430) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderFireworkRocket(RenderFireworkRocketEvent event) {
      if ((Boolean)this.fireworksConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderFloatingItem(RenderFloatingItemEvent event) {
      if ((Boolean)this.totemConfig.getValue() && event.getFloatingItem() == class_1802.field_8288) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderWorldBorder(RenderWorldBorderEvent event) {
      if ((Boolean)this.worldBorderConfig.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderFog(RenderFogEvent event) {
      if (this.fogConfig.getValue() == NoRenderModule.FogRender.LIQUID_VISION && mc.field_1724 != null && mc.field_1724.method_5777(class_3486.field_15518)) {
         event.cancel();
      } else if (this.fogConfig.getValue() == NoRenderModule.FogRender.CLEAR) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderItem(RenderItemEvent event) {
      if (this.itemsConfig.getValue() == NoRenderModule.ItemRender.HIDE) {
         event.cancel();
      }

   }

   @EventListener
   public void onRenderToast(RenderToastEvent event) {
      if ((Boolean)this.guiToastConfig.getValue()) {
         event.cancel();
      }

   }

   public static enum FogRender {
      CLEAR,
      LIQUID_VISION,
      OFF;

      // $FF: synthetic method
      private static NoRenderModule.FogRender[] $values() {
         return new NoRenderModule.FogRender[]{CLEAR, LIQUID_VISION, OFF};
      }
   }

   public static enum ItemRender {
      REMOVE,
      HIDE,
      OFF;

      // $FF: synthetic method
      private static NoRenderModule.ItemRender[] $values() {
         return new NoRenderModule.ItemRender[]{REMOVE, HIDE, OFF};
      }
   }
}
