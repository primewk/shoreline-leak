package net.shoreline.client.impl.module.render;

import java.util.Iterator;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.gui.hud.PlayerListColumnsEvent;
import net.shoreline.client.impl.event.gui.hud.PlayerListEvent;
import net.shoreline.client.impl.event.gui.hud.PlayerListNameEvent;
import net.shoreline.client.init.Managers;

public class ExtraTabModule extends ToggleModule {
   Config<Integer> sizeConfig = new NumberConfig("Size", "The number of players to show", 80, 200, 1000);
   Config<Integer> columnsConfig = new NumberConfig("Columns", "The number columns to show.", 1, 20, 100);
   Config<Boolean> selfConfig = new BooleanConfig("Self", "Highlights yourself in the tab list.", false);
   Config<Boolean> friendsConfig = new BooleanConfig("Friends", "Highlights friends in the tab list.", true);

   public ExtraTabModule() {
      super("ExtraTab", "Expands the tab list size to allow for more players", ModuleCategory.RENDER);
   }

   @EventListener
   public void onPlayerListName(PlayerListNameEvent event) {
      if ((Boolean)this.selfConfig.getValue() && event.getPlayerName().getString().contains(mc.method_53462().getName())) {
         event.cancel();
         event.setPlayerName(class_2561.method_30163("§s" + event.getPlayerName().getString()));
      } else if ((Boolean)this.friendsConfig.getValue()) {
         Iterator var2 = Managers.SOCIAL.getFriends().iterator();

         while(var2.hasNext()) {
            String s = (String)var2.next();
            if (event.getPlayerName().getString().contains(s)) {
               event.cancel();
               class_124 var10001 = class_124.field_1075;
               event.setPlayerName(class_2561.method_30163(var10001 + event.getPlayerName().getString()));
               break;
            }
         }
      }

   }

   @EventListener
   public void onPlayerList(PlayerListEvent event) {
      event.cancel();
      event.setSize((Integer)this.sizeConfig.getValue());
   }

   @EventListener
   public void onPlayerListColumns(PlayerListColumnsEvent event) {
      event.cancel();
      event.setTabHeight((Integer)this.columnsConfig.getValue());
   }
}
