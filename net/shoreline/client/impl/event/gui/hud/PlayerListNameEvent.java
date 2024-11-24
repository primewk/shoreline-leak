package net.shoreline.client.impl.event.gui.hud;

import java.util.UUID;
import net.minecraft.class_2561;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class PlayerListNameEvent extends Event {
   private class_2561 playerName;
   private final UUID id;

   public PlayerListNameEvent(class_2561 playerName, UUID id) {
      this.playerName = playerName;
      this.id = id;
   }

   public void setPlayerName(class_2561 playerName) {
      this.playerName = playerName;
   }

   public class_2561 getPlayerName() {
      return this.playerName;
   }

   public UUID getId() {
      return this.id;
   }
}
