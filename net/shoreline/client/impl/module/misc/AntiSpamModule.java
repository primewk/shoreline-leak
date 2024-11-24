package net.shoreline.client.impl.module.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_2596;
import net.minecraft.class_7438;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PacketEvent;

public class AntiSpamModule extends ToggleModule {
   Config<Boolean> unicodeConfig = new BooleanConfig("Unicode", "Prevents unicode characters from being rendered in chat", false);
   private final Map<UUID, String> messages = new HashMap();

   public AntiSpamModule() {
      super("AntiSpam", "Prevents players from spamming the game chat", ModuleCategory.MISCELLANEOUS);
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1724 != null) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_7438) {
            class_7438 packet = (class_7438)var3;
            if ((Boolean)this.unicodeConfig.getValue()) {
               String msg = packet.comp_1102().comp_1090();
               Pattern pattern = Pattern.compile("[\\x00-\\x7F]", 2);
               Matcher matcher = pattern.matcher(msg);
               if (matcher.find()) {
                  event.cancel();
                  return;
               }
            }

            UUID sender = packet.comp_1099();
            String chatMessage = packet.comp_1102().comp_1090();
            String lastMessage = (String)this.messages.get(sender);
            if (chatMessage.equalsIgnoreCase(lastMessage)) {
               event.cancel();
            } else if (lastMessage != null) {
               this.messages.replace(sender, chatMessage);
            } else {
               this.messages.put(sender, chatMessage);
            }
         }

      }
   }
}
