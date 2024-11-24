package net.shoreline.client.impl.module.render;

import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_2596;
import net.minecraft.class_2703;
import net.minecraft.class_7828;
import net.minecraft.class_1297.class_5529;
import net.minecraft.class_2703.class_2705;
import net.minecraft.class_2703.class_5893;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.api.waypoint.Waypoint;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.impl.event.world.RemoveEntityEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;

public class WaypointsModule extends ToggleModule {
   Config<Boolean> logoutsConfig = new BooleanConfig("LogoutPoints", "Marks the position of player logouts", false);
   Config<Boolean> deathsConfig = new BooleanConfig("DeathPoints", "Marks the position of player deaths", false);

   public WaypointsModule() {
      super("Waypoints", "Renders a waypoint at marked locations", ModuleCategory.RENDER);
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1687 != null) {
         class_2596 var4 = event.getPacket();
         if (var4 instanceof class_2703) {
            class_2703 packet = (class_2703)var4;
            if ((Boolean)this.logoutsConfig.getValue()) {
               Iterator var10 = packet.method_46329().iterator();

               while(var10.hasNext()) {
                  class_2705 entry = (class_2705)var10.next();
                  GameProfile profile = entry.comp_1107();
                  if (profile.getName() != null && !profile.getName().isEmpty() && profile.getId() != null) {
                     class_1657 player = mc.field_1687.method_18470(profile.getId());
                     if (player != null && packet.method_46327().contains(class_5893.field_29136)) {
                        Managers.WAYPOINT.remove(String.format("%s's Logout", player.method_5477().getString()));
                     }
                  }
               }

               return;
            }
         }

         var4 = event.getPacket();
         if (var4 instanceof class_7828) {
            class_7828 packet = (class_7828)var4;
            if ((Boolean)this.logoutsConfig.getValue() && mc.field_1724.method_5682() != null) {
               String ip = mc.field_1724.method_5682().method_3819();
               String serverIp = mc.method_1542() ? "Singleplayer" : ip;
               Iterator var6 = packet.comp_1105().iterator();

               while(var6.hasNext()) {
                  UUID id = (UUID)var6.next();
                  class_1657 player = mc.field_1687.method_18470(id);
                  if (player != null) {
                     Managers.WAYPOINT.register(new Waypoint(String.format("%s's Logout", player.method_5477().getString()), serverIp, player.field_6014, player.field_6036, player.field_5969));
                  }
               }
            }
         }

      }
   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent event) {
      if (event.getRemovalReason() == class_5529.field_26998 && event.getEntity() == mc.field_1724 && (Boolean)this.deathsConfig.getValue()) {
         String serverIp = mc.method_1542() ? "Singleplayer" : mc.field_1687.method_8503().method_3819();
         Managers.WAYPOINT.remove("Last Death");
         Managers.WAYPOINT.register(new Waypoint("Last Death", serverIp, mc.field_1724.field_3926, mc.field_1724.field_3940, mc.field_1724.field_3924));
      }

   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (mc.field_1724 != null) {
         Iterator var2 = Managers.WAYPOINT.getWaypoints().iterator();

         while(var2.hasNext()) {
            Waypoint waypoint = (Waypoint)var2.next();
            class_238 waypointBox = class_1657.field_18135.method_30757(waypoint.getPos());
            RenderManager.renderBoundingBox(event.getMatrices(), waypointBox, 2.5F, Modules.COLORS.getRGB(145));
            RenderManager.renderSign(event.getMatrices(), waypoint.getName(), waypointBox.method_1005().method_1031(0.0D, 1.0D, 0.0D));
         }

      }
   }
}
