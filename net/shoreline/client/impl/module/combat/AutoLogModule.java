package net.shoreline.client.impl.module.combat;

import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_2824;
import net.minecraft.class_742;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.world.FakePlayerEntity;

public class AutoLogModule extends ToggleModule {
   Config<Float> healthConfig = new NumberConfig("Health", "Disconnects when player reaches this health", 0.1F, 5.0F, 19.0F);
   Config<Boolean> healthTotemConfig = new BooleanConfig("HealthTotems", "Totem check for health config", true);
   Config<Boolean> onRenderConfig = new BooleanConfig("OnRender", "Disconnects when a player enters render distance", false);
   Config<Boolean> noTotemConfig = new BooleanConfig("NoTotems", "Disconnects when player has no totems in the inventory", false);
   Config<Integer> totemsConfig = new NumberConfig("Totems", "The number of totems before disconnecting", 0, 1, 5);
   Config<Boolean> illegalDisconnectConfig = new BooleanConfig("IllegalDisconnect", "Disconnects from the server using invalid packets", false);

   public AutoLogModule() {
      super("AutoLog", "Automatically disconnects from server during combat", ModuleCategory.COMBAT);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if ((Boolean)this.onRenderConfig.getValue()) {
            class_742 player = (class_742)mc.field_1687.method_18456().stream().filter((p) -> {
               return this.checkEnemy(p);
            }).findFirst().orElse((Object)null);
            if (player != null) {
               this.playerDisconnect("[AutoLog] %s came into render distance.", player.method_5477().getString());
               return;
            }
         }

         float health = mc.field_1724.method_6032() + mc.field_1724.method_6067();
         int totems = Managers.INVENTORY.count(class_1802.field_8288);
         boolean b2 = totems <= (Integer)this.totemsConfig.getValue();
         if (health <= (Float)this.healthConfig.getValue()) {
            if (!(Boolean)this.healthTotemConfig.getValue()) {
               this.playerDisconnect("[AutoLog] logged out with %d hearts remaining.", (int)health);
               return;
            }

            if (b2) {
               this.playerDisconnect("[AutoLog] logged out with %d totems and %d hearts remaining.", totems, (int)health);
               return;
            }
         }

         if (b2 && (Boolean)this.noTotemConfig.getValue()) {
            this.playerDisconnect("[AutoLog] logged out with %d totems remaining.", totems);
         }

      }
   }

   private void playerDisconnect(String disconnectReason, Object... args) {
      if ((Boolean)this.illegalDisconnectConfig.getValue()) {
         Managers.NETWORK.sendPacket(class_2824.method_34206(mc.field_1724, false));
         this.disable();
      } else if (mc.method_1562() == null) {
         mc.field_1687.method_8525();
         this.disable();
      } else {
         disconnectReason = String.format(disconnectReason, args);
         mc.method_1562().method_48296().method_10747(class_2561.method_30163(disconnectReason));
         this.disable();
      }
   }

   private boolean checkEnemy(class_742 player) {
      return !Managers.SOCIAL.isFriend(player.method_5477()) && !(player instanceof FakePlayerEntity);
   }
}
