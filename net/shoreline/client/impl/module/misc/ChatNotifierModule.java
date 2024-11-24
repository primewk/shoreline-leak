package net.shoreline.client.impl.module.misc;

import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.entity.EntityDeathEvent;
import net.shoreline.client.impl.event.network.GameJoinEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.world.AddEntityEvent;
import net.shoreline.client.impl.event.world.RemoveEntityEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.chat.ChatUtil;

public class ChatNotifierModule extends ToggleModule {
   Config<Boolean> totemPopConfig = new BooleanConfig("TotemPop", "Notifies in chat when a player pops a totem", true);
   Config<Boolean> visualRangeConfig = new BooleanConfig("VisualRange", "Notifies in chat when player enters visual range", false);
   Config<Boolean> friendsConfig = new BooleanConfig("Friends", "Notifies for friends", false);
   Config<Boolean> grimConfig = new BooleanConfig("Grim", "Notifies you if the server you join is running GrimAC", false);

   public ChatNotifierModule() {
      super("ChatNotifier", "Notifies in chat", ModuleCategory.MISCELLANEOUS);
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2663) {
         class_2663 packet = (class_2663)var3;
         if (packet.method_11470() == 35 && (Boolean)this.totemPopConfig.getValue()) {
            class_1297 entity = packet.method_11469(mc.field_1687);
            if (!(entity instanceof class_1309) || entity.method_5476() == null) {
               return;
            }

            int totems = Managers.TOTEM.getTotems(entity);
            String playerName = entity.method_5476().getString();
            boolean isFriend = Managers.SOCIAL.isFriend(playerName);
            if (isFriend && !(Boolean)this.friendsConfig.getValue() || entity == mc.field_1724) {
               return;
            }

            ChatUtil.clientSendMessage((isFriend ? "§b" : "§s") + playerName + "§f popped §s" + totems + "§f totems");
         }
      }

   }

   @EventListener
   public void onGameJoin(GameJoinEvent event) {
      if ((Boolean)this.grimConfig.getValue()) {
         if (Managers.ANTICHEAT.isGrim()) {
            ChatUtil.clientSendMessage("This server is running GrimAC.");
         } else {
            ChatUtil.clientSendMessage("This server is not running GrimAC.");
         }
      }

   }

   @EventListener
   public void onAddEntity(AddEntityEvent event) {
      if ((Boolean)this.visualRangeConfig.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null) {
         String playerName = event.getEntity().method_5476().getString();
         boolean isFriend = Managers.SOCIAL.isFriend(playerName);
         if ((!isFriend || (Boolean)this.friendsConfig.getValue()) && event.getEntity() != mc.field_1724) {
            String var10000 = isFriend ? "§b" + playerName : playerName;
            ChatUtil.clientSendMessageRaw("§s[VisualRange] " + var10000 + "§f entered your visual range");
         }
      }
   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent event) {
      if ((Boolean)this.visualRangeConfig.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null) {
         String playerName = event.getEntity().method_5476().getString();
         boolean isFriend = Managers.SOCIAL.isFriend(playerName);
         if ((!isFriend || (Boolean)this.friendsConfig.getValue()) && event.getEntity() != mc.field_1724) {
            String var10000 = isFriend ? "§b" + playerName : "§c" + playerName;
            ChatUtil.clientSendMessageRaw("§s[VisualRange] " + var10000 + "§f left your visual range");
         }
      }
   }

   @EventListener
   public void onEntityDeath(EntityDeathEvent event) {
      if (event.getEntity().method_5476() != null && (Boolean)this.totemPopConfig.getValue()) {
         int totems = Managers.TOTEM.getTotems(event.getEntity());
         if (totems != 0) {
            String playerName = event.getEntity().method_5476().getString();
            boolean isFriend = Managers.SOCIAL.isFriend(playerName);
            if ((!isFriend || (Boolean)this.friendsConfig.getValue()) && event.getEntity() != mc.field_1724) {
               ChatUtil.clientSendMessage((isFriend ? "§b" : "§s") + playerName + "§f died after popping §s" + totems + "§f totems");
            }
         }
      }
   }
}
