package net.shoreline.client.impl.module.misc;

import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.entity.player.PushEntityEvent;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.util.world.FakePlayerEntity;

public class FakePlayerModule extends ToggleModule {
   private FakePlayerEntity fakePlayer;

   public FakePlayerModule() {
      super("FakePlayer", "Spawns an indestructible client-side player", ModuleCategory.MISCELLANEOUS);
   }

   public void onEnable() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         this.fakePlayer = new FakePlayerEntity(mc.field_1724, "FakePlayer");
         this.fakePlayer.spawnPlayer();
      }

   }

   public void onDisable() {
      if (this.fakePlayer != null) {
         this.fakePlayer.despawnPlayer();
         this.fakePlayer = null;
      }

   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      this.fakePlayer = null;
      this.disable();
   }

   @EventListener
   public void onPushEntity(PushEntityEvent event) {
      if (event.getPushed().equals(mc.field_1724) && event.getPusher().equals(this.fakePlayer)) {
         event.setCanceled(true);
      }

   }
}
