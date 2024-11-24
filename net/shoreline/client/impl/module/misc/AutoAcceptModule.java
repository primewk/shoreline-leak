package net.shoreline.client.impl.module.misc;

import java.util.Iterator;
import net.minecraft.class_2596;
import net.minecraft.class_7438;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.chat.ChatUtil;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;

public class AutoAcceptModule extends ToggleModule {
   private final Timer acceptTimer = new CacheTimer();
   Config<Float> delayConfig = new NumberConfig("Delay", "The delay before accepting teleport requests", 0.0F, 3.0F, 10.0F);

   public AutoAcceptModule() {
      super("AutoAccept", "Automatically accepts teleport requests", ModuleCategory.MISCELLANEOUS);
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_7438) {
         class_7438 packet = (class_7438)var3;
         String text = packet.comp_1102().comp_1090();
         if ((text.contains("has requested to teleport to you.") || text.contains("has requested you teleport to them.")) && this.acceptTimer.passed((Float)this.delayConfig.getValue() * 1000.0F)) {
            Iterator var4 = Managers.SOCIAL.getFriends().iterator();

            while(var4.hasNext()) {
               String friend = (String)var4.next();
               if (text.contains(friend)) {
                  ChatUtil.serverSendMessage("/tpaccept");
                  break;
               }
            }
         }
      }

   }
}
